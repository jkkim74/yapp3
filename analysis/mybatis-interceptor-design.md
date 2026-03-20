# MyBatis Interceptor를 활용한 SNS 임시 ID / KT ID 통합 설계 (v3)

## 핵심 변경사항 (v3 - SNS 임시 아이디 "SNS!XXXXX" 전체 라이프사이클 대응)

v2에서는 TB_USER(PK=CNTR_NO)와 TB_USER_KT(PK=USER_ID)의 듀얼 PK를 반영했으나,
**SNS 로그인 신규 사용자의 임시 아이디(SNS!XXXXX) 발급 -> 본인인증 -> YBOX 가입 -> 정식 KT ID 발급**
시나리오에서 **중복 레코드 생성 문제**를 해결하지 못했습니다.

이 v3 문서는 **하나의 사용자에 대해 임시 SNS 아이디와 정식 KT ID가 동시에 두 레코드로 존재하지 않도록** 하는 설계입니다.

---

## 1. AS-IS 사용자 유형과 DB 구조

### 1-1. 기존 사용자 유형 (4가지)

```
┌────────────────────────────────────────────────────────────────────────┐
│                         기존 사용자 유형                                │
├──────────────┬──────────────┬──────────────┬──────────────────────────┤
│ 유형          │ memStatus    │ TB_USER      │ TB_USER_KT              │
├──────────────┼──────────────┼──────────────┼──────────────────────────┤
│ 정회원+KT ID │ G0001        │ O (CNTR_NO)  │ O (USER_ID=KT ID)       │
│ 정회원       │ G0002        │ O (CNTR_NO)  │ X                        │
│ 준회원(KT ID)│ G0003        │ X            │ O (USER_ID=KT ID)       │
│ [신규] SNS   │ G0004(신규)  │ X            │ O (USER_ID=SNS!XXXXX)   │
└──────────────┴──────────────┴──────────────┴──────────────────────────┘
```

### 1-2. 테이블 구조 (AS-IS)

```
TB_USER (회선 사용자 테이블)
PK: CNTR_NO (계약번호)
┌───────────┬──────────────┬─────────────┬───────────────────────────┐
│ CNTR_NO   │ USER_ID      │ JOIN_STATUS │ MOBILE_NO, MEM_STATUS ... │
│ (PK)      │ (KT ID저장)  │             │                           │
├───────────┼──────────────┼─────────────┼───────────────────────────┤
│ C001      │ honggildong  │ G0001       │ 01012345678, G0001, ...   │
│ C002      │ NULL         │ G0001       │ 01098765432, G0002, ...   │
└───────────┴──────────────┴─────────────┴───────────────────────────┘

TB_USER_KT (KT ID / SNS 사용자 테이블)
PK: USER_ID
┌──────────────────┬─────────────┬───────────────────────────┐
│ USER_ID          │ JOIN_STATUS │ MOBILE_NO, NAME, KT_YN ...│
│ (PK)             │             │                           │
├──────────────────┼─────────────┼───────────────────────────┤
│ honggildong      │ G0001       │ 01012345678, 홍길동, Y    │  ← 정식 KT ID
│ SNS!S0001_abc123 │ G0001       │ 01055551234, 김철수, N    │  ← SNS 임시 ID
└──────────────────┴─────────────┴───────────────────────────┘

공용 테이블 (TB_REWARD_INFO, TB_CLASS_JOIN, TB_ATTEND_DAY_CHECK 등)
┌───────────┬──────────────────┬────────────────────────────┐
│ CNTR_NO   │ USER_ID          │ 기타 컬럼들...              │
│ (회선용)   │ (KT ID/SNS ID)  │                            │
├───────────┼──────────────────┼────────────────────────────┤
│ C001      │ NULL             │ ← 회선 사용자              │
│ NULL      │ honggildong      │ ← KT ID 사용자            │
│ NULL      │ SNS!S0001_abc123 │ ← SNS 임시 ID 사용자      │
└───────────┴──────────────────┴────────────────────────────┘
```

### 1-3. 핵심 문제점

```
★ 문제 시나리오:

[1] SNS 신규 사용자 가입
    → TB_USER_KT INSERT: USER_ID = "SNS!S0001_abc123"
    → TB_REWARD_INFO INSERT: USER_ID = "SNS!S0001_abc123"

[2] 이후 정식 KT ID "honggildong" 발급 (IAMUI 인증)
    → TB_USER_KT INSERT: USER_ID = "honggildong"  ★ 별도 레코드 생성!

[3] 결과: 동일인에 대해 TB_USER_KT에 2개 레코드 존재
    ┌──────────────────┬──────────┐
    │ SNS!S0001_abc123 │ 임시 ID  │ ← 이 레코드와 연결된 이벤트 이력 분리됨
    │ honggildong      │ 정식 ID  │ ← 새 레코드, 이력 없음
    └──────────────────┴──────────┘

    → 사용자 입장: "이전에 받은 혜택이 사라졌다!"
    → 시스템 입장: 동일인의 데이터가 2곳에 분산
```

---

## 2. TO-BE 설계: SNS 임시 ID -> KT ID 통합 전략

### 2-1. 핵심 원칙

```
┌──────────────────────────────────────────────────────────────────────┐
│  ★ 핵심 원칙: "하나의 사용자 = 하나의 TB_USER_KT 레코드"           │
│                                                                      │
│  1) SNS 로그인 시 TB_USER_KT.USER_ID = "SNS!XXXXX" 로 INSERT        │
│  2) 정식 KT ID 발급 시 기존 레코드의 USER_ID를 KT ID로 UPDATE       │
│     → 새 레코드를 INSERT하지 않음!                                   │
│  3) CREDENTIAL_ID로 SNS 임시 ID와 KT ID를 영구적으로 연결           │
│  4) 공용 테이블의 USER_ID도 일괄 변경 (SNS!XXXXX -> KT ID)          │
│  5) Interceptor가 변환 과도기의 안전망 역할                          │
└──────────────────────────────────────────────────────────────────────┘
```

### 2-2. DB 스키마 변경

```sql
-- ================================================================
-- [DDL-001] TB_USER_KT 컬럼 추가
-- ================================================================
ALTER TABLE TB_USER_KT ADD COLUMN CREDENTIAL_ID VARCHAR(200);
ALTER TABLE TB_USER_KT ADD COLUMN SNS_TEMP_ID   VARCHAR(60);

COMMENT ON COLUMN TB_USER_KT.CREDENTIAL_ID IS 'IAMUI credential_id (영구 사용자 식별자)';
COMMENT ON COLUMN TB_USER_KT.SNS_TEMP_ID   IS '최초 SNS 가입 시 사용한 임시 ID (이력 보존)';

-- CREDENTIAL_ID 유니크 인덱스 (NULL 허용, 값이 있으면 유니크)
CREATE UNIQUE INDEX IDX_USER_KT_CREDENTIAL_ID 
ON TB_USER_KT (CREDENTIAL_ID) 
WHERE CREDENTIAL_ID IS NOT NULL;

-- SNS_TEMP_ID 인덱스 (SNS 임시 ID로 역조회용)
CREATE INDEX IDX_USER_KT_SNS_TEMP_ID 
ON TB_USER_KT (SNS_TEMP_ID) 
WHERE SNS_TEMP_ID IS NOT NULL;


-- ================================================================
-- [DDL-002] TB_USER 컬럼 추가 (회선 사용자가 KT ID도 가진 경우)
-- ================================================================
ALTER TABLE TB_USER ADD COLUMN KT_ID VARCHAR(100);
ALTER TABLE TB_USER ADD COLUMN CREDENTIAL_ID VARCHAR(200);

CREATE INDEX idx_user_kt_id ON TB_USER(KT_ID);
CREATE INDEX idx_user_credential_id ON TB_USER(CREDENTIAL_ID);
```

### 2-3. TO-BE 테이블 상태 예시

```
TB_USER_KT (변경 후):
┌──────────────────┬────────────────┬──────────────────┬───────────┬─────────────────┐
│ USER_ID          │ CREDENTIAL_ID  │ SNS_TEMP_ID      │MEM_STATUS │ 상태 설명        │
│ (PK)             │ (신규)          │ (신규)            │           │                 │
├──────────────────┼────────────────┼──────────────────┼───────────┼─────────────────┤
│ SNS!S0001_abc123 │ NULL           │ NULL             │ G0004     │ SNS 신규가입     │
│ honggildong      │ cred_abc123    │ SNS!S0001_abc123 │ G0003     │ KT ID 전환 완료  │
│ kt_user_555      │ cred_def456    │ NULL             │ G0003     │ 처음부터 KT ID   │
└──────────────────┴────────────────┴──────────────────┴───────────┴─────────────────┘

★ 주목: "SNS!S0001_abc123" 레코드는 없어지고, 
         "honggildong" 레코드 하나만 존재. 
         SNS_TEMP_ID 컬럼에 이전 임시 ID 이력 보존.


TB_USER (변경 후):
┌───────────┬──────────────┬──────────────┬────────────────┬───────────┐
│ CNTR_NO   │ USER_ID      │ KT_ID(신규)  │ CREDENTIAL_ID  │ 상태 설명  │
│ (PK)      │              │              │ (신규)          │           │
├───────────┼──────────────┼──────────────┼────────────────┼───────────┤
│ C001      │ honggildong  │ honggildong  │ cred_abc123    │ 회선+KT ID│
│ C002      │ NULL         │ NULL         │ NULL           │ 회선만    │
└───────────┴──────────────┴──────────────┴────────────────┴───────────┘
```

### 2-4. 사용자 유형별 memStatus 정의

```
┌──────────┬──────────────────────────────────────────────────────────┐
│ memStatus│ 설명                                                     │
├──────────┼──────────────────────────────────────────────────────────┤
│ G0001    │ 정회원 + KT ID : TB_USER(CNTR_NO) + TB_USER_KT(KT ID)  │
│ G0002    │ 정회원 (KT ID 없음) : TB_USER(CNTR_NO)만 존재           │
│ G0003    │ 준회원 (KT ID) : TB_USER_KT(USER_ID=KT ID)만 존재       │
│ G0004    │ [신규] SNS 임시회원 : TB_USER_KT(USER_ID=SNS!XXXXX)     │
│          │         → 추후 KT ID 전환 시 G0003 또는 G0001로 변경     │
└──────────┴──────────────────────────────────────────────────────────┘
```

---

## 3. SNS 임시 ID 전체 라이프사이클

### 3-1. 시나리오 1: SNS 신규 가입 (KT ID 미보유)

```
┌─────────────────────────────────────────────────────────────────────┐
│  시나리오 1: SNS 로그인 → 본인인증 → YBOX 가입 (KT ID 없음)         │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  [1] 사용자가 카카오 로그인                                          │
│      → SNS 제공자로부터 snsType="S0001", snsKey="abc123def456"     │
│      → 임시 ID 생성: "SNS!S0001_abc123def456"                      │
│                                                                     │
│  [2] 본인인증 (KMC 등)                                              │
│      → 이름, 생년월일, 성별, 휴대폰번호 확인                        │
│                                                                     │
│  [3] TB_USER_KT INSERT (YBOX 가입 처리)                             │
│      ┌──────────────────────┬──────────┬────┬──────┬───┐            │
│      │ USER_ID(PK)          │MEM_STATUS│NAME│MOBILE│...│            │
│      ├──────────────────────┼──────────┼────┼──────┼───┤            │
│      │ SNS!S0001_abc123def… │ G0004    │김철│010…  │...│            │
│      └──────────────────────┴──────────┴────┴──────┴───┘            │
│      ★ CREDENTIAL_ID = NULL (IAMUI 미인증)                          │
│      ★ SNS_TEMP_ID = NULL (아직 전환 전이므로 불필요)                │
│                                                                     │
│  [4] 세션: userId = "SNS!S0001_abc123def456", memStatus = "G0004"   │
│                                                                     │
│  [5] YBOX 기능 사용 (이벤트 참여, 리워드 등)                         │
│      → 공용 테이블 INSERT: USER_ID = "SNS!S0001_abc123def456"       │
│                                                                     │
│  [6] Interceptor 동작:                                               │
│      → resolveToKtId("SNS!S0001_abc123def456")                      │
│      → KT ID 매핑 없음 → 원본 유지                                  │
│      ✅ 정상 동작                                                    │
└─────────────────────────────────────────────────────────────────────┘
```

### 3-2. 시나리오 2: SNS 임시 사용자가 KT ID 발급 (핵심!)

```
┌─────────────────────────────────────────────────────────────────────┐
│  시나리오 2: 기존 SNS 사용자가 IAMUI 인증으로 KT ID 획득             │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  [1] IAMUI 인증 완료                                                 │
│      → credentialId = "cred_abc123"                                 │
│      → ktId = "honggildong"                                        │
│      → oldSnsId = "SNS!S0001_abc123def456"                         │
│                                                                     │
│  [2] KtIdMigrationService.migrateToKtId() 호출                      │
│                                                                     │
│  [3] ★ TB_USER_KT UPDATE (레코드 생성이 아닌 기존 레코드 변경!)     │
│                                                                     │
│      변경 전:                                                        │
│      ┌──────────────────────┬──────┬─────┬──────┬──────┐            │
│      │ USER_ID(PK)          │STATUS│CRED │SNS_TM│ NAME │            │
│      ├──────────────────────┼──────┼─────┼──────┼──────┤            │
│      │ SNS!S0001_abc123def… │G0004 │NULL │NULL  │ 김철 │            │
│      └──────────────────────┴──────┴─────┴──────┴──────┘            │
│                                                                     │
│      실행 SQL:                                                       │
│      -- Step 1: 기존 레코드 삭제 (PK 변경이므로 DELETE + INSERT)     │
│      DELETE FROM TB_USER_KT WHERE USER_ID = 'SNS!S0001_abc123def…'; │
│                                                                     │
│      -- Step 2: 새 PK(KT ID)로 INSERT (기존 데이터 유지)            │
│      INSERT INTO TB_USER_KT (USER_ID, CREDENTIAL_ID, SNS_TEMP_ID,  │
│                               MEM_STATUS, NAME, MOBILE_NO, ...)     │
│      VALUES ('honggildong', 'cred_abc123',                          │
│              'SNS!S0001_abc123def456', 'G0003', '김철', '010…', …); │
│                                                                     │
│      변경 후:                                                        │
│      ┌──────────────┬──────┬────────────┬──────────────────────┬────┐│
│      │ USER_ID(PK)  │STATUS│CREDENTIAL  │SNS_TEMP_ID           │NAME││
│      ├──────────────┼──────┼────────────┼──────────────────────┼────┤│
│      │ honggildong  │G0003 │cred_abc123 │SNS!S0001_abc123def…  │김철││
│      └──────────────┴──────┴────────────┴──────────────────────┴────┘│
│                                                                     │
│      ★ 레코드 1개! 중복 없음!                                       │
│      ★ SNS_TEMP_ID에 이전 임시 ID 이력 보존                         │
│                                                                     │
│  [4] 공용 테이블 USER_ID 일괄 변경                                   │
│      UPDATE TB_REWARD_INFO SET USER_ID = 'honggildong'              │
│      WHERE USER_ID = 'SNS!S0001_abc123def456'                       │
│      ... (TB_CLASS_JOIN, TB_ATTEND_DAY_CHECK 등 모든 공용 테이블)    │
│                                                                     │
│  [5] TB_USER_KT 종속 테이블 처리                                     │
│      -- TB_TERMS_AGREE_KT: USER_ID 변경                             │
│      UPDATE TB_TERMS_AGREE_KT                                       │
│      SET USER_ID = 'honggildong'                                    │
│      WHERE USER_ID = 'SNS!S0001_abc123def456'                       │
│                                                                     │
│      -- TB_SLEEP_USER_KT: USER_ID 변경                              │
│      UPDATE TB_SLEEP_USER_KT                                        │
│      SET USER_ID = 'honggildong'                                    │
│      WHERE USER_ID = 'SNS!S0001_abc123def456'                       │
│                                                                     │
│      -- TB_UUID_KT: USER_ID 변경                                    │
│      UPDATE TB_UUID_KT                                              │
│      SET USER_ID = 'honggildong'                                    │
│      WHERE USER_ID = 'SNS!S0001_abc123def456'                       │
│                                                                     │
│      -- TB_PREFERENCE_INFO_KT: KT_ID 변경                           │
│      UPDATE TB_PREFERENCE_INFO_KT                                   │
│      SET KT_ID = 'honggildong'                                      │
│      WHERE KT_ID = 'SNS!S0001_abc123def456'                         │
│                                                                     │
│  [6] 캐시 갱신                                                       │
│      cache.put("SNS!S0001_abc123def456", "honggildong")             │
│      cache.put("honggildong", "honggildong") // self                │
│                                                                     │
│  ✅ 결과: TB_USER_KT에 레코드 1개만 존재. 중복 없음!                │
└─────────────────────────────────────────────────────────────────────┘
```

### 3-3. 시나리오 3: SNS 재로그인 (KT ID 전환 완료 후)

```
┌─────────────────────────────────────────────────────────────────────┐
│  시나리오 3: KT ID 전환 완료 후 다시 카카오로 로그인                  │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  [1] 카카오 로그인 → snsKey = "abc123def456"                        │
│      → 예전 임시 ID: "SNS!S0001_abc123def456"                       │
│                                                                     │
│  [2] 로그인 처리: SNS_TEMP_ID로 TB_USER_KT 조회                     │
│      SELECT USER_ID FROM TB_USER_KT                                 │
│      WHERE SNS_TEMP_ID = 'SNS!S0001_abc123def456'                   │
│      → 결과: "honggildong" 발견                                     │
│      → 세션에 userId = "honggildong" 저장                           │
│                                                                     │
│  [3] 만약 세션에 임시 ID가 남아있어도:                                │
│      Interceptor 동작:                                               │
│      → resolveToKtId("SNS!S0001_abc123def456")                      │
│      → 캐시 히트: "honggildong"                                     │
│      → 파라미터 변환: userId = "honggildong"                         │
│      ✅ KT ID로 정확히 조회됨                                       │
└─────────────────────────────────────────────────────────────────────┘
```

### 3-4. 시나리오 4: 기존 회선 사용자 (변경 없음)

```
[1] 회선 로그인 → CNTR_NO = "C001"
[2] Interceptor: cntrNo 존재 → 변환 건너뜀
[3] WHERE CNTR_NO = 'C001'
✅ 기존 로직 100% 유지
```

### 3-5. 시나리오 5: 기존 KT ID 로그인 (IAMUI 통해 직접)

```
[1] IAMUI 로그인 → userId = "honggildong", credentialId = "cred_abc123"
[2] TB_USER_KT에서 USER_ID = "honggildong" 조회 → 존재
[3] 세션: userId = "honggildong", memStatus = "G0003"
[4] 모든 쿼리: WHERE USER_ID = 'honggildong'
✅ Interceptor 변환 불필요
```

### 3-6. 시나리오 6: SNS 사용자가 회선도 연동 (정회원 승격)

```
┌─────────────────────────────────────────────────────────────────────┐
│  시나리오 6: KT ID 전환 후 회선 연동 → G0001 정회원                  │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  [1] TB_USER_KT: USER_ID = "honggildong", MEM_STATUS = "G0003"     │
│                                                                     │
│  [2] 회선 연동: CNTR_NO = "C001"                                    │
│      → TB_USER INSERT/UPDATE: CNTR_NO=C001, USER_ID=honggildong    │
│      → TB_USER_KT UPDATE: MEM_STATUS = "G0001"                     │
│                                                                     │
│  [3] 결과:                                                           │
│      TB_USER:     CNTR_NO=C001, USER_ID=honggildong, KT_ID=honggildong│
│      TB_USER_KT:  USER_ID=honggildong, MEM_STATUS=G0001            │
│      ✅ 정회원 승격 완료                                             │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 4. Interceptor 설계 (v3)

### 4-1. 변환 대상 판단 로직

```
MyBatis 쿼리 실행
     │
     ▼
[1] 재진입 체크 (ThreadLocal RESOLVING)
     │ TRUE  → 패스 (순환 방지)
     │ FALSE → 계속
     │
     ▼
[2] Namespace 체크
     │ SKIP 대상 (user, userkt, idmapping, common) → 패스
     │ 변환 대상 → 계속
     │
     ▼
[3] 파라미터 분석
     │
     ├─ cntrNo 존재 → 패스 (회선 사용자, 기존 로직)
     │
     ├─ userId가 "SNS!"로 시작 → resolveToKtId()
     │   ├─ KT ID 발견 → userId 치환
     │   └─ KT ID 없음 → 원본 유지 (아직 SNS만 있는 사용자)
     │
     └─ userId가 일반값 → resolveToKtId()
         ├─ 이미 KT ID → 변환 불필요 (self-mapping)
         └─ 다른 ID → 치환
```

### 4-2. ID 매핑 조회 전략 (3단계)

```
resolveToKtId(userId) 호출:

  1단계: 캐시 조회
         → 히트: 즉시 반환
         → "NONE": 매핑 없음, null 반환 (원본 유지)
         → 미스: 다음 단계

  2단계: TB_USER_KT에서 SNS_TEMP_ID로 조회
         SELECT USER_ID FROM TB_USER_KT
         WHERE SNS_TEMP_ID = #{userId}
         AND JOIN_STATUS = 'G0001'
         → 발견: KT ID 반환

  3단계: TB_USER에서 CREDENTIAL_ID 기반 조회 (fallback)
         SELECT KT_ID FROM TB_USER
         WHERE CREDENTIAL_ID = (
             SELECT CREDENTIAL_ID FROM TB_USER_KT
             WHERE USER_ID = #{userId} OR SNS_TEMP_ID = #{userId}
             LIMIT 1
         )
         AND KT_ID IS NOT NULL AND KT_ID != ''
         → 발견: KT ID 반환
         → 없음: null (원본 유지)

  4단계: 캐시 저장 후 반환
```

### 4-3. Interceptor 제외 Namespace

| Namespace | 제외 이유 |
|-----------|----------|
| `mybatis.mapper.user` | TB_USER 직접 접근 (PK=CNTR_NO). 변환 대상 아님 |
| `mybatis.mapper.userkt` | TB_USER_KT 직접 접근 (PK=USER_ID). 가입/수정 시 원본 ID 필요 |
| `mybatis.mapper.idmapping` | Interceptor 내부 매핑 조회 쿼리. 순환 참조 방지 |
| `mybatis.mapper.common` | 인증/코드 등 비사용자 쿼리 |

### 4-4. 변환 대상 Namespace

| Namespace | 파일 | choose블록 | 설명 |
|-----------|------|:---:|------|
| `mybatis.mapper.event` | eventMapper.xml | 21 | 이벤트 |
| `mybatis.mapper.cms` | cmsMapper.xml | 7 | 푸시/알림 |
| `mybatis.mapper.reward` | rewardMapper.xml | 6 | 리워드 |
| `mybatis.mapper.ycanvas` | ycanvasMapper.xml | 3 | 수강 |
| `mybatis.mapper.yfriends` | yfriendsMapper.xml | 1 | 친구 초대 |
| `mybatis.mapper.ticket` | ticketMapper.xml | (if) | 응모권 |
| `mybatis.mapper.vote` | voteMapper.xml | - | 투표 |
| `mybatis.mapper.attend` | attendMapper.xml | - | 출석 |
| | | **총 38+** | **기존 Mapper 수정 ZERO** |

---

## 5. KT ID 전환 마이그레이션 프로세스 상세

### 5-1. 전체 흐름 (KtIdMigrationService.migrateToKtId)

```
migrateToKtId(credentialId, newKtId, oldSnsId)
     │
     ▼
[1] 기존 TB_USER_KT 레코드 복사 + PK 변경
    │
    │  ★ PK(USER_ID)는 UPDATE로 변경 불가 → DELETE + INSERT 전략
    │
    │  (a) 기존 레코드 조회: SELECT * FROM TB_USER_KT WHERE USER_ID = #{oldSnsId}
    │  (b) 기존 레코드 삭제: DELETE FROM TB_USER_KT WHERE USER_ID = #{oldSnsId}
    │  (c) 새 PK로 INSERT:  INSERT INTO TB_USER_KT (USER_ID=newKtId, CREDENTIAL_ID, 
    │                        SNS_TEMP_ID=oldSnsId, ...)
    │                        VALUES (기존 데이터 복사)
    │
    │  또는 더 안전한 방식:
    │  (a) 새 KT ID 레코드가 이미 존재하는지 확인
    │  (b) 존재하지 않으면: 기존 데이터를 새 PK로 이동 (DELETE + INSERT)
    │  (c) 존재하면: 기존 SNS 레코드만 삭제 (데이터 병합)
    │
     ▼
[2] TB_USER_KT 종속 테이블 USER_ID 변경
    │  TB_TERMS_AGREE_KT     : USER_ID = oldSnsId -> newKtId
    │  TB_SLEEP_USER_KT      : USER_ID = oldSnsId -> newKtId
    │  TB_UUID_KT            : USER_ID = oldSnsId -> newKtId
    │  TB_PREFERENCE_INFO_KT : KT_ID   = oldSnsId -> newKtId
    │
     ▼
[3] 공용 테이블 USER_ID 변경
    │  TB_REWARD_INFO, TB_ATTEND_DAY_CHECK, TB_VOTE_HISTORY,
    │  TB_EVENT_LIKE, TB_EVENT_REPLY, TB_TICKET_REWARD_INFO,
    │  TB_CLASS_JOIN, TB_THREAD_PUSH_SEND, TB_TICKET_GIFT_REWARD_INFO
    │  → UPDATE SET USER_ID = newKtId WHERE USER_ID = oldSnsId
    │
     ▼
[4] TB_ENTRY_INFO 특수 처리
    │  UPDATE SET RECV_USER_ID = newKtId WHERE RECV_USER_ID = oldSnsId
    │
     ▼
[5] TB_USER KT_ID 갱신 (회선 연동 시)
    │  UPDATE TB_USER SET KT_ID = newKtId WHERE CREDENTIAL_ID = credentialId
    │
     ▼
[6] 캐시 갱신
    │  cache.put(oldSnsId, newKtId)
    │  cache.put(newKtId, newKtId)  // self-mapping
    │
     ▼
✅ 완료
```

### 5-2. PK 변경 전략 비교

```
방안 A: DELETE + INSERT (채택)
  장점: PK 변경에 가장 안전, 표준 SQL 패턴
  단점: 트랜잭션 내에서 수행 필요
  구현: 기존 레코드 조회 → 삭제 → 새 PK로 삽입

방안 B: 임시 테이블 사용
  장점: 복잡한 데이터 무결성 보장
  단점: 구현 복잡, 성능 오버헤드
  → 불채택

방안 C: USER_ID를 PK에서 제외하고 별도 ID 컬럼 추가
  장점: PK 변경 이슈 근본 해결
  단점: 기존 스키마 대폭 변경 필요, 80+ Mapper 수정
  → 불채택 (현실적이지 않음)
```

---

## 6. 전체 아키텍처 (v3)

```
┌─────────────────────────────────────────────────────────────────────────┐
│                              Client (App)                              │
│                                                                        │
│   [Case 1] SNS 로그인 (KT ID 없음)                                    │
│     → YSID: |mobile|SNS!S0001_abc123|...|G0004                         │
│                                                                        │
│   [Case 2] IAMUI KT ID 로그인                                          │
│     → YSID: |mobile|honggildong|...|G0003                              │
│                                                                        │
│   [Case 3] 회선 로그인                                                  │
│     → YSID: cntrNo|mobile|userId|...|G0002                             │
│                                                                        │
│   [Case 4] SNS 재로그인 (KT ID 전환 완료 후)                            │
│     → SNS_TEMP_ID 조회 → KT ID 발견                                   │
│     → YSID: |mobile|honggildong|...|G0003                              │
└────────────────────────────────────┬────────────────────────────────────┘
                                     │
                                     ▼
╔═══════════════════════════════════════════════════════════════════════╗
║  ★ UserIdResolvingInterceptor (MyBatis Plugin) ★                    ║
║                                                                      ║
║  [대상] 공용 Mapper: event, reward, ycanvas, cms, yfriends 등        ║
║  [제외] user, userkt, idmapping, common                              ║
║                                                                      ║
║  if (cntrNo != null) → 패스                                          ║
║  if (userId starts with "SNS!") → resolveToKtId()                   ║
║  if (userId is normal) → resolveToKtId() (self-mapping 확인)         ║
║                                                                      ║
║  3단계 조회: 캐시 → TB_USER_KT.SNS_TEMP_ID → TB_USER.KT_ID         ║
╚═══════════════════════════════════════════════════════════════════════╝
                                     │
                                     ▼
┌────────────────────────────────────────────────────────────────────────┐
│  MyBatis Executor → PreparedStatement → PostgreSQL                    │
│                                                                        │
│  기존 Mapper.xml 수정 ZERO:                                           │
│  WHERE USER_ID = #{userId}  ← Interceptor가 이미 KT ID로 치환       │
└────────────────────────────────────────────────────────────────────────┘
```

---

## 7. 구현 파일 목록 및 역할 (v3)

| # | 파일 | 역할 | v3 변경 내역 |
|---|------|------|-------------|
| 1 | `UserIdResolvingInterceptor.java` | MyBatis Plugin | SNS! 접두사 감지 로직 추가 |
| 2 | `MyBatisInterceptorConfig.java` | Interceptor Spring 등록 | v3 로그 메시지 |
| 3 | `IdMappingCacheService.java` | 캐시 + DB 조회 | 3단계 조회(SNS_TEMP_ID 추가) |
| 4 | `KtIdMigrationService.java` | KT ID 전환 마이그레이션 | PK변경(DELETE+INSERT), KT종속테이블 처리 |
| 5 | `idMappingMapper.xml` | 매핑 전용 SQL | SNS_TEMP_ID 조회쿼리, PK변경 쿼리 추가 |
| 6 | `mybatis-interceptor-design.md` | 이 설계 문서 | v3 전면 재작성 |

---

## 8. 성능 분석

| 케이스 | 오버헤드 | 설명 |
|--------|---------|------|
| cntrNo 있음 (회선) | ~ 0ms | null 체크 후 즉시 패스 |
| SKIP namespace | ~ 0ms | startsWith 체크 후 즉시 패스 |
| 캐시 히트 | < 0.01ms | ConcurrentHashMap.get() |
| 캐시 미스 - SNS_TEMP_ID 조회 | ~1ms | 인덱스 사용 |
| 캐시 미스 - CREDENTIAL_ID 폴백 | ~2ms | 서브쿼리 + 인덱스 |
| KT ID 전환 마이그레이션 | ~50-100ms | 트랜잭션 내 일괄 처리 (1회성) |

### 캐시 전략

- **매핑 있음**: 1시간 TTL
- **매핑 없음**: 10분 TTL (새 KT ID 생성을 빠르게 감지)
- **KT ID 전환 시**: 즉시 캐시 갱신 (0ms 지연)
- **Redis 교체**: 다중 서버 시 Redis로 교체 가능

---

## 9. 주의사항 및 엣지 케이스

### 9-1. PK 변경 트랜잭션 안전성

```
KT ID 전환 시 DELETE + INSERT는 반드시 @Transactional 내에서 수행.
중간에 실패하면 전체 롤백.

실패 시나리오:
  DELETE 성공 → INSERT 실패 → 롤백 → 기존 SNS 레코드 복원
  ✅ 데이터 손실 없음
```

### 9-2. 동시 로그인 방지

```
SNS 로그인과 KT ID 전환이 동시에 일어나는 경우:
  → KtIdMigrationService에서 캐시를 즉시 갱신
  → Interceptor가 캐시를 참조하므로 전환 즉시 반영
  → 일시적으로 SNS ID로 조회해도 결과 없음 → 캐시 NONE (10분)
  → 10분 후 자동 재조회 시 KT ID 발견
```

### 9-3. 이미 KT ID가 있는 상태에서 SNS 로그인

```
TB_USER_KT에 KT ID 레코드가 이미 존재하는 상태에서
같은 사용자가 SNS 로그인 시도:
  → SNS_TEMP_ID 또는 CREDENTIAL_ID로 기존 KT ID 발견
  → 새 임시 레코드 생성하지 않음
  → 기존 KT ID로 세션 설정
```

### 9-4. SNS 임시 ID 형식 검증

```java
// SNS 임시 ID 식별 패턴
public static boolean isSnsTemporaryId(String userId) {
    return userId != null && userId.startsWith("SNS!");
}
```

---

## 10. 결론

```
┌────────────────────────────────────────────────────────────────────┐
│                                                                    │
│  v3 핵심 해결:                                                     │
│                                                                    │
│  ❌ (기존 문제) SNS 임시 ID와 KT ID가 별도 레코드로 중복 생성      │
│                                                                    │
│  ✅ (해결) KT ID 전환 시 기존 레코드의 PK를 변경 (DELETE+INSERT)   │
│           → 하나의 사용자 = 하나의 TB_USER_KT 레코드               │
│           → SNS_TEMP_ID 컬럼에 이전 임시 ID 이력 보존              │
│                                                                    │
│  ✅ 기존 Mapper.xml 80+곳 수정 없이 자동 변환                     │
│  ✅ TB_USER (PK=CNTR_NO) 기존 로직 100% 유지                      │
│  ✅ TB_USER_KT 종속 테이블 (약관, UUID 등) 일괄 처리              │
│  ✅ 공용 테이블 USER_ID 일괄 변경                                  │
│  ✅ 캐시 기반 성능 < 0.01ms                                        │
│                                                                    │
│  구현 파일: Java 4개 + XML 1개 + 설계 문서 1개                      │
│  기존 코드 수정: 0개                                                │
│                                                                    │
└────────────────────────────────────────────────────────────────────┘
```
