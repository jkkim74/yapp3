# YBOX SNS 로그인 설계서 (v5)

## 설계 컨셉

```
SNS 로그인한 사용자의 경우, SNS ID를 user_id로 그대로 유지하여
YBOX에서 활동한 이력에 대한 정보의 키는 user_id에 있는 SNS ID로 한다.

댓글 등록과 같이 KT ID가 노출되는 경우에만,
KT ID를 가입한 사용자의 경우 KT ID를 보여준다.
```

---

## 1. SNS 로그인

### 1-0. 적용 범위

```
★ 아래 내용은 SNS 로그인한 경우에만 해당된다.
  기존 회선 로그인 / KT ID 직접 로그인은 변경 없음.
```

### 1-1. 테이블 컬럼 추가

```sql
-- ================================================================
-- [DDL-001] TB_USER_KT 컬럼 추가 (준회원 테이블)
-- ================================================================
ALTER TABLE TB_USER_KT ADD COLUMN CREDENTIAL_ID VARCHAR(200);
ALTER TABLE TB_USER_KT ADD COLUMN KT_ID VARCHAR(100);

COMMENT ON COLUMN TB_USER_KT.CREDENTIAL_ID IS 'IAMUI credential_id (영구 사용자 식별자)';
COMMENT ON COLUMN TB_USER_KT.KT_ID IS '실제 KT ID (kt.com에서 KT ID 가입 후 설정)';

CREATE INDEX IDX_USER_KT_CREDENTIAL_ID ON TB_USER_KT (CREDENTIAL_ID) 
WHERE CREDENTIAL_ID IS NOT NULL;

CREATE INDEX IDX_USER_KT_KT_ID ON TB_USER_KT (KT_ID) 
WHERE KT_ID IS NOT NULL;


-- ================================================================
-- [DDL-002] TB_USER 컬럼 추가 (정회원 테이블)
-- ================================================================
ALTER TABLE TB_USER ADD COLUMN CREDENTIAL_ID VARCHAR(200);
ALTER TABLE TB_USER ADD COLUMN KT_ID VARCHAR(100);

COMMENT ON COLUMN TB_USER.CREDENTIAL_ID IS 'IAMUI credential_id (영구 사용자 식별자)';
COMMENT ON COLUMN TB_USER.KT_ID IS '실제 KT ID (KT ID 보유 정회원인 경우)';

CREATE INDEX IDX_USER_CREDENTIAL_ID ON TB_USER (CREDENTIAL_ID) 
WHERE CREDENTIAL_ID IS NOT NULL;
```

### 1-2. 준회원 - KT ID가 없는 신규 사용자의 SNS 로그인

```
┌─────────────────────────────────────────────────────────────────────┐
│  케이스: KT ID 미보유 신규 사용자 → SNS로 IAMUI 로그인              │
│  (kt.com에 KT ID를 가입하지 않은 사용자)                            │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  [1] IAMUI에서 SNS 로그인 (카카오/구글/애플 등)                     │
│      → snsType = "S0001" (카카오)                                  │
│      → snsKey = "abc123def456"                                     │
│      → credentialId = "cred_abc123" (IAMUI에서 발급)               │
│                                                                     │
│  [2] SCAP 토큰 조회 결과                                            │
│      → KT ID 없음 (SNS만으로 kt.com 가입)                          │
│      → credentialId 존재                                            │
│                                                                     │
│  [3] SNS ID 생성                                                    │
│      → snsId = "SNS!S0001_abc123def456"                            │
│                                                                     │
│  [4] TB_USER_KT INSERT (1 row 생성)                                 │
│      ┌──────────────────────┬──────────┬──────────────┬──────┐      │
│      │ USER_ID (PK)         │ KT_ID    │CREDENTIAL_ID │STATUS│      │
│      ├──────────────────────┼──────────┼──────────────┼──────┤      │
│      │ SNS!S0001_abc123def… │ (빈값)   │ cred_abc123  │G0004 │      │
│      └──────────────────────┴──────────┴──────────────┴──────┘      │
│                                                                     │
│      ★ USER_ID = SNS ID ("SNS!S0001_abc123def456")                 │
│      ★ KT_ID = 빈값 (KT ID 미보유)                                 │
│      ★ CREDENTIAL_ID = IAMUI 발급 credential_id                    │
│      ★ MEM_STATUS = G0004 (SNS 임시회원)                            │
│                                                                     │
│  [5] 세션 정보                                                       │
│      → userId = "SNS!S0001_abc123def456"                           │
│      → memStatus = "G0004"                                         │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 1-3. 준회원 - SNS 사용자가 차후 KT ID를 생성한 경우

```
┌─────────────────────────────────────────────────────────────────────┐
│  케이스: 기존 SNS 사용자가 kt.com에서 KT ID 가입 후 로그인          │
│  (SNS 로그인 링크를 생성한 경우)                                     │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  [1] 사용자가 kt.com에서 KT ID "honggildong" 생성                  │
│      → SNS 계정과 KT ID를 연결 (SNS 로그인 링크)                   │
│                                                                     │
│  [2] YBOX에 SNS 로그인 → IAMUI 인증                                │
│      → credentialId = "cred_abc123"                                │
│                                                                     │
│  [3] SCAP 토큰 조회                                                 │
│      → ★ 이번에는 KT ID = "honggildong" 이 조회됨!                │
│                                                                     │
│  [4] credential_id로 TB_USER_KT 조회                                │
│      SELECT * FROM TB_USER_KT                                       │
│      WHERE CREDENTIAL_ID = 'cred_abc123'                            │
│      → USER_ID = "SNS!S0001_abc123def456" (기존 SNS 레코드 발견)   │
│                                                                     │
│  [5] ★ KT_ID 컬럼만 업데이트 (USER_ID는 변경하지 않음!)            │
│      UPDATE TB_USER_KT                                              │
│      SET KT_ID = 'honggildong'                                     │
│      WHERE CREDENTIAL_ID = 'cred_abc123'                           │
│                                                                     │
│      변경 후:                                                        │
│      ┌──────────────────────┬─────────────┬──────────────┬──────┐   │
│      │ USER_ID (PK)         │ KT_ID       │CREDENTIAL_ID │STATUS│   │
│      ├──────────────────────┼─────────────┼──────────────┼──────┤   │
│      │ SNS!S0001_abc123def… │ honggildong │ cred_abc123  │G0003 │   │
│      └──────────────────────┴─────────────┴──────────────┴──────┘   │
│                                                                     │
│      ★★★ 핵심: USER_ID(PK)는 SNS ID 그대로 유지! ★★★            │
│      ★★★ KT_ID 컬럼에만 새 KT ID를 저장 ★★★                     │
│      ★ MEM_STATUS: G0004 → G0003 (준회원 KT ID 보유)               │
│                                                                     │
│  [6] 세션 정보 (변경 없음!)                                          │
│      → userId = "SNS!S0001_abc123def456" (SNS ID 유지)             │
│      → memStatus = "G0003"                                         │
│      → ktId = "honggildong" (댓글 표시용)                           │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 1-4. 정회원 - KT ID가 없는 기존 사용자의 SNS 로그인 (G0002)

```
┌─────────────────────────────────────────────────────────────────────┐
│  케이스: KT ID가 없는 정회원 사용자 → SNS로 IAMUI 로그인            │
│  (기존 사용자 중 KT ID 없는 사용자 = memStatus G0002)               │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  전제 조건:                                                          │
│  - 정회원은 이미 TB_USER에 데이터가 있다                             │
│  - 정회원 신규 사용자는 무조건 KT ID가 있다                          │
│  - KT ID가 없는 경우 YBOX 로그인 불가                                │
│  - 기존 사용자 중 KT ID 없는 사용자 = memStatus G0002               │
│                                                                     │
│  [1] G0002 사용자가 SNS로 IAMUI 로그인                              │
│      → credentialId = "cred_def456"                                │
│      → SCAP 조회 결과: KT ID 없음                                  │
│                                                                     │
│  [2] TB_USER에서 CNTR_NO로 기존 레코드 조회 (이미 존재)             │
│                                                                     │
│  [3] TB_USER UPDATE (기존 데이터에 SNS 정보 추가)                   │
│      UPDATE TB_USER SET                                             │
│          USER_ID = 'SNS!S0001_xyz789...',                          │
│          KT_ID = NULL,                  -- KT ID 없음               │
│          CREDENTIAL_ID = 'cred_def456'                             │
│      WHERE CNTR_NO = #{cntrNo}                                     │
│                                                                     │
│      변경 후:                                                        │
│      ┌─────────┬──────────────────────┬──────┬──────────────┬──────┐│
│      │ CNTR_NO │ USER_ID              │KT_ID │CREDENTIAL_ID │STATUS││
│      │ (PK)    │                      │      │              │      ││
│      ├─────────┼──────────────────────┼──────┼──────────────┼──────┤│
│      │ C002    │ SNS!S0001_xyz789...  │(빈값)│ cred_def456  │G0002 ││
│      └─────────┴──────────────────────┴──────┴──────────────┴──────┘│
│                                                                     │
│      ★ USER_ID에 SNS ID가 들어감 (기존 NULL → SNS ID)              │
│      ★ KT_ID는 빈값 유지 (KT ID 미보유)                            │
│      ★ 정회원이므로 INSERT가 아닌 UPDATE 처리                       │
│                                                                     │
│  [4] 세션 정보                                                       │
│      → cntrNo = "C002"                                             │
│      → userId = "SNS!S0001_xyz789..."                              │
│      → memStatus = "G0002"                                         │
│                                                                     │
│  [참고] 정회원은 CNTR_NO를 키값으로 조회하므로,                      │
│         공용 테이블의 이벤트 이력 등은 CNTR_NO로 접근.               │
│         SNS ID가 USER_ID에 들어가도 조회에는 영향 없음.              │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 1-5. TB_USER_KT / TB_USER 상태 예시 (전체)

```
TB_USER_KT (준회원 테이블, PK = USER_ID):
┌──────────────────────┬─────────────┬────────────────┬──────────┬──────────────────────┐
│ USER_ID (PK)         │ KT_ID (신규)│ CREDENTIAL_ID  │MEM_STATUS│ 설명                  │
│                      │             │ (신규)          │          │                      │
├──────────────────────┼─────────────┼────────────────┼──────────┼──────────────────────┤
│ SNS!S0001_abc123     │ (빈값)      │ cred_abc123    │ G0004    │ SNS가입, KT ID 없음  │
│ SNS!S0001_abc123     │ honggildong │ cred_abc123    │ G0003    │ 차후 KT ID 생성      │
│ kt_user_555          │ kt_user_555 │ cred_555       │ G0003    │ 처음부터 KT ID 가입  │
│ honggildong          │ honggildong │ cred_hong      │ G0001    │ 기존 KT ID + 회선    │
└──────────────────────┴─────────────┴────────────────┴──────────┴──────────────────────┘

★ 주목: SNS 사용자가 KT ID를 나중에 만들어도 USER_ID(PK)는 SNS ID 그대로!
         KT_ID 컬럼에만 KT ID 저장.
★ 주목: 처음부터 KT ID로 가입한 사용자는 USER_ID = KT_ID (기존과 동일)


TB_USER (정회원 테이블, PK = CNTR_NO):
┌─────────┬──────────────────────┬─────────────┬────────────────┬──────────┐
│ CNTR_NO │ USER_ID              │ KT_ID (신규)│ CREDENTIAL_ID  │MEM_STATUS│
│ (PK)    │                      │             │ (신규)          │          │
├─────────┼──────────────────────┼─────────────┼────────────────┼──────────┤
│ C001    │ honggildong          │ honggildong │ cred_hong      │ G0001    │
│ C002    │ SNS!S0001_xyz789     │ (빈값)      │ cred_def456    │ G0002    │
│ C003    │ (빈값)               │ (빈값)      │ (빈값)         │ G0002    │
└─────────┴──────────────────────┴─────────────┴────────────────┴──────────┘

★ C001: KT ID + 회선 사용자 (기존과 동일)
★ C002: KT ID 없는 정회원이 SNS 로그인한 경우 (USER_ID에 SNS ID)
★ C003: KT ID 없는 정회원이 아직 SNS 로그인 안 한 경우
```

### 1-6. memStatus 코드 정의

```
┌──────────┬───────────────────────────────────────────────────────────┐
│ memStatus│ 설명                                                      │
├──────────┼───────────────────────────────────────────────────────────┤
│ G0001    │ 정회원 + KT ID : TB_USER(CNTR_NO) + TB_USER_KT(USER_ID) │
│ G0002    │ 정회원 (KT ID 없음) : TB_USER(CNTR_NO)만 존재            │
│          │  → SNS 로그인 시 USER_ID에 SNS ID 저장                   │
│ G0003    │ 준회원 (KT ID 보유) : TB_USER_KT(USER_ID)만 존재          │
│          │  → SNS 사용자가 차후 KT ID 생성한 경우 포함               │
│ G0004    │ [신규] SNS 임시회원 : TB_USER_KT(USER_ID=SNS!XXXXX)      │
│          │  → KT ID 미보유, SNS로만 가입한 준회원                    │
└──────────┴───────────────────────────────────────────────────────────┘
```

---

## 2. 재가입

### 2-0. 기존 재가입 프로세스

```
┌─────────────────────────────────────────────────────────────────────┐
│  기존 재가입 프로세스                                                │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  [탈퇴 시]                                                           │
│  TB_SVCOUT 테이블에 사용자의 탈퇴 시 계약번호, user_id 등 저장      │
│                                                                     │
│  [재가입 시]                                                         │
│  TB_SVCOUT에서 계약번호 또는 user_id로 기존 탈퇴 정보 조회           │
│  → KT shub에서 고객 정보를 가져와 회원 테이블에 재등록               │
│    (TB_USER, TB_USER_KT에 INSERT/UPDATE)                            │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 2-1. SNS 로그인 사용자의 재가입 문제점 및 해결

```
┌─────────────────────────────────────────────────────────────────────┐
│  문제점                                                              │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  SNS 로그인 사용자가 탈퇴 시:                                       │
│  → TB_SVCOUT에 USER_ID = "SNS!S0001_abc123" 저장                  │
│                                                                     │
│  차후 재가입 시 KT ID를 가입한 경우:                                 │
│  → IAMUI 로그인 후 SCAP 토큰으로 조회하면 KT ID = "honggildong"   │
│  → TB_SVCOUT에서 USER_ID = "honggildong"으로 조회 시도             │
│  → ★ 조회 안 됨! (탈퇴 시 저장된 USER_ID는 SNS ID이므로)          │
│                                                                     │
│  ┌─────────────────────────────────────────────┐                    │
│  │ TB_SVCOUT (현재)                             │                    │
│  ├───────────┬──────────────────────┬──────────┤                    │
│  │ CNTR_NO   │ USER_ID              │ ...      │                    │
│  ├───────────┼──────────────────────┼──────────┤                    │
│  │ C001      │ SNS!S0001_abc123     │ ...      │ ← 탈퇴 시 SNS ID  │
│  └───────────┴──────────────────────┴──────────┘                    │
│                                                                     │
│  재가입 시: USER_ID = "honggildong" → 매칭 안 됨!                   │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│  해결 방안                                                           │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  [1] TB_SVCOUT 테이블에 CREDENTIAL_ID 컬럼 추가                    │
│                                                                     │
│      ALTER TABLE TB_SVCOUT ADD COLUMN CREDENTIAL_ID VARCHAR(200);   │
│      COMMENT ON COLUMN TB_SVCOUT.CREDENTIAL_ID                     │
│        IS 'IAMUI credential_id (재가입 시 매칭용)';                 │
│      CREATE INDEX IDX_SVCOUT_CREDENTIAL_ID                         │
│        ON TB_SVCOUT (CREDENTIAL_ID)                                │
│        WHERE CREDENTIAL_ID IS NOT NULL;                             │
│                                                                     │
│  [2] 탈퇴 시 CREDENTIAL_ID도 함께 저장                              │
│      INSERT INTO TB_SVCOUT (CNTR_NO, USER_ID, CREDENTIAL_ID, ...)  │
│      VALUES (#{cntrNo}, #{userId}, #{credentialId}, ...)            │
│                                                                     │
│  [3] 재가입 시 CREDENTIAL_ID로 조회                                  │
│      SELECT * FROM TB_SVCOUT                                        │
│      WHERE CREDENTIAL_ID = #{credentialId}                         │
│      → ★ SNS ID든 KT ID든 credential_id로 매칭!                    │
│                                                                     │
│      ┌─────────────────────────────────────────────────────┐        │
│      │ TB_SVCOUT (변경 후)                                  │        │
│      ├───────────┬──────────────────────┬──────────────┬───┤        │
│      │ CNTR_NO   │ USER_ID              │CREDENTIAL_ID │...│        │
│      ├───────────┼──────────────────────┼──────────────┼───┤        │
│      │ C001      │ SNS!S0001_abc123     │ cred_abc123  │...│        │
│      └───────────┴──────────────────────┴──────────────┴───┘        │
│                                                                     │
│      재가입 시:                                                       │
│      → IAMUI 로그인 → credentialId = "cred_abc123"                 │
│      → TB_SVCOUT에서 CREDENTIAL_ID = "cred_abc123" 조회            │
│      → ★ 매칭 성공! 기존 탈퇴 정보 발견                             │
│      → shub에서 고객 정보 가져와 재가입 처리                         │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 2-2. 재가입 DDL

```sql
-- ================================================================
-- [DDL-003] TB_SVCOUT 컬럼 추가
-- ================================================================
ALTER TABLE TB_SVCOUT ADD COLUMN CREDENTIAL_ID VARCHAR(200);

COMMENT ON COLUMN TB_SVCOUT.CREDENTIAL_ID IS 'IAMUI credential_id (재가입 시 매칭 키)';

CREATE INDEX IDX_SVCOUT_CREDENTIAL_ID ON TB_SVCOUT (CREDENTIAL_ID)
WHERE CREDENTIAL_ID IS NOT NULL;
```

### 2-3. 재가입 프로세스 변경

```
기존:  TB_SVCOUT에서 CNTR_NO 또는 USER_ID로 조회
변경:  TB_SVCOUT에서 CREDENTIAL_ID로 조회 (1차)
       → 없으면 기존 방식(CNTR_NO / USER_ID)으로 폴백 (2차)

★ 기존 로직과 하위 호환 유지
★ credential_id가 있으면 우선 사용
★ credential_id가 없는 기존 탈퇴 데이터는 기존 방식으로 처리
```

---

## 3. 이벤트 등 코어 기능 테이블

### 3-0. 기본 원칙

```
┌──────────────────────────────────────────────────────────────────────┐
│  SNS를 통해 kt.com 가입 및 YBOX에 가입한 사용자는                    │
│  기존에 KT ID를 통해 가입한 사용자와 동일하게 이벤트 참여가 가능하다. │
│                                                                      │
│  ★ 기존 이벤트/리워드/출석체크/좋아요/댓글 로직은 변경 최소화       │
│  ★ 기존 <choose> 패턴 (cntrNo / userId 분기)이 그대로 동작          │
└──────────────────────────────────────────────────────────────────────┘
```

### 3-1. 공용 테이블의 USER_ID = SNS ID

```
SNS 사용자가 YBOX에서 이벤트를 응모하는 경우,
응모 정보의 USER_ID 에는 SNS ID가 저장된다.

예시) TB_REWARD_INFO:
┌───────────┬──────────────────────┬──────────┬───────────────────┐
│ CNTR_NO   │ USER_ID              │ ISSUE_SEQ│ REG_DT            │
├───────────┼──────────────────────┼──────────┼───────────────────┤
│ NULL      │ SNS!S0001_abc123     │ 1001     │ 2026-01-15        │ ← SNS 사용자
│ NULL      │ SNS!S0001_abc123     │ 1005     │ 2026-02-20        │ ← SNS 사용자
│ NULL      │ honggildong          │ 1010     │ 2026-03-15        │ ← KT ID 사용자
│ C001      │ NULL                 │ 1002     │ 2026-01-20        │ ← 회선 사용자
└───────────┴──────────────────────┴──────────┴───────────────────┘

★ SNS 사용자는 USER_ID에 SNS ID("SNS!XXXXX")가 저장됨
★ 이 값은 KT ID 가입 후에도 변경하지 않음 (v4/v5 핵심 원칙)
```

### 3-2. 차후 KT ID 가입 시 KT_ID 컬럼 업데이트

```
SNS 사용자가 kt.com에서 KT ID를 가입한 경우,
credential_id를 통해 TB_USER_KT를 조회하여 KT_ID 컬럼에 KT ID를 업데이트한다.

★ USER_ID(PK)는 변경하지 않음!
★ KT_ID 컬럼에만 새 KT ID를 저장!

UPDATE TB_USER_KT
SET KT_ID = 'honggildong',
    MEM_STATUS = 'G0003'
WHERE CREDENTIAL_ID = #{credentialId}
```

### 3-3. 정회원 SNS 사용자의 이벤트 이력 조회

```
┌─────────────────────────────────────────────────────────────────────┐
│  정회원 SNS 사용자 (TB_USER 존재, CNTR_NO 보유)                     │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  정회원의 경우, SNS 사용자든 아니든 이전과 동일하다.                 │
│                                                                     │
│  → 계약번호(CNTR_NO)를 키값으로 해서                                │
│    본인이 응모한 이벤트 내역을 조회 가능                             │
│                                                                     │
│  SQL: WHERE CNTR_NO = #{cntrNo}                                     │
│                                                                     │
│  ★ 기존 <choose> 패턴이 그대로 동작:                                │
│  <choose>                                                           │
│    <when test='cntrNo != null and cntrNo != ""'>                    │
│      WHERE CNTR_NO = #{cntrNo}    ← 정회원: CNTR_NO로 조회         │
│    </when>                                                          │
│    <otherwise>                                                      │
│      WHERE USER_ID = #{userId}    ← 준회원: USER_ID로 조회         │
│    </otherwise>                                                     │
│  </choose>                                                          │
│                                                                     │
│  ★ 변경 없음! 기존 Mapper XML 수정 불필요                           │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 3-4. 준회원 SNS 사용자의 이벤트 이력 조회

```
┌─────────────────────────────────────────────────────────────────────┐
│  준회원 SNS 사용자 (TB_USER_KT만 존재, CNTR_NO 없음)                │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  user_id를 통해 응모한 이벤트 내역을 조회 가능하다.                  │
│  이때 user_id는 SNS ID이다.                                         │
│                                                                     │
│  세션: userId = "SNS!S0001_abc123", cntrNo = null                  │
│                                                                     │
│  → <choose>에서 cntrNo가 null이므로 <otherwise> 분기                │
│  → WHERE USER_ID = #{userId}                                       │
│  → WHERE USER_ID = 'SNS!S0001_abc123'                              │
│                                                                     │
│  ★ KT ID를 나중에 가입해도 USER_ID(PK)는 SNS ID 그대로             │
│  ★ 세션의 userId도 SNS ID 그대로                                    │
│  ★ 따라서 이벤트 이력 조회가 항상 정상 동작                         │
│                                                                     │
│  ★ 변경 없음! 기존 Mapper XML 수정 불필요                           │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 3-5. 댓글/좋아요의 KT ID 노출 처리 (핵심!)

```
┌─────────────────────────────────────────────────────────────────────┐
│  댓글 작성, 좋아요 등의 키값 처리                                    │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  [등록 시] 키값은 user_id = SNS ID                                  │
│                                                                     │
│    INSERT INTO TB_EVENT_REPLY                                       │
│    (EVT_SEQ, NAME, CNTR_NO, USER_ID, CONTENTS, ...)                │
│    VALUES (#{evtSeq}, #{name}, #{cntrNo},                          │
│            #{userId},  ← "SNS!S0001_abc123" (SNS ID)               │
│            #{contents}, ...)                                        │
│                                                                     │
│    INSERT INTO TB_EVENT_LIKE                                        │
│    (EVT_SEQ, CNTR_NO, USER_ID, LIKE_YN, ...)                      │
│    VALUES (#{evtSeq}, #{cntrNo},                                   │
│            #{userId},  ← "SNS!S0001_abc123" (SNS ID)               │
│            'Y', ...)                                                │
│                                                                     │
│  ★ 등록 시 USER_ID에는 항상 세션의 userId(=SNS ID)가 저장됨        │
│                                                                     │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  [조회 시] 댓글의 작성자 정보를 표시할 때 KT ID 노출                │
│                                                                     │
│  ★ 핵심: 작성 댓글에 입력자 정보를 보여줄 때는                      │
│          KT ID를 가입한 사용자의 경우 KT ID를 보여줘야 한다.        │
│                                                                     │
│  [방안] 댓글 목록 조회 시 TB_USER_KT JOIN으로 KT_ID 가져오기        │
│                                                                     │
│  AS-IS (현재 댓글 목록 조회):                                        │
│  SELECT  A.REPLY_SEQ, A.EVT_SEQ, A.CNTR_NO,                        │
│          A.USER_ID, A.NAME, A.CONTENTS, ...                         │
│  FROM TB_EVENT_REPLY A                                              │
│  WHERE A.EVT_SEQ = #{evtSeq}                                       │
│                                                                     │
│  TO-BE (KT ID 포함 조회):                                            │
│  SELECT  A.REPLY_SEQ, A.EVT_SEQ, A.CNTR_NO,                        │
│          A.USER_ID, A.NAME, A.CONTENTS,                             │
│          B.KT_ID AS DISPLAY_ID,         ← KT_ID가 있으면 표시      │
│          COALESCE(B.KT_ID, A.USER_ID)                               │
│            AS DISPLAY_NAME,             ← KT_ID 없으면 SNS ID 표시 │
│          ...                                                         │
│  FROM TB_EVENT_REPLY A                                              │
│  LEFT JOIN TB_USER_KT B ON A.USER_ID = B.USER_ID                   │
│  WHERE A.EVT_SEQ = #{evtSeq}                                       │
│                                                                     │
│  또는 정회원인 경우:                                                  │
│  LEFT JOIN TB_USER C ON A.CNTR_NO = C.CNTR_NO                      │
│  → COALESCE(C.KT_ID, B.KT_ID, A.USER_ID) AS DISPLAY_NAME          │
│                                                                     │
│  결과 예시:                                                           │
│  ┌──────────┬──────────────────────┬─────────────┬──────────────┐   │
│  │ REPLY_SEQ│ USER_ID (저장키)     │ KT_ID       │ DISPLAY_NAME │   │
│  ├──────────┼──────────────────────┼─────────────┼──────────────┤   │
│  │ 101      │ SNS!S0001_abc123     │ honggildong │ honggildong  │   │
│  │ 102      │ SNS!S0003_xyz789     │ NULL        │ SNS 사용자   │   │
│  │ 103      │ honggildong          │ honggildong │ honggildong  │   │
│  └──────────┴──────────────────────┴─────────────┴──────────────┘   │
│                                                                     │
│  ★ KT ID가 있는 SNS 사용자: KT ID 표시 (101번)                     │
│  ★ KT ID가 없는 SNS 사용자: 마스킹 처리 또는 "SNS 사용자" (102번) │
│  ★ 기존 KT ID 사용자: 기존과 동일 (103번)                          │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 3-6. 댓글 표시 이름(DISPLAY_NAME) 결정 로직

```
DISPLAY_NAME 결정 순서:

  if (정회원, CNTR_NO != null)
    → TB_USER.KT_ID 또는 TB_USER.USER_ID 사용 (기존 로직)
  
  else if (준회원, USER_ID 사용)
    → TB_USER_KT.KT_ID가 있으면 KT_ID 표시
    → TB_USER_KT.KT_ID가 없으면(SNS only) 마스킹 처리
      예: "SNS 사용자", "ka***23" (앞2뒤2만 표시) 등

SQL 패턴:
  COALESCE(
    NULLIF(B.KT_ID, ''),     -- KT_ID가 있으면 우선 사용
    A.NAME                    -- 없으면 이름 사용 (이미 저장된 NAME 컬럼)
  ) AS DISPLAY_NAME
```

---

## 4. 전체 데이터 흐름 시나리오

### 4-1. 시나리오 A: SNS 신규 가입 → 이벤트 참여 → KT ID 생성 → 재로그인

```
[Step 1] SNS 신규 가입 (KT ID 없음)
══════════════════════════════════
  TB_USER_KT INSERT:
    USER_ID = "SNS!S0001_abc123"
    KT_ID = (빈값)
    CREDENTIAL_ID = "cred_abc123"
    MEM_STATUS = "G0004"

  세션: userId = "SNS!S0001_abc123"

[Step 2] 이벤트 응모
══════════════════════════════════
  TB_REWARD_INFO INSERT:
    USER_ID = "SNS!S0001_abc123"
    ISSUE_SEQ = 1001

  TB_EVENT_REPLY INSERT (댓글):
    USER_ID = "SNS!S0001_abc123"
    NAME = "김철수"
    CONTENTS = "참여합니다!"

[Step 3] kt.com에서 KT ID "honggildong" 생성 (SNS 연결)
══════════════════════════════════
  → YBOX 재로그인 시 SCAP에서 KT ID 조회됨

  TB_USER_KT UPDATE (credential_id로 조회):
    SET KT_ID = "honggildong", MEM_STATUS = "G0003"
    WHERE CREDENTIAL_ID = "cred_abc123"

  변경 후:
    USER_ID = "SNS!S0001_abc123"  ← 변경 안 됨!
    KT_ID = "honggildong"         ← 새로 저장!

  세션: userId = "SNS!S0001_abc123" (변경 없음)

[Step 4] 이벤트 이력 조회
══════════════════════════════════
  WHERE USER_ID = 'SNS!S0001_abc123'  ← 기존과 동일!
  → 이벤트 응모 이력, 댓글, 좋아요 모두 정상 조회
  ✅ 기존 이력이 본인의 활동 정보로 유지!

[Step 5] 댓글 목록 조회 (KT ID 노출)
══════════════════════════════════
  SELECT A.*, COALESCE(B.KT_ID, A.NAME) AS DISPLAY_NAME
  FROM TB_EVENT_REPLY A
  LEFT JOIN TB_USER_KT B ON A.USER_ID = B.USER_ID
  → DISPLAY_NAME = "honggildong"  ← KT ID 표시!
```

### 4-2. 시나리오 B: G0002 정회원의 SNS 로그인

```
[Step 1] G0002 정회원이 SNS 로그인
══════════════════════════════════
  TB_USER UPDATE:
    SET USER_ID = "SNS!S0001_xyz789",
        CREDENTIAL_ID = "cred_def456"
    WHERE CNTR_NO = "C002"

  세션: cntrNo = "C002", userId = "SNS!S0001_xyz789"

[Step 2] 이벤트 응모
══════════════════════════════════
  TB_REWARD_INFO INSERT:
    CNTR_NO = "C002"   ← 정회원이므로 CNTR_NO로 INSERT
    USER_ID = NULL

  ★ 정회원은 CNTR_NO 키로 동작하므로 SNS ID 무관

[Step 3] 이벤트 이력 조회
══════════════════════════════════
  WHERE CNTR_NO = 'C002'  ← 기존과 100% 동일!
  ✅ 정회원은 변경 없음
```

---

## 5. 변경 대상 파일 목록

### 5-1. DDL (DB 변경)

| # | DDL | 테이블 | 설명 |
|---|-----|--------|------|
| 1 | ALTER TABLE | TB_USER_KT | CREDENTIAL_ID, KT_ID 컬럼 추가 |
| 2 | ALTER TABLE | TB_USER | CREDENTIAL_ID, KT_ID 컬럼 추가 |
| 3 | ALTER TABLE | TB_SVCOUT | CREDENTIAL_ID 컬럼 추가 |
| 4 | CREATE INDEX | TB_USER_KT | CREDENTIAL_ID, KT_ID 인덱스 |
| 5 | CREATE INDEX | TB_USER | CREDENTIAL_ID 인덱스 |
| 6 | CREATE INDEX | TB_SVCOUT | CREDENTIAL_ID 인덱스 |

### 5-2. Java 파일 변경

| # | 파일 | 변경 내용 |
|---|------|----------|
| 1 | `SessionData.java` | credentialId, ktId 필드 추가 |
| 2 | `UserInfo.java` | credentialId, ktId 필드 추가 |
| 3 | `SvcOut.java` | credentialId 필드 추가 |
| 4 | `UserService.java` | SNS 로그인 처리, KT ID 업데이트 로직 |
| 5 | `UserKtService.java` | SNS 사용자 등록/KT ID 바인딩 |
| 6 | `IamUiLoginController.java` | SNS 로그인 분기, credential_id 저장 |
| 7 | `EventService.java` (등) | 댓글 조회 시 DISPLAY_NAME 처리 |

### 5-3. Mapper XML 변경

| # | 파일 | 변경 내용 |
|---|------|----------|
| 1 | `userMapper.xml` | insertSvcOut에 CREDENTIAL_ID 추가, TB_USER 컬럼 추가 |
| 2 | `userKtMapper.xml` | insertUserKtInfo에 CREDENTIAL_ID/KT_ID 추가, KT_ID 업데이트 쿼리 |
| 3 | `eventMapper.xml` | getReplyList에 TB_USER_KT JOIN 추가 (DISPLAY_NAME) |

### 5-4. 변경하지 않는 파일 (핵심!)

```
★ 아래 파일들은 변경하지 않음:

  - eventMapper.xml의 이벤트 응모/조회 쿼리 (기존 <choose> 패턴 유지)
  - rewardMapper.xml (기존 <choose> 패턴 유지)
  - attendMapper.xml (기존 <choose> 패턴 유지)
  - cmsMapper.xml (기존 <choose> 패턴 유지)
  - ycanvasMapper.xml (기존 <choose> 패턴 유지)
  - 기타 공용 테이블 Mapper (38+ 곳 모두 변경 없음!)

이유:
  - USER_ID에 SNS ID가 그대로 유지되므로
  - 기존 WHERE USER_ID = #{userId} 쿼리가 정상 동작
  - userId에 SNS ID가 들어오면 SNS ID로 조회
  - Interceptor 등 별도 변환 메커니즘 불필요!
```

---

## 6. Mapper XML 변경 상세

### 6-1. userKtMapper.xml - KT_ID 업데이트 쿼리 (신규)

```xml
<!-- KT ID 생성 시 credential_id로 조회하여 KT_ID 업데이트 -->
<update id="updateKtIdByCredentialId" parameterType="Map">
    UPDATE TB_USER_KT SET
        KT_ID = #{ktId},
        MEM_STATUS = 'G0003',
        MOD_DT = NOW()
    WHERE CREDENTIAL_ID = #{credentialId}
    AND (KT_ID IS NULL OR KT_ID = '')
</update>

<!-- credential_id로 사용자 조회 -->
<select id="getUserKtInfoByCredentialId" resultMap="userInfoMap">
    SELECT A.*, ...
    FROM TB_USER_KT A
    WHERE A.CREDENTIAL_ID = #{credentialId}
    LIMIT 1
</select>

<!-- insertUserKtInfo에 CREDENTIAL_ID, KT_ID 컬럼 추가 -->
<!-- 기존 INSERT에 컬럼 추가 -->
```

### 6-2. userMapper.xml - insertSvcOut 변경

```xml
<!-- 변경: CREDENTIAL_ID 컬럼 추가 -->
<insert id="insertSvcOut" parameterType="TermsAgree">
    INSERT INTO TB_SVCOUT (
          CNTR_NO
        , SVCOUT_TP
        , SVCOUT_DESC
        , REJOIN_YN
        , REG_DT
        , MOD_DT
        , USER_ID
        , CREDENTIAL_ID       <!-- 신규 추가 -->
    ) VALUES (
          #{cntrNo}
        , #{svcoutTp}
        , #{svcoutDesc}
        , 'N'
        , NOW()
        , NOW()
        , #{userId}
        , #{credentialId}     <!-- 신규 추가 -->
    )
</insert>

<!-- 재가입 시 credential_id로 탈퇴 정보 조회 (신규) -->
<select id="getSvcOutByCredentialId" parameterType="String" resultType="SvcOut">
    SELECT * FROM TB_SVCOUT
    WHERE CREDENTIAL_ID = #{credentialId}
    ORDER BY REG_DT DESC
    LIMIT 1
</select>
```

### 6-3. eventMapper.xml - 댓글 목록 조회 변경 (DISPLAY_NAME)

```xml
<!-- AS-IS: 현재 댓글 목록 조회 -->
<select id="getReplyList" resultType="EventReply">
    SELECT  A.REPLY_SEQ, A.EVT_SEQ, A.CNTR_NO,
            A.USER_ID, A.NAME, A.CONTENTS,
            A.REG_DT, A.MOD_DT,
            ...
    FROM PLANY.TB_EVENT_REPLY A
    WHERE A.EVT_SEQ = #{evtSeq} AND A.DEL_YN = 'N'
    ...
</select>

<!-- TO-BE: KT ID 표시를 위한 JOIN 추가 -->
<select id="getReplyList" resultType="EventReply">
    SELECT  A.REPLY_SEQ, A.EVT_SEQ, A.CNTR_NO,
            A.USER_ID, A.NAME, A.CONTENTS,
            A.REG_DT, A.MOD_DT,
            COALESCE(
                NULLIF(B.KT_ID, ''),
                NULLIF(C.KT_ID, ''),
                A.NAME
            ) AS DISPLAY_ID,
            ...
    FROM PLANY.TB_EVENT_REPLY A
    LEFT JOIN TB_USER_KT B ON A.USER_ID = B.USER_ID
    LEFT JOIN TB_USER C ON A.CNTR_NO = C.CNTR_NO
    WHERE A.EVT_SEQ = #{evtSeq} AND A.DEL_YN = 'N'
    ...
</select>
```

---

## 7. 전체 아키텍처 (v5)

```
┌─────────────────────────────────────────────────────────────────────────┐
│                              Client (App)                              │
│                                                                        │
│  [Case 1] SNS 로그인 (KT ID 없음) → 준회원 G0004                      │
│    → 세션: userId = "SNS!S0001_abc123"                                 │
│    → 공용 테이블: USER_ID = "SNS!S0001_abc123"                         │
│    → 댓글 표시: NAME 컬럼 또는 마스킹 처리                             │
│                                                                        │
│  [Case 2] SNS 로그인 (KT ID 차후 생성) → 준회원 G0003                  │
│    → 세션: userId = "SNS!S0001_abc123" (변경 없음!)                    │
│    → TB_USER_KT.KT_ID = "honggildong" (업데이트됨)                    │
│    → 공용 테이블: USER_ID = "SNS!S0001_abc123" (변경 없음!)            │
│    → 댓글 표시: "honggildong" (KT ID 노출)                            │
│                                                                        │
│  [Case 3] 정회원 SNS 로그인 (KT ID 없음, G0002)                       │
│    → 세션: cntrNo = "C002", userId = "SNS!S0001_xyz789"               │
│    → 공용 테이블: CNTR_NO = "C002" (정회원 키)                        │
│    → 정회원은 기존과 100% 동일                                         │
│                                                                        │
│  [Case 4] 기존 KT ID 직접 로그인 (변경 없음)                           │
│    → 세션: userId = "honggildong"                                      │
│    → 기존과 100% 동일                                                  │
│                                                                        │
│  [Case 5] 기존 회선 로그인 (변경 없음)                                  │
│    → 세션: cntrNo = "C001"                                             │
│    → 기존과 100% 동일                                                  │
│                                                                        │
└────────────────────────────────────────────────────────────────────────┘
                                     │
                                     ▼
┌────────────────────────────────────────────────────────────────────────┐
│  YBOX Backend                                                          │
│                                                                        │
│  ★ Interceptor 불필요! (v3/v4 대비 핵심 간소화)                       │
│                                                                        │
│  [SNS 로그인 시]                                                       │
│  1) IAMUI 인증 → credentialId 획득                                    │
│  2) SCAP 토큰 조회 → KT ID 유무 확인                                  │
│  3) TB_USER_KT 조회/INSERT (credential_id 기반)                       │
│  4) KT ID 있으면: KT_ID 컬럼 업데이트                                 │
│  5) 세션 설정: userId = SNS ID (항상!)                                 │
│                                                                        │
│  [이벤트 기능]                                                         │
│  → 기존 <choose> 패턴 그대로 동작                                     │
│  → cntrNo 있으면 CNTR_NO로, 없으면 USER_ID(=SNS ID)로 조회            │
│  → 댓글 목록만 JOIN 추가 (KT ID 표시)                                 │
│                                                                        │
│  [재가입]                                                               │
│  → TB_SVCOUT에서 credential_id로 조회                                  │
│                                                                        │
└────────────────────────────────────────────────────────────────────────┘
                                     │
                                     ▼
┌────────────────────────────────────────────────────────────────────────┐
│  PostgreSQL                                                            │
│                                                                        │
│  ★ 공용 테이블 USER_ID 변경 없음!                                     │
│  ★ Mapper XML 변경: 최소 (댓글 JOIN 추가 정도)                        │
│  ★ 이벤트/리워드/출석 등 기존 쿼리 100% 유지                          │
│                                                                        │
└────────────────────────────────────────────────────────────────────────┘
```

---

## 8. v5 설계의 핵심 장점

```
┌────────────────────────────────────────────────────────────────────┐
│                                                                    │
│  v5 vs v3/v4 비교                                                  │
│                                                                    │
│  ┌──────────────────────┬────────┬────────┬──────────────────────┐ │
│  │ 항목                 │ v3     │ v4     │ v5                   │ │
│  ├──────────────────────┼────────┼────────┼──────────────────────┤ │
│  │ 공용 테이블 변경     │ UPDATE │ 변경X  │ 변경X                │ │
│  │ Interceptor          │ 필요   │ 필요   │ ★ 불필요!           │ │
│  │ Mapper XML 변경      │ 0곳    │ 38곳   │ ★ 2~3곳 (댓글만)   │ │
│  │ Java 신규 파일       │ 4개    │ 4개    │ ★ 0개 (기존만 수정) │ │
│  │ 매핑 테이블          │ -      │ 필요   │ ★ 불필요!           │ │
│  │ 캐시 서비스          │ 필요   │ 필요   │ ★ 불필요!           │ │
│  │ KT ID 바인딩 처리    │ 16+SQL │ 3 SQL  │ ★ 1 SQL (UPDATE)   │ │
│  │ 복잡도               │ 높음   │ 중간   │ ★ 낮음              │ │
│  └──────────────────────┴────────┴────────┴──────────────────────┘ │
│                                                                    │
│  ★ v5 핵심:                                                       │
│  "SNS ID를 user_id로 그대로 유지하면                               │
│   Interceptor도, 매핑 테이블도, 캐시도 필요 없다!"                 │
│                                                                    │
│  → 세션의 userId가 항상 SNS ID이므로                               │
│  → 기존 WHERE USER_ID = #{userId} 쿼리가 그대로 동작              │
│  → KT ID는 TB_USER_KT.KT_ID 컬럼에만 저장                        │
│  → KT ID 노출이 필요한 곳(댓글)에서만 JOIN으로 가져옴             │
│                                                                    │
└────────────────────────────────────────────────────────────────────┘
```

---

## 9. 주의사항 및 엣지 케이스

### 9-1. SNS 사용자가 탈퇴 후 다른 SNS로 재가입

```
[케이스] 카카오로 가입 → 탈퇴 → 구글로 재가입

  탈퇴 시: TB_SVCOUT에 CREDENTIAL_ID = "cred_abc123" 저장
  재가입 시: 구글로 로그인하면 새로운 CREDENTIAL_ID 발급
  → credential_id가 다르므로 별개 사용자로 처리됨

  ★ 같은 credential_id로 재가입해야 기존 탈퇴 정보와 매칭
  ★ IAMUI에서 동일인이면 동일 credential_id가 발급되므로
    SNS 종류가 달라도 동일인이면 재가입 매칭 가능
```

### 9-2. 한 사용자가 복수 SNS로 로그인

```
[케이스] 같은 사람이 카카오/구글 모두로 로그인 시도

  → IAMUI에서 동일인에 대해 동일 credential_id 발급
  → TB_USER_KT에서 credential_id로 조회 시 기존 레코드 발견
  → 기존 SNS ID(USER_ID)가 유지됨 (첫 번째 SNS 기준)
  → KT_ID가 있으면 세션에 KT_ID 정보 포함

  ★ 첫 번째 SNS로 만든 레코드가 기준이 됨
  ★ 두 번째 SNS 로그인 시에도 같은 레코드 사용
```

### 9-3. 정회원 신규 사용자는 무조건 KT ID가 있다

```
정회원 신규 가입자: KT ID 필수 → 이 설계의 영향 없음
정회원 기존 사용자 (G0002): KT ID 없음 → SNS 로그인 시 USER_ID에 SNS ID 저장
  → 단, 정회원은 CNTR_NO로 조회하므로 공용 테이블에는 영향 없음
```

### 9-4. SNS ID 형식

```
형식: SNS!{SNS_TYPE_CODE}_{SNS_KEY}

예시:
  카카오: SNS!S0001_abc123def456
  구글:   SNS!S0003_xyz789ghi012
  애플:   SNS!S0004_mno345pqr678

규칙:
  - 접두사 "SNS!" → 일반 KT ID와 구분
  - SNS_TYPE: S0001(카카오), S0002(라인), S0003(구글), S0004(애플)
  - 전체 길이: VARCHAR(60) 이내
```

---

## 10. 결론

```
┌────────────────────────────────────────────────────────────────────┐
│                                                                    │
│  v5 설계 핵심:                                                     │
│                                                                    │
│  "SNS ID를 user_id로 그대로 유지한다"                              │
│                                                                    │
│  이 한 줄의 원칙으로:                                              │
│                                                                    │
│  ✅ 공용 테이블 USER_ID 변경 없음                                  │
│  ✅ 이벤트 응모이력, 알림이력, 댓글, 좋아요 등 그대로 유지         │
│  ✅ Interceptor 불필요 (기존 쿼리 그대로 동작)                     │
│  ✅ 매핑 테이블 불필요                                              │
│  ✅ 캐시 서비스 불필요                                              │
│  ✅ 기존 Mapper XML 38곳 변경 불필요                                │
│  ✅ KT ID는 TB_USER_KT.KT_ID 컬럼에만 저장                        │
│  ✅ KT ID 노출이 필요한 곳(댓글)에서만 JOIN으로 표시               │
│  ✅ 재가입은 credential_id로 매칭                                   │
│                                                                    │
│  DB 변경: 3개 테이블 ALTER (컬럼 추가)                              │
│  Java 변경: 기존 파일 수정만 (신규 파일 없음)                       │
│  Mapper 변경: 2~3곳 (댓글 JOIN, insertSvcOut, KT_ID 업데이트)      │
│                                                                    │
└────────────────────────────────────────────────────────────────────┘
```
