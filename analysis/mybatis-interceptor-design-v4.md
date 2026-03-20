# MyBatis Interceptor를 활용한 SNS ID / KT ID 통합 설계 (v4)

## v4 핵심 변경: "공용 테이블 USER_ID 미변경" + "매핑 기반 조회"

```
┌──────────────────────────────────────────────────────────────────────┐
│                                                                      │
│  ★ v3 → v4 패러다임 전환 (사용자 요건 반영)                          │
│                                                                      │
│  [v3] 공용 테이블의 USER_ID를 KT ID로 일괄 UPDATE                   │
│       → 데이터 변경 리스크, 마이그레이션 부하, 히스토리 단절          │
│                                                                      │
│  [v4] 공용 테이블의 USER_ID는 절대 변경하지 않는다                   │
│       → snsId와 ktId는 매핑만 한다 (매핑 키 = credential_id)        │
│       → 조회 시 Interceptor가 본인의 모든 ID를 IN 조건으로 확장      │
│       → snsId로 쌓은 이력이 ktId 로그인 시에도 그대로 조회됨         │
│                                                                      │
└──────────────────────────────────────────────────────────────────────┘
```

### v3 대비 v4 주요 변경 요약

| 항목 | v3 (이전 설계) | v4 (현재 설계) |
|------|---------------|---------------|
| 공용 테이블 USER_ID | KT ID 발급 시 일괄 UPDATE | **절대 변경 안 함** |
| 데이터 무결성 | UPDATE 실패 시 데이터 불일치 위험 | **원본 데이터 100% 보존** |
| 매핑 방식 | 캐시에서 1:1 ID 치환 | **credential_id 기반 다중 ID 매핑** |
| Interceptor 역할 | userId 파라미터를 KT ID로 치환 | **userId를 본인의 모든 ID로 확장 (IN 조건)** |
| 이력 연속성 | 마이그레이션 후 연속 | **마이그레이션 없이 자연스럽게 연속** |
| TB_USER_KT PK 변경 | DELETE + INSERT | **PK 변경 없음 (SNS ID 레코드 유지)** |
| 마이그레이션 서비스 | 대규모 UPDATE 필요 | **매핑 테이블 INSERT 1건만** |

---

## 1. 요건 정의 (v4 핵심 3가지)

```
┌──────────────────────────────────────────────────────────────────────┐
│                                                                      │
│  요건 1) snsId로 등록한 공용 테이블의 userId 정보는                  │
│          새로 발급된 KT ID로 업데이트 하지 않는다.                    │
│                                                                      │
│  요건 2) snsId와 kt id는 서로 매핑 처리만 한다.                      │
│          매핑 키는 credential_id로 한다.                              │
│                                                                      │
│  요건 3) snsId로 응모한 이벤트 응모이력, 알림 이력, 댓글,            │
│          좋아요 등이 새로 등록한 kt id로 로그인 시에도               │
│          본인의 활동 정보로 남아 있어야 한다.                          │
│                                                                      │
└──────────────────────────────────────────────────────────────────────┘
```

---

## 2. AS-IS 사용자 유형과 DB 구조 (변경 없음)

### 2-1. 사용자 유형 (4가지)

```
┌──────────────┬──────────────┬──────────────┬──────────────────────────┐
│ 유형          │ memStatus    │ TB_USER      │ TB_USER_KT              │
├──────────────┼──────────────┼──────────────┼──────────────────────────┤
│ 정회원+KT ID │ G0001        │ O (CNTR_NO)  │ O (USER_ID=KT ID)       │
│ 정회원       │ G0002        │ O (CNTR_NO)  │ X                        │
│ 준회원(KT ID)│ G0003        │ X            │ O (USER_ID=KT ID)       │
│ SNS 임시     │ G0004(신규)  │ X            │ O (USER_ID=SNS!XXXXX)   │
└──────────────┴──────────────┴──────────────┴──────────────────────────┘
```

### 2-2. 테이블 구조 (AS-IS)

```
TB_USER (회선 사용자 테이블)
PK: CNTR_NO
┌───────────┬──────────────┬─────────────┬───────────────────────────┐
│ CNTR_NO   │ USER_ID      │ JOIN_STATUS │ MOBILE_NO, MEM_STATUS ... │
│ (PK)      │              │             │                           │
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
│ honggildong      │ G0001       │ 01012345678, 홍길동, Y    │
│ SNS!S0001_abc123 │ G0001       │ 01055551234, 김철수, N    │
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

---

## 3. TO-BE 설계: 매핑 기반 다중 ID 조회 전략

### 3-1. 핵심 원칙

```
┌──────────────────────────────────────────────────────────────────────┐
│  ★ v4 핵심 원칙                                                     │
│                                                                      │
│  1) 공용 테이블의 USER_ID는 절대 변경하지 않는다                     │
│     → SNS!XXXXX로 INSERT된 데이터는 영구히 SNS!XXXXX로 유지          │
│     → KT ID로 INSERT된 데이터는 영구히 KT ID로 유지                  │
│                                                                      │
│  2) snsId와 ktId는 credential_id를 매핑 키로 연결한다                │
│     → TB_USER_IDENTITY_MAP 테이블에 매핑 관계 저장                   │
│     → credential_id = IAMUI 영구 식별자                              │
│                                                                      │
│  3) 조회 시 본인의 모든 ID(snsId + ktId)를 IN 조건으로 확장          │
│     → Interceptor가 userId를 본인의 모든 ID 목록으로 변환            │
│     → WHERE USER_ID = #{userId}                                      │
│       → WHERE USER_ID IN ('SNS!S0001_abc123', 'honggildong')        │
│                                                                      │
│  4) TB_USER_KT에는 SNS 레코드와 KT 레코드가 각각 존재할 수 있다     │
│     → "SNS!S0001_abc123" 레코드: SNS 임시회원 정보 (G0004)           │
│     → "honggildong" 레코드: KT ID 회원 정보 (G0003)                  │
│     → 두 레코드가 동일인임은 credential_id 매핑으로 확인             │
│                                                                      │
│  5) KT ID 발급 후 로그인은 KT ID(honggildong)로 진행                │
│     → Interceptor가 매핑된 SNS ID도 함께 조회 범위에 포함            │
│     → 기존 SNS 이력이 자연스럽게 본인 이력으로 표시                   │
│                                                                      │
└──────────────────────────────────────────────────────────────────────┘
```

### 3-2. v4 데이터 흐름 개요

```
[Before: SNS ID로 활동]

  SNS 로그인 → userId = "SNS!S0001_abc123"

  공용 테이블 INSERT:
    TB_REWARD_INFO:      USER_ID = "SNS!S0001_abc123"   ← 영구 보존!
    TB_ATTEND_DAY_CHECK: USER_ID = "SNS!S0001_abc123"   ← 영구 보존!
    TB_EVENT_LIKE:       USER_ID = "SNS!S0001_abc123"   ← 영구 보존!

[After: KT ID 발급 후]

  KT ID 로그인 → userId = "honggildong"

  매핑 테이블 조회:
    TB_USER_IDENTITY_MAP:
      CREDENTIAL_ID = "cred_abc123"
      SNS_USER_ID   = "SNS!S0001_abc123"
      KT_USER_ID    = "honggildong"

  Interceptor 변환:
    WHERE USER_ID = #{userId}
    → WHERE USER_ID IN ('honggildong', 'SNS!S0001_abc123')

  결과: SNS ID로 쌓은 이력 + KT ID로 쌓은 이력 모두 조회!
```

### 3-3. DB 스키마 변경

```sql
-- ================================================================
-- [DDL-001] TB_USER_KT 컬럼 추가
-- ================================================================
ALTER TABLE TB_USER_KT ADD COLUMN CREDENTIAL_ID VARCHAR(200);

COMMENT ON COLUMN TB_USER_KT.CREDENTIAL_ID IS 'IAMUI credential_id (영구 사용자 식별자)';

-- CREDENTIAL_ID 인덱스 (NULL 허용)
CREATE INDEX IDX_USER_KT_CREDENTIAL_ID 
ON TB_USER_KT (CREDENTIAL_ID) 
WHERE CREDENTIAL_ID IS NOT NULL;


-- ================================================================
-- [DDL-002] TB_USER 컬럼 추가
-- ================================================================
ALTER TABLE TB_USER ADD COLUMN KT_ID VARCHAR(100);
ALTER TABLE TB_USER ADD COLUMN CREDENTIAL_ID VARCHAR(200);

CREATE INDEX idx_user_kt_id ON TB_USER(KT_ID);
CREATE INDEX idx_user_credential_id ON TB_USER(CREDENTIAL_ID);


-- ================================================================
-- [DDL-003] TB_USER_IDENTITY_MAP : SNS ID ↔ KT ID 매핑 테이블 (핵심!)
-- ================================================================
CREATE TABLE TB_USER_IDENTITY_MAP
(
    MAP_SEQ         SERIAL NOT NULL,
    SNS_USER_ID     VARCHAR(60) NOT NULL,       -- SNS!{type}_{key} 형태의 USER_ID
    KT_USER_ID      VARCHAR(50),                -- KT ID (매핑 완료 후 값 존재)
    CREDENTIAL_ID   VARCHAR(200) NOT NULL,      -- IAMUI credential_id (매핑 키!)
    SNS_TYPE        CHAR(5) NOT NULL,           -- S0001:카카오, S0002:라인, S0003:구글, S0004:애플
    SNS_KEY         VARCHAR(200) NOT NULL,      -- SNS 고유 식별키
    MAP_STATUS      CHAR(1) DEFAULT 'A',        -- A:활성(SNS only), M:매핑완료(KT ID 연동)
    MAP_DT          TIMESTAMP WITH TIME ZONE,   -- 매핑 완료일시
    REG_DT          TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    MOD_DT          TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (MAP_SEQ)
) WITHOUT OIDS;

COMMENT ON TABLE TB_USER_IDENTITY_MAP IS 'SNS ID ↔ KT ID 매핑 (credential_id 기반)';
COMMENT ON COLUMN TB_USER_IDENTITY_MAP.SNS_USER_ID IS 'SNS!{type}_{key} 형태의 USER_ID (공용 테이블에 저장된 원본 ID)';
COMMENT ON COLUMN TB_USER_IDENTITY_MAP.KT_USER_ID IS '정식 KT ID (매핑 완료 후 설정)';
COMMENT ON COLUMN TB_USER_IDENTITY_MAP.CREDENTIAL_ID IS 'IAMUI credential_id (매핑 연결 키)';
COMMENT ON COLUMN TB_USER_IDENTITY_MAP.MAP_STATUS IS 'A:활성(SNS-only), M:매핑완료(KT ID 연동)';

CREATE INDEX IDX_IDENTITY_MAP_SNS ON TB_USER_IDENTITY_MAP (SNS_USER_ID);
CREATE INDEX IDX_IDENTITY_MAP_KT ON TB_USER_IDENTITY_MAP (KT_USER_ID) WHERE KT_USER_ID IS NOT NULL;
CREATE UNIQUE INDEX IDX_IDENTITY_MAP_CREDENTIAL ON TB_USER_IDENTITY_MAP (CREDENTIAL_ID);
```

### 3-4. TO-BE 테이블 상태 예시

```
TB_USER_KT (변경 후 - PK 변경 없음!):
┌──────────────────┬────────────────┬───────────┬─────────────────────────┐
│ USER_ID          │ CREDENTIAL_ID  │MEM_STATUS │ 상태 설명                │
│ (PK)             │ (신규)          │           │                         │
├──────────────────┼────────────────┼───────────┼─────────────────────────┤
│ SNS!S0001_abc123 │ cred_abc123    │ G0004     │ SNS 가입 → KT 전환 후   │
│                  │                │           │ 레코드 유지! (PK 변경 X) │
│ honggildong      │ cred_abc123    │ G0003     │ KT ID 발급 시 새 INSERT  │
│ kt_user_555      │ cred_def456    │ G0003     │ 처음부터 KT ID 가입      │
└──────────────────┴────────────────┴───────────┴─────────────────────────┘

★ 주목: SNS 레코드와 KT 레코드가 동시에 존재 가능!
         둘은 같은 CREDENTIAL_ID로 연결됨.
         v3처럼 DELETE+INSERT로 PK 변경하지 않음.

★ 또는 SNS 레코드를 비활성화(MEM_STATUS 변경)하고 KT 레코드만 활성 사용:
   SNS!S0001_abc123  │ cred_abc123 │ G0005(비활성) │ KT 전환됨, 이력만 보존
   honggildong       │ cred_abc123 │ G0003         │ 활성 KT ID

(비활성화 여부는 정책에 따라 결정. 본 설계서는 비활성화를 기본으로 함)


TB_USER_IDENTITY_MAP (매핑 테이블):
┌─────┬──────────────────┬──────────────┬────────────────┬──────┬──────────┐
│ SEQ │ SNS_USER_ID      │ KT_USER_ID   │ CREDENTIAL_ID  │STATUS│ 설명      │
├─────┼──────────────────┼──────────────┼────────────────┼──────┼──────────┤
│  1  │ SNS!S0001_abc123 │ honggildong  │ cred_abc123    │ M    │ 매핑완료  │
│  2  │ SNS!S0003_xyz789 │ NULL         │ cred_xyz789    │ A    │ SNS만    │
└─────┴──────────────────┴──────────────┴────────────────┴──────┴──────────┘


공용 테이블 (데이터 변경 없음! 원본 그대로 유지):
┌───────────┬──────────────────┬────────────────────────────┐
│ CNTR_NO   │ USER_ID          │ 비고                        │
├───────────┼──────────────────┼────────────────────────────┤
│ C001      │ NULL             │ ← 회선 사용자, 변경 없음    │
│ NULL      │ honggildong      │ ← KT ID 이력, 변경 없음    │
│ NULL      │ SNS!S0001_abc123 │ ← SNS 이력, 영구 보존!     │
│ NULL      │ honggildong      │ ← KT ID 발급 후 새 이력    │
└───────────┴──────────────────┴────────────────────────────┘

★ "SNS!S0001_abc123"으로 남아있는 이력이 KT ID 로그인 시에도 조회됨!
   (Interceptor가 IN 조건으로 확장)
```

---

## 4. Interceptor 설계 (v4 - 다중 ID 확장)

### 4-1. v3 vs v4 Interceptor 동작 비교

```
[v3 Interceptor]
  userId = "SNS!S0001_abc123"
  → resolveToKtId() → "honggildong"
  → WHERE USER_ID = 'honggildong'
  → ★ 문제: SNS ID로 쌓인 이력은 조회 불가 (이미 UPDATE했으므로 OK이긴 함)

[v4 Interceptor]  ← 핵심 변경!
  userId = "honggildong" (KT ID로 로그인)
  → resolveAllUserIds() → ['honggildong', 'SNS!S0001_abc123']
  → WHERE USER_ID IN ('honggildong', 'SNS!S0001_abc123')
  → ★ 결과: 두 ID로 쌓인 이력 모두 조회!
```

### 4-2. 전체 변환 흐름

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
     ├─ userId만 존재 → resolveAllUserIds()
     │   │
     │   ├─ 매핑 테이블 조회 (credential_id 기반)
     │   │   → 본인의 모든 ID 목록 반환
     │   │   예: ['honggildong', 'SNS!S0001_abc123']
     │   │
     │   ├─ ID가 1개 (매핑 없음) → 기존처럼 단일 조건 유지
     │   │   WHERE USER_ID = #{userId}
     │   │
     │   └─ ID가 2개 이상 (매핑 있음) → IN 조건으로 확장
     │       WHERE USER_ID IN ('honggildong', 'SNS!S0001_abc123')
     │
     └─ 파라미터에 userId 없음 → 패스
```

### 4-3. Interceptor SQL 변환 방식 (2가지 옵션)

```
┌──────────────────────────────────────────────────────────────────────┐
│  ★ 옵션 A: 파라미터 확장 (권장 - 기존 Mapper XML 수정 최소)         │
│                                                                      │
│  Interceptor가 파라미터 Map에 userIdList를 추가하고,                 │
│  Mapper XML의 <choose> 블록에 userIdList 분기를 추가하는 방식.       │
│                                                                      │
│  파라미터 변경:                                                       │
│    기존: { "userId": "honggildong" }                                 │
│    변경: { "userId": "honggildong",                                  │
│            "userIdList": ["honggildong", "SNS!S0001_abc123"],        │
│            "_hasMultipleIds": true }                                  │
│                                                                      │
│  Mapper XML (기존 <choose> 블록에 1개 분기 추가):                     │
│    <choose>                                                          │
│      <when test='cntrNo != null and cntrNo != ""'>                   │
│        WHERE CNTR_NO = #{cntrNo}                                     │
│      </when>                                                         │
│      <when test='_hasMultipleIds != null and _hasMultipleIds'>       │
│        WHERE USER_ID IN                                              │
│        <foreach item="id" collection="userIdList"                    │
│                 open="(" separator="," close=")">                    │
│          #{id}                                                       │
│        </foreach>                                                    │
│      </when>                                                         │
│      <otherwise>                                                     │
│        WHERE USER_ID = #{userId}                                     │
│      </otherwise>                                                    │
│    </choose>                                                         │
│                                                                      │
│  ★ 장점: 기존 로직 완벽 호환, 매핑 없는 사용자는 기존과 동일        │
│  ★ 단점: 기존 Mapper XML에 <when> 분기 1개씩 추가 필요              │
│          (단, 기존 <choose> 구조를 활용하므로 변경량 적음)            │
│                                                                      │
└──────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────┐
│  ★ 옵션 B: BoundSql 직접 수정 (Mapper XML 수정 ZERO)                │
│                                                                      │
│  Interceptor가 BoundSql의 SQL 문을 직접 수정하여                     │
│  WHERE USER_ID = ? 를 WHERE USER_ID IN (?, ?) 로 변환.              │
│                                                                      │
│  동작 방식:                                                           │
│    1) BoundSql에서 SQL 추출                                          │
│    2) "USER_ID = ?" 패턴을 찾아 "USER_ID IN (?, ?)" 로 치환         │
│    3) PreparedStatement 파라미터도 확장                               │
│                                                                      │
│  ★ 장점: 기존 Mapper XML 수정 ZERO!                                 │
│  ★ 단점: SQL 직접 조작의 복잡성, JOIN ON 절의 USER_ID도              │
│          변환 필요, 디버깅 어려움                                     │
│                                                                      │
└──────────────────────────────────────────────────────────────────────┘

★ 권장: 옵션 A (파라미터 확장)
  이유:
  - 기존 Mapper XML의 <choose> 구조를 자연스럽게 활용
  - SQL 직접 조작 없이 MyBatis 표준 파라미터 바인딩 사용
  - 디버깅/로깅이 명확
  - <foreach>는 MyBatis 표준 기능으로 안정적
  - 기존 코드 검토 결과 <choose> 블록이 이미 38+ 곳에 존재하므로
    패턴이 확립되어 있어 추가 분기의 유지보수 부담 적음
```

### 4-4. Interceptor 제외/대상 Namespace (v3과 동일)

| Namespace | 처리 |
|-----------|------|
| `mybatis.mapper.user` | 제외 (TB_USER 직접 접근) |
| `mybatis.mapper.userkt` | 제외 (TB_USER_KT 직접 접근) |
| `mybatis.mapper.idmapping` | 제외 (순환 방지) |
| `mybatis.mapper.common` | 제외 (비사용자 쿼리) |
| `mybatis.mapper.event` | **대상** - 이벤트 조회/참여 |
| `mybatis.mapper.reward` | **대상** - 리워드 |
| `mybatis.mapper.attend` | **대상** - 출석체크 |
| `mybatis.mapper.cms` | **대상** - 푸시/알림 |
| `mybatis.mapper.ycanvas` | **대상** - 수강 |
| `mybatis.mapper.yfriends` | **대상** - 친구 초대 |
| `mybatis.mapper.ticket` | **대상** - 응모권 |
| `mybatis.mapper.vote` | **대상** - 투표 |

### 4-5. ID 매핑 조회 전략 (v4 신규)

```
resolveAllUserIds(userId) 호출:

  1단계: 캐시 조회 (ConcurrentHashMap)
         KEY: userId
         VALUE: Set<String> (본인의 모든 ID 목록)
         → 히트: 즉시 반환
         → "SINGLE": 매핑 없음, [userId] 단일 반환
         → 미스: 다음 단계

  2단계: TB_USER_IDENTITY_MAP 조회
         (매핑 테이블에서 credential_id로 연결된 모든 ID 조회)

         -- userId가 SNS ID인 경우:
         SELECT SNS_USER_ID, KT_USER_ID
         FROM TB_USER_IDENTITY_MAP
         WHERE SNS_USER_ID = #{userId}
         AND MAP_STATUS = 'M'

         -- userId가 KT ID인 경우:
         SELECT SNS_USER_ID, KT_USER_ID
         FROM TB_USER_IDENTITY_MAP
         WHERE KT_USER_ID = #{userId}
         AND MAP_STATUS = 'M'

         → 결과: ['honggildong', 'SNS!S0001_abc123']

  3단계: TB_USER_KT CREDENTIAL_ID 폴백 조회
         (매핑 테이블에 아직 없는 경우, CREDENTIAL_ID로 직접 조회)

         SELECT USER_ID
         FROM TB_USER_KT
         WHERE CREDENTIAL_ID = (
             SELECT CREDENTIAL_ID FROM TB_USER_KT
             WHERE USER_ID = #{userId}
             AND CREDENTIAL_ID IS NOT NULL
         )

         → 동일 CREDENTIAL_ID를 가진 모든 USER_ID 반환

  4단계: 결과 캐시 저장 후 반환
         cache.put(userId, Set<"honggildong", "SNS!S0001_abc123">)
         cache.put("honggildong", Set<"honggildong", "SNS!S0001_abc123">)
         cache.put("SNS!S0001_abc123", Set<"honggildong", "SNS!S0001_abc123">)
```

---

## 5. SNS 임시 ID 전체 라이프사이클 (v4)

### 5-1. 시나리오 1: SNS 신규 가입 (KT ID 미보유)

```
┌─────────────────────────────────────────────────────────────────────┐
│  시나리오 1: SNS 로그인 → 본인인증 → YBOX 가입 (KT ID 없음)         │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  [1] 사용자가 카카오 로그인                                          │
│      → SNS 제공자로부터 snsType="S0001", snsKey="abc123def456"     │
│      → 임시 ID 생성: "SNS!S0001_abc123def456"                      │
│                                                                     │
│  [2] IAMUI 인증 (본인인증)                                          │
│      → credentialId = "cred_abc123"                                │
│                                                                     │
│  [3] TB_USER_KT INSERT                                              │
│      ┌──────────────────────┬──────────┬──────────────┐             │
│      │ USER_ID(PK)          │MEM_STATUS│CREDENTIAL_ID │             │
│      ├──────────────────────┼──────────┼──────────────┤             │
│      │ SNS!S0001_abc123def… │ G0004    │cred_abc123   │             │
│      └──────────────────────┴──────────┴──────────────┘             │
│                                                                     │
│  [4] TB_USER_IDENTITY_MAP INSERT                                    │
│      ┌──────────────────────┬──────────┬──────────────┬────┐        │
│      │ SNS_USER_ID          │KT_USER_ID│CREDENTIAL_ID │STAT│        │
│      ├──────────────────────┼──────────┼──────────────┼────┤        │
│      │ SNS!S0001_abc123def… │ NULL     │cred_abc123   │ A  │        │
│      └──────────────────────┴──────────┴──────────────┴────┘        │
│                                                                     │
│  [5] 세션: userId = "SNS!S0001_abc123def456", memStatus = "G0004"  │
│                                                                     │
│  [6] YBOX 기능 사용 (이벤트 참여, 리워드 등)                         │
│      → 공용 테이블 INSERT: USER_ID = "SNS!S0001_abc123def456"       │
│                                                                     │
│  [7] Interceptor 동작:                                               │
│      → resolveAllUserIds("SNS!S0001_abc123def456")                  │
│      → 매핑 없음 (MAP_STATUS='A', KT_USER_ID=NULL)                 │
│      → 단일 ID로 기존과 동일: WHERE USER_ID = #{userId}             │
│      ✅ 정상 동작 (v3과 동일)                                       │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 5-2. 시나리오 2: SNS 사용자가 KT ID 발급 (핵심!)

```
┌─────────────────────────────────────────────────────────────────────┐
│  시나리오 2: 기존 SNS 사용자가 IAMUI 인증으로 KT ID 획득             │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  [1] IAMUI 인증 완료                                                 │
│      → credentialId = "cred_abc123"                                 │
│      → ktId = "honggildong"                                        │
│      → 세션의 oldSnsId = "SNS!S0001_abc123def456"                  │
│                                                                     │
│  [2] KtIdBindingService.bindKtId() 호출                             │
│                                                                     │
│  [3] ★ TB_USER_KT에 KT ID 레코드 INSERT (SNS 레코드 유지!)         │
│                                                                     │
│      TB_USER_KT 변경 후:                                            │
│      ┌──────────────────────┬──────────┬──────────────┐             │
│      │ USER_ID(PK)          │MEM_STATUS│CREDENTIAL_ID │             │
│      ├──────────────────────┼──────────┼──────────────┤             │
│      │ SNS!S0001_abc123def… │ G0005    │cred_abc123   │ ← 비활성   │
│      │ honggildong          │ G0003    │cred_abc123   │ ← 신규!    │
│      └──────────────────────┴──────────┴──────────────┘             │
│                                                                     │
│      ★ SNS 레코드의 PK는 그대로! MEM_STATUS만 G0005(비활성)로 변경 │
│      ★ KT ID 레코드를 새로 INSERT                                   │
│      ★ 두 레코드가 같은 CREDENTIAL_ID로 연결                        │
│                                                                     │
│  [4] ★ TB_USER_IDENTITY_MAP UPDATE (매핑 완료)                      │
│      ┌──────────────────────┬──────────────┬──────────────┬────┐    │
│      │ SNS_USER_ID          │ KT_USER_ID   │CREDENTIAL_ID │STAT│    │
│      ├──────────────────────┼──────────────┼──────────────┼────┤    │
│      │ SNS!S0001_abc123def… │ honggildong  │cred_abc123   │ M  │    │
│      └──────────────────────┴──────────────┴──────────────┴────┘    │
│                                                                     │
│  [5] ★ 공용 테이블 USER_ID는 변경하지 않음! ★                      │
│      TB_REWARD_INFO:      USER_ID = "SNS!S0001_abc123def456"        │
│      TB_ATTEND_DAY_CHECK: USER_ID = "SNS!S0001_abc123def456"        │
│      → 그대로 유지!                                                  │
│                                                                     │
│  [6] 캐시 갱신                                                       │
│      cache.put("honggildong",          ["honggildong","SNS!..."])   │
│      cache.put("SNS!S0001_abc123def…", ["honggildong","SNS!..."])   │
│                                                                     │
│  ✅ 결과: 공용 테이블 데이터 변경 ZERO!                              │
│           매핑 테이블만 UPDATE!                                       │
│           SNS 이력이 원본 그대로 보존!                                │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 5-3. 시나리오 3: KT ID로 로그인하여 SNS 이력 조회 (핵심!)

```
┌─────────────────────────────────────────────────────────────────────┐
│  시나리오 3: KT ID 전환 완료 후 KT ID로 로그인하여 기존 이력 조회    │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  [1] IAMUI 로그인 → userId = "honggildong"                          │
│                                                                     │
│  [2] 이벤트 목록 조회 API 호출                                       │
│      → eventMapper.getEventList(userId="honggildong")               │
│                                                                     │
│  [3] Interceptor 동작:                                               │
│      → resolveAllUserIds("honggildong")                             │
│      → 캐시 미스 → TB_USER_IDENTITY_MAP 조회                        │
│        SELECT SNS_USER_ID, KT_USER_ID                               │
│        FROM TB_USER_IDENTITY_MAP                                    │
│        WHERE KT_USER_ID = 'honggildong'                             │
│        AND MAP_STATUS = 'M'                                         │
│      → 결과: SNS_USER_ID = "SNS!S0001_abc123def456"                │
│      → 본인 ID 목록: ["honggildong", "SNS!S0001_abc123def456"]     │
│                                                                     │
│  [4] 파라미터 확장:                                                   │
│      기존: { "userId": "honggildong" }                               │
│      변경: { "userId": "honggildong",                                │
│              "userIdList": ["honggildong", "SNS!S0001_abc123def…"],  │
│              "_hasMultipleIds": true }                                │
│                                                                     │
│  [5] Mapper XML 실행 (옵션 A):                                       │
│      <choose>                                                        │
│        <when test='cntrNo != null and cntrNo != ""'>                 │
│          WHERE CNTR_NO = #{cntrNo}                                   │
│        </when>                                                       │
│        <when test='_hasMultipleIds != null and _hasMultipleIds'>     │
│          WHERE USER_ID IN                                            │
│          <foreach item="id" collection="userIdList"                  │
│                   open="(" separator="," close=")">                  │
│            #{id}                                                     │
│          </foreach>                                                  │
│        </when>                                                       │
│        <otherwise>                                                   │
│          WHERE USER_ID = #{userId}                                   │
│        </otherwise>                                                  │
│      </choose>                                                       │
│                                                                     │
│  [6] 실행 SQL:                                                       │
│      WHERE USER_ID IN ('honggildong', 'SNS!S0001_abc123def456')     │
│                                                                     │
│  [7] 결과:                                                           │
│      ┌──────────┬──────────────────────┬────────────────────┐        │
│      │ ISSUE_SEQ│ USER_ID              │ REG_DT             │        │
│      ├──────────┼──────────────────────┼────────────────────┤        │
│      │ 1001     │ SNS!S0001_abc123def… │ 2026-01-15 (SNS때) │        │
│      │ 1002     │ SNS!S0001_abc123def… │ 2026-02-20 (SNS때) │        │
│      │ 1003     │ honggildong          │ 2026-03-19 (KT이후)│        │
│      └──────────┴──────────────────────┴────────────────────┘        │
│                                                                     │
│  ✅ SNS 시절 이력 + KT ID 이후 이력 모두 본인 활동으로 조회!        │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 5-4. 시나리오 4: 기존 회선 사용자 (변경 없음)

```
[1] 회선 로그인 → CNTR_NO = "C001"
[2] Interceptor: cntrNo 존재 → 변환 건너뜀
[3] WHERE CNTR_NO = 'C001'
✅ 기존 로직 100% 유지 (v3과 동일)
```

### 5-5. 시나리오 5: 기존 KT ID 로그인 (매핑 없는 사용자)

```
[1] IAMUI 로그인 → userId = "kt_user_555"
[2] Interceptor: resolveAllUserIds("kt_user_555")
    → 매핑 테이블에 없음 → 단일 ID 반환
[3] 파라미터 변경 없음 (기존과 동일)
    WHERE USER_ID = 'kt_user_555'
✅ Interceptor 오버헤드 거의 없음
```

### 5-6. 시나리오 6: SNS 재로그인 (KT ID 전환 완료 후)

```
┌─────────────────────────────────────────────────────────────────────┐
│  시나리오 6: KT ID 전환 완료 후 다시 카카오로 로그인                  │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  [1] 카카오 로그인 → snsKey = "abc123def456"                        │
│      → 임시 ID: "SNS!S0001_abc123def456"                           │
│                                                                     │
│  [2] 로그인 처리:                                                    │
│      TB_USER_IDENTITY_MAP 조회:                                     │
│      SELECT KT_USER_ID FROM TB_USER_IDENTITY_MAP                    │
│      WHERE SNS_USER_ID = 'SNS!S0001_abc123def456'                   │
│      AND MAP_STATUS = 'M'                                           │
│      → KT_USER_ID = "honggildong" 발견!                             │
│                                                                     │
│  [3] 세션에 userId = "honggildong" 설정 (KT ID로 로그인)            │
│                                                                     │
│  [4] 이후 모든 조회: 시나리오 3과 동일하게 동작                      │
│      → Interceptor가 IN 조건으로 확장                                │
│      ✅ SNS 이력 + KT 이력 모두 조회                                 │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 6. KT ID 바인딩 프로세스 (v4 - 마이그레이션 없음!)

### 6-1. 전체 흐름 (v3의 KtIdMigrationService 대체)

```
bindKtId(credentialId, newKtId, oldSnsId)
     │
     ▼
[1] TB_USER_KT에 KT ID 레코드 INSERT
    │  ★ 기존 SNS 레코드는 유지! PK 변경 없음!
    │
    │  (a) KT ID 레코드가 이미 존재하는지 확인
    │  (b) 존재하지 않으면: KT ID로 신규 INSERT
    │      INSERT INTO TB_USER_KT (USER_ID=newKtId, CREDENTIAL_ID, 
    │                               MEM_STATUS='G0003', ...)
    │  (c) 존재하면: CREDENTIAL_ID만 갱신
    │
     ▼
[2] TB_USER_KT SNS 레코드 비활성화 (선택적)
    │  UPDATE TB_USER_KT 
    │  SET MEM_STATUS = 'G0005'    -- 비활성 (KT 전환됨)
    │  WHERE USER_ID = #{oldSnsId}
    │
    │  ★ 또는 SNS 레코드를 그대로 유지 (정책에 따라)
    │  ★ SNS 레코드의 CREDENTIAL_ID를 갱신하여 연결 표시
    │
     ▼
[3] TB_USER_IDENTITY_MAP UPDATE (매핑 완료)
    │  UPDATE TB_USER_IDENTITY_MAP SET
    │    KT_USER_ID = newKtId,
    │    MAP_STATUS = 'M',
    │    MAP_DT = NOW()
    │  WHERE SNS_USER_ID = oldSnsId
    │  AND CREDENTIAL_ID = credentialId
    │
     ▼
[4] TB_USER KT_ID 갱신 (회선 연동 시)
    │  UPDATE TB_USER SET KT_ID = newKtId
    │  WHERE CREDENTIAL_ID = credentialId
    │
     ▼
[5] 캐시 갱신
    │  cache.put(oldSnsId, Set["honggildong", "SNS!..."])
    │  cache.put(newKtId,  Set["honggildong", "SNS!..."])
    │
     ▼
✅ 완료

★ v3과의 결정적 차이:
   - 공용 테이블 USER_ID 일괄 UPDATE → 없음!
   - TB_USER_KT DELETE+INSERT → 없음! (SNS 레코드 유지)
   - TB_USER_KT 종속 테이블 일괄 UPDATE → 없음!
   - 실제 처리: TB_USER_KT INSERT 1건 + 매핑 테이블 UPDATE 1건
```

### 6-2. v3 vs v4 바인딩 프로세스 비교

```
┌──────────────────────────────────┬──────────────────────────────────┐
│ v3 (KtIdMigrationService)        │ v4 (KtIdBindingService)          │
├──────────────────────────────────┼──────────────────────────────────┤
│ TB_USER_KT DELETE+INSERT         │ TB_USER_KT INSERT (신규 레코드)  │
│ (PK 변경)                        │ (기존 SNS 레코드 유지)           │
│                                  │                                  │
│ TB_TERMS_AGREE_KT UPDATE         │ 없음!                            │
│ TB_SLEEP_USER_KT UPDATE          │ 없음!                            │
│ TB_UUID_KT UPDATE                │ 없음!                            │
│ TB_PREFERENCE_INFO_KT UPDATE     │ 없음!                            │
│                                  │                                  │
│ TB_REWARD_INFO UPDATE            │ 없음!                            │
│ TB_ATTEND_DAY_CHECK UPDATE       │ 없음!                            │
│ TB_VOTE_HISTORY UPDATE           │ 없음!                            │
│ TB_EVENT_LIKE UPDATE             │ 없음!                            │
│ TB_EVENT_REPLY UPDATE            │ 없음!                            │
│ TB_TICKET_REWARD_INFO UPDATE     │ 없음!                            │
│ TB_CLASS_JOIN UPDATE             │ 없음!                            │
│ TB_THREAD_PUSH_SEND UPDATE       │ 없음!                            │
│ TB_TICKET_GIFT_REWARD_INFO UPDATE│ 없음!                            │
│ TB_ENTRY_INFO RECV_USER_ID UPDATE│ 없음!                            │
│                                  │                                  │
│ TB_USER KT_ID UPDATE             │ TB_USER KT_ID UPDATE (동일)      │
│                                  │                                  │
│ ★ 총 UPDATE: 14+ 테이블          │ ★ 총 변경: 3건                   │
│ ★ 처리 시간: ~50-100ms           │ ★ 처리 시간: ~5ms                │
│ ★ 데이터 변경 리스크: 높음        │ ★ 데이터 변경 리스크: 없음       │
└──────────────────────────────────┴──────────────────────────────────┘
```

---

## 7. Interceptor의 SELECT/INSERT/UPDATE별 동작 (v4)

### 7-1. SELECT 쿼리 (조회)

```
[핵심] SELECT 시 본인의 모든 ID를 IN 조건으로 확장

예시: 이벤트 리워드 조회
  기존 SQL:  SELECT * FROM TB_REWARD_INFO WHERE USER_ID = #{userId}
  변환 SQL:  SELECT * FROM TB_REWARD_INFO 
             WHERE USER_ID IN ('honggildong', 'SNS!S0001_abc123')

→ SNS 시절 이력 + KT ID 이후 이력 모두 조회
→ 사용자 입장: "이전 혜택이 그대로 보인다!"
```

### 7-2. INSERT 쿼리 (등록)

```
[핵심] INSERT 시에는 현재 로그인 ID만 사용 (확장 안 함!)

예시: 이벤트 응모
  SQL: INSERT INTO TB_ENTRY_INFO (USER_ID, ...) VALUES (#{userId}, ...)

  KT ID로 로그인: USER_ID = 'honggildong' → 그대로 INSERT
  
→ Interceptor는 INSERT 시에는 userId를 변환하지 않음
→ 현재 로그인된 ID(KT ID)로 새 데이터 생성
→ 이후 해당 데이터는 KT ID로 조회됨
```

### 7-3. UPDATE 쿼리 (수정)

```
[핵심] UPDATE WHERE 절에서 본인의 모든 ID를 IN 조건으로 확장

예시: 이벤트 좋아요 취소
  기존 SQL:  DELETE FROM TB_EVENT_LIKE 
             WHERE USER_ID = #{userId} AND EVT_SEQ = #{evtSeq}
  변환 SQL:  DELETE FROM TB_EVENT_LIKE 
             WHERE USER_ID IN ('honggildong', 'SNS!S0001_abc123')
             AND EVT_SEQ = #{evtSeq}

→ SNS ID로 좋아요 한 것도 KT ID 로그인 시 취소 가능
```

### 7-4. Interceptor의 INSERT 제외 로직

```java
@Override
public Object intercept(Invocation invocation) throws Throwable {
    MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
    
    // INSERT 쿼리는 다중 ID 확장 하지 않음
    // (새 데이터는 현재 로그인 ID로만 INSERT)
    if (ms.getSqlCommandType() == SqlCommandType.INSERT) {
        return invocation.proceed();  // 변환 없이 원본 실행
    }
    
    // SELECT, UPDATE, DELETE는 다중 ID 확장
    // ...
}
```

---

## 8. 전체 아키텍처 (v4)

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
│     → TB_USER_IDENTITY_MAP 조회 → KT ID 발견                           │
│     → YSID: |mobile|honggildong|...|G0003                              │
│                                                                        │
└────────────────────────────────────┬────────────────────────────────────┘
                                     │
                                     ▼
╔═══════════════════════════════════════════════════════════════════════╗
║  ★ UserIdResolvingInterceptor (v4 - 다중 ID 확장) ★                 ║
║                                                                      ║
║  [대상] 공용 Mapper: event, reward, ycanvas, cms, yfriends 등        ║
║  [제외] user, userkt, idmapping, common                              ║
║  [제외] INSERT 쿼리 (새 데이터는 현재 ID로만 등록)                   ║
║                                                                      ║
║  if (cntrNo != null) → 패스                                          ║
║  if (INSERT) → 패스 (현재 ID로 등록)                                 ║
║  if (SELECT/UPDATE/DELETE) → resolveAllUserIds()                     ║
║     → 본인의 모든 ID 목록 조회 (매핑 테이블)                         ║
║     → userIdList 파라미터 추가                                       ║
║                                                                      ║
║  조회: 캐시 → TB_USER_IDENTITY_MAP → TB_USER_KT.CREDENTIAL_ID      ║
╚═══════════════════════════════════════════════════════════════════════╝
                                     │
                                     ▼
┌────────────────────────────────────────────────────────────────────────┐
│  MyBatis Executor → PreparedStatement → PostgreSQL                    │
│                                                                        │
│  Mapper XML 변경:                                                      │
│  기존 <choose> 블록에 _hasMultipleIds 분기 1개 추가:                   │
│    <when test='_hasMultipleIds != null and _hasMultipleIds'>           │
│      WHERE USER_ID IN <foreach ...>                                    │
│    </when>                                                             │
│                                                                        │
│  ★ 기존 choose 구조를 활용하므로 변경 최소화                           │
└────────────────────────────────────────────────────────────────────────┘
```

---

## 9. Mapper XML 변경 상세 (옵션 A 기준)

### 9-1. 기존 <choose> 블록 패턴 (현재 38+ 곳)

```xml
<!-- 현재 패턴 (eventMapper.xml, rewardMapper.xml 등) -->
<choose>
    <when test='cntrNo != null and cntrNo != ""'>
        WHERE CNTR_NO = #{cntrNo}
    </when>
    <otherwise>
        WHERE USER_ID = #{userId}
    </otherwise>
</choose>
```

### 9-2. v4 변경 패턴 (_hasMultipleIds 분기 추가)

```xml
<!-- v4 변경: _hasMultipleIds 분기 1개 추가 -->
<choose>
    <when test='cntrNo != null and cntrNo != ""'>
        WHERE CNTR_NO = #{cntrNo}
    </when>
    <when test='_hasMultipleIds != null and _hasMultipleIds'>
        WHERE USER_ID IN
        <foreach item="id" collection="userIdList"
                 open="(" separator="," close=")">
            #{id}
        </foreach>
    </when>
    <otherwise>
        WHERE USER_ID = #{userId}
    </otherwise>
</choose>
```

### 9-3. JOIN ON 절에서의 변경 (이벤트 상세 조회 등)

```xml
<!-- 현재 패턴 (JOIN ON 절에서 userId 사용) -->
<choose>
    <when test='cntrNo != null and cntrNo != ""'>
        LEFT OUTER JOIN TB_ENTRY_INFO C 
          ON C.ISSUE_SEQ = B.ISSUE_SEQ AND C.RECV_CNTR_NO = #{cntrNo}
        LEFT OUTER JOIN TB_REWARD_INFO D 
          ON D.ISSUE_SEQ = B.ISSUE_SEQ AND D.CNTR_NO = #{cntrNo}
    </when>
    <otherwise>
        LEFT OUTER JOIN TB_ENTRY_INFO C 
          ON C.ISSUE_SEQ = B.ISSUE_SEQ AND C.RECV_USER_ID = #{userId}
        LEFT OUTER JOIN TB_REWARD_INFO D 
          ON D.ISSUE_SEQ = B.ISSUE_SEQ AND D.USER_ID = #{userId}
    </otherwise>
</choose>

<!-- v4 변경 -->
<choose>
    <when test='cntrNo != null and cntrNo != ""'>
        LEFT OUTER JOIN TB_ENTRY_INFO C 
          ON C.ISSUE_SEQ = B.ISSUE_SEQ AND C.RECV_CNTR_NO = #{cntrNo}
        LEFT OUTER JOIN TB_REWARD_INFO D 
          ON D.ISSUE_SEQ = B.ISSUE_SEQ AND D.CNTR_NO = #{cntrNo}
    </when>
    <when test='_hasMultipleIds != null and _hasMultipleIds'>
        LEFT OUTER JOIN TB_ENTRY_INFO C 
          ON C.ISSUE_SEQ = B.ISSUE_SEQ 
          AND C.RECV_USER_ID IN
          <foreach item="id" collection="userIdList"
                   open="(" separator="," close=")">#{id}</foreach>
        LEFT OUTER JOIN TB_REWARD_INFO D 
          ON D.ISSUE_SEQ = B.ISSUE_SEQ 
          AND D.USER_ID IN
          <foreach item="id" collection="userIdList"
                   open="(" separator="," close=")">#{id}</foreach>
    </when>
    <otherwise>
        LEFT OUTER JOIN TB_ENTRY_INFO C 
          ON C.ISSUE_SEQ = B.ISSUE_SEQ AND C.RECV_USER_ID = #{userId}
        LEFT OUTER JOIN TB_REWARD_INFO D 
          ON D.ISSUE_SEQ = B.ISSUE_SEQ AND D.USER_ID = #{userId}
    </otherwise>
</choose>
```

### 9-4. 변경 대상 Mapper 목록 (옵션 A 기준)

| Mapper | choose 블록 수 | 변경 내용 |
|--------|:---:|------|
| eventMapper.xml | 21 | 각 choose에 `_hasMultipleIds` when 추가 |
| rewardMapper.xml | 6 | 동일 |
| cmsMapper.xml | 7 | 동일 |
| ycanvasMapper.xml | 3 | 동일 |
| yfriendsMapper.xml | 1 | 동일 |
| ticketMapper.xml | (if) | if 블록에 동일 패턴 추가 |
| voteMapper.xml | - | 추가 |
| attendMapper.xml | - | 추가 |
| **합계** | **38+** | **패턴 동일, 기계적 추가** |

```
★ 변경 난이도: 낮음
  - 모든 변경이 동일 패턴 (<when> 분기 1개 추가)
  - 기존 로직에 영향 없음 (새 분기는 _hasMultipleIds=true 일 때만 동작)
  - _hasMultipleIds가 없으면 기존 <otherwise>로 fallback
```

---

## 10. 옵션 B 상세: BoundSql 직접 수정 (Mapper XML 수정 ZERO)

```
★ 옵션 B를 채택할 경우 Mapper XML 수정이 필요 없습니다.
  Interceptor가 BoundSql의 SQL을 직접 수정합니다.
```

### 10-1. BoundSql 수정 방식

```java
/**
 * v4 Interceptor: BoundSql SQL 직접 수정 (옵션 B)
 */
private void rewriteSqlForMultipleIds(Invocation invocation, 
                                        List<String> userIds) {
    MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
    Object parameter = invocation.getArgs()[1];
    BoundSql boundSql = ms.getBoundSql(parameter);
    
    String originalSql = boundSql.getSql();
    
    // USER_ID = ? 패턴을 USER_ID IN (?, ?) 로 치환
    // (CNTR_NO 조건은 건드리지 않음)
    String placeholders = userIds.stream()
        .map(id -> "?")
        .collect(Collectors.joining(", "));
    
    String newSql = originalSql.replace(
        "USER_ID = ?",
        "USER_ID IN (" + placeholders + ")"
    );
    
    // RECV_USER_ID = ? 도 치환
    newSql = newSql.replace(
        "RECV_USER_ID = ?",
        "RECV_USER_ID IN (" + placeholders + ")"
    );
    
    // BoundSql 교체 (리플렉션)
    Field sqlField = BoundSql.class.getDeclaredField("sql");
    sqlField.setAccessible(true);
    sqlField.set(boundSql, newSql);
    
    // 추가 파라미터 바인딩
    // ... (PreparedStatement 파라미터 순서 조정)
}
```

### 10-2. 옵션 B의 장단점

```
장점:
  ✅ Mapper XML 수정 ZERO (38+ 곳 변경 불필요)
  ✅ 신규 Mapper 추가 시에도 자동 적용

단점:
  ⚠️ SQL 직접 조작의 복잡성 (정규표현식 기반 치환)
  ⚠️ JOIN ON 절의 USER_ID도 치환해야 함
  ⚠️ PreparedStatement 파라미터 순서 변경 복잡
  ⚠️ 디버깅 어려움 (실제 SQL과 Mapper XML SQL 불일치)
  ⚠️ 서브쿼리 내 USER_ID도 치환 필요
  ⚠️ MyBatis 버전 업그레이드 시 호환성 이슈 가능
```

### 10-3. 권장안

```
★ 옵션 A (파라미터 확장) 권장

이유:
  1) 38곳 변경이 많아 보이지만, 모두 동일 패턴이므로
     스크립트로 자동 변경 가능
  2) SQL 직접 조작보다 MyBatis 표준 파라미터 방식이 안전
  3) 디버깅/로깅 시 실제 실행되는 SQL 확인 용이
  4) 이미 <choose> 구조가 확립되어 있어 유지보수 부담 적음
  5) INSERT 쿼리 제외 로직이 명확 (SqlCommandType 체크)
  
★ 또는 옵션 B를 채택하여 Mapper XML 수정 ZERO로 갈 수도 있음
  (팀 협의 후 결정)
```

---

## 11. 구현 파일 목록 및 역할 (v4)

| # | 파일 | 역할 | v3 대비 변경 |
|---|------|------|-------------|
| 1 | `UserIdResolvingInterceptor.java` | MyBatis Plugin | **전면 재작성**: 1:1 치환 → 다중 ID 확장 |
| 2 | `MyBatisInterceptorConfig.java` | Interceptor Spring 등록 | v4 로그 메시지 |
| 3 | `IdMappingCacheService.java` | 캐시 + DB 조회 | **전면 재작성**: 매핑 테이블 조회, Set<String> 캐시 |
| 4 | `KtIdBindingService.java` (신규) | KT ID 바인딩 | **신규**: v3의 Migration 대체, UPDATE 없음 |
| 5 | `idMappingMapper.xml` | 매핑 전용 SQL | **전면 재작성**: 매핑 테이블 CRUD 중심 |
| 6 | `mybatis-interceptor-design-v4.md` | 이 설계 문서 | v4 전면 재작성 |
| 7 | (옵션 A) `eventMapper.xml` 등 38곳 | choose 블록 변경 | `_hasMultipleIds` 분기 추가 |

### v3 → v4 삭제 대상

| 파일 | 이유 |
|------|------|
| `KtIdMigrationService.java` | 공용 테이블 UPDATE 불필요 → KtIdBindingService로 대체 |

---

## 12. 성능 분석 (v4)

| 케이스 | 오버헤드 | 설명 |
|--------|---------|------|
| cntrNo 있음 (회선) | ~ 0ms | null 체크 후 즉시 패스 |
| SKIP namespace | ~ 0ms | startsWith 체크 후 즉시 패스 |
| INSERT 쿼리 | ~ 0ms | SqlCommandType 체크 후 패스 |
| 캐시 히트 (매핑 없음) | < 0.01ms | ConcurrentHashMap.get() |
| 캐시 히트 (매핑 있음) | < 0.01ms | Set<String> 반환 |
| 캐시 미스 - 매핑 테이블 조회 | ~1ms | TB_USER_IDENTITY_MAP 인덱스 |
| 캐시 미스 - CREDENTIAL_ID 폴백 | ~2ms | TB_USER_KT CREDENTIAL_ID |
| KT ID 바인딩 (1회성) | ~5ms | INSERT 1건 + UPDATE 1건 |
| IN 조건 확장 (SELECT) | ~0.5ms | IN (2-3개 ID) 추가 오버헤드 |

### v3 vs v4 성능 비교

```
┌───────────────────────────────┬──────────┬──────────┐
│ 항목                          │ v3       │ v4       │
├───────────────────────────────┼──────────┼──────────┤
│ KT ID 바인딩 (1회성)          │ ~100ms   │ ~5ms     │ ← 20배 빠름!
│ SELECT 쿼리 (캐시 히트)       │ <0.01ms  │ <0.01ms  │ 동일
│ SELECT 쿼리 (IN 확장)         │ N/A      │ ~0.5ms   │ 신규 (미미)
│ 공용 테이블 데이터 변경 건수    │ 수십~수백│ 0        │ ← ZERO!
│ 트랜잭션 크기 (바인딩)         │ 대형     │ 소형     │ 훨씬 안전
└───────────────────────────────┴──────────┴──────────┘
```

### 캐시 전략

```
- 캐시 키: userId (String)
- 캐시 값: Set<String> (본인의 모든 ID)
- 매핑 있음 TTL: 1시간
- 매핑 없음 TTL: 10분 (KT ID 발급을 빠르게 감지)
- KT ID 바인딩 시: 즉시 캐시 갱신 (양방향)
- Redis 교체: 다중 서버 시 Set<String> → Redis Set으로 교체 가능
```

---

## 13. 주의사항 및 엣지 케이스

### 13-1. IN 조건의 중복 결과 방지

```
문제: IN 조건으로 다중 ID 조회 시 같은 데이터가 중복 나올 수 있는가?

답변: 아니요. 하나의 데이터 행은 하나의 USER_ID만 가지므로 중복 없음.
      - TB_REWARD_INFO 행1: USER_ID = "SNS!S0001_abc123"
      - TB_REWARD_INFO 행2: USER_ID = "honggildong"
      → IN 조건으로 두 행 모두 조회, 중복 없음.
```

### 13-2. INSERT 시 ID 선택

```
KT ID 발급 후 새로운 활동(이벤트 응모 등)은 항상 KT ID로 INSERT.
  → 세션에 KT ID가 설정되어 있으므로 자연스럽게 KT ID로 INSERT
  → Interceptor는 INSERT를 변환하지 않음
```

### 13-3. COUNT 쿼리에서의 IN 조건

```
문제: 중복 참여 체크 시 IN 조건이 문제가 될 수 있는가?

예시: "이미 응모한 이벤트인지 체크"
  SELECT COUNT(*) FROM TB_ENTRY_INFO 
  WHERE EVT_SEQ = #{evtSeq}
  AND RECV_USER_ID IN ('honggildong', 'SNS!S0001_abc123')

→ SNS ID로 이미 응모했으면 KT ID 로그인 시에도 중복 참여 방지됨!
→ 오히려 v4가 더 정확한 중복 체크를 제공함
   (v3에서는 마이그레이션 실패 시 중복 참여 가능했음)
```

### 13-4. 동시 로그인 방지

```
SNS 로그인과 KT ID 바인딩이 동시에 일어나는 경우:
  → KtIdBindingService에서 캐시를 즉시 갱신
  → 이후 쿼리부터는 다중 ID 확장 동작
  → 바인딩 직전 쿼리는 단일 ID로 실행 (문제 없음)
```

### 13-5. 한 사용자의 복수 SNS 계정

```
한 사용자가 여러 SNS로 가입한 경우:
  → TB_USER_IDENTITY_MAP에 여러 행 존재 가능
  → credential_id가 같으면 모두 같은 사용자
  → resolveAllUserIds()가 모든 SNS ID + KT ID 반환
  → IN 조건에 모든 ID 포함

예시:
  TB_USER_IDENTITY_MAP:
    SNS!S0001_abc123 / honggildong / cred_abc123 / M
    SNS!S0003_xyz789 / honggildong / cred_abc123 / M

  resolveAllUserIds("honggildong") 
  → ["honggildong", "SNS!S0001_abc123", "SNS!S0003_xyz789"]

  WHERE USER_ID IN ('honggildong', 'SNS!S0001_abc123', 'SNS!S0003_xyz789')
```

### 13-6. TB_USER_KT에 두 레코드 존재 시 로그인 처리

```
TB_USER_KT에 SNS 레코드와 KT 레코드가 모두 있을 때:
  → SNS 레코드: USER_ID="SNS!S0001_abc123", MEM_STATUS=G0005 (비활성)
  → KT 레코드: USER_ID="honggildong", MEM_STATUS=G0003 (활성)

SNS로 재로그인 시:
  1) TB_USER_IDENTITY_MAP에서 KT_USER_ID 조회
  2) KT_USER_ID 존재 → KT ID로 세션 설정
  3) G0005 레코드는 접근하지 않음 (비활성)

KT ID로 직접 로그인 시:
  1) TB_USER_KT에서 USER_ID="honggildong" 조회
  2) MEM_STATUS=G0003 → 정상 로그인
  3) SNS 레코드(G0005)는 무시
```

---

## 14. memStatus 코드 확장

```
┌──────────┬──────────────────────────────────────────────────────────┐
│ memStatus│ 설명                                                     │
├──────────┼──────────────────────────────────────────────────────────┤
│ G0001    │ 정회원 + KT ID : TB_USER(CNTR_NO) + TB_USER_KT(KT ID)  │
│ G0002    │ 정회원 (KT ID 없음) : TB_USER(CNTR_NO)만 존재           │
│ G0003    │ 준회원 (KT ID) : TB_USER_KT(USER_ID=KT ID)만 존재       │
│ G0004    │ SNS 임시회원 : TB_USER_KT(USER_ID=SNS!XXXXX) 활성       │
│ G0005    │ [신규] SNS 전환완료 : TB_USER_KT(USER_ID=SNS!XXXXX)     │
│          │   KT ID 바인딩 완료 후 비활성 (이력 보존용)               │
└──────────┴──────────────────────────────────────────────────────────┘
```

---

## 15. idMappingMapper.xml 쿼리 설계 (v4)

```xml
<!-- ================================================================ -->
<!-- [조회] 매핑 테이블 기반 다중 ID 조회                                -->
<!-- ================================================================ -->

<!-- credential_id 기반으로 연결된 모든 ID 조회 (KT ID로 조회 시) -->
<select id="getAllLinkedUserIds" parameterType="String" resultType="String">
    SELECT SNS_USER_ID AS USER_ID
    FROM TB_USER_IDENTITY_MAP
    WHERE KT_USER_ID = #{userId}
    AND MAP_STATUS = 'M'
    UNION ALL
    SELECT #{userId} AS USER_ID
</select>

<!-- SNS ID로 조회 시 연결된 KT ID 포함 모든 ID 조회 -->
<select id="getAllLinkedUserIdsBySns" parameterType="String" resultType="String">
    SELECT KT_USER_ID AS USER_ID
    FROM TB_USER_IDENTITY_MAP
    WHERE SNS_USER_ID = #{userId}
    AND MAP_STATUS = 'M'
    AND KT_USER_ID IS NOT NULL
    UNION ALL
    SELECT #{userId} AS USER_ID
</select>

<!-- credential_id 폴백: TB_USER_KT에서 같은 credential_id의 모든 USER_ID -->
<select id="getAllUserIdsByCredential" parameterType="String" resultType="String">
    SELECT B.USER_ID
    FROM TB_USER_KT B
    WHERE B.CREDENTIAL_ID = (
        SELECT A.CREDENTIAL_ID
        FROM TB_USER_KT A
        WHERE A.USER_ID = #{userId}
        AND A.CREDENTIAL_ID IS NOT NULL
        LIMIT 1
    )
</select>

<!-- SNS 재로그인 시 KT_USER_ID 조회 -->
<select id="getKtIdBySnsUserId" parameterType="String" resultType="String">
    SELECT KT_USER_ID
    FROM TB_USER_IDENTITY_MAP
    WHERE SNS_USER_ID = #{snsUserId}
    AND MAP_STATUS = 'M'
    AND KT_USER_ID IS NOT NULL
    LIMIT 1
</select>

<!-- ================================================================ -->
<!-- [바인딩] KT ID 바인딩 시 실행되는 쿼리들                           -->
<!-- ================================================================ -->

<!-- KT ID 레코드 존재 여부 확인 -->
<select id="checkKtIdExists" parameterType="String" resultType="int">
    SELECT COUNT(*)
    FROM TB_USER_KT
    WHERE USER_ID = #{ktId}
</select>

<!-- TB_USER_IDENTITY_MAP 매핑 완료 -->
<update id="completeMapping" parameterType="Map">
    UPDATE TB_USER_IDENTITY_MAP SET
        KT_USER_ID = #{ktUserId},
        MAP_STATUS = 'M',
        MAP_DT = NOW(),
        MOD_DT = NOW()
    WHERE SNS_USER_ID = #{snsUserId}
    AND CREDENTIAL_ID = #{credentialId}
</update>

<!-- SNS 레코드 비활성화 -->
<update id="deactivateSnsRecord" parameterType="Map">
    UPDATE TB_USER_KT SET
        MEM_STATUS = 'G0005',
        MOD_DT = NOW()
    WHERE USER_ID = #{snsUserId}
</update>

<!-- TB_USER KT_ID 갱신 -->
<update id="updateUserKtId" parameterType="Map">
    UPDATE TB_USER SET
        KT_ID = #{newKtId},
        MOD_DT = NOW()
    WHERE CREDENTIAL_ID = #{credentialId}
</update>
```

---

## 16. 결론

```
┌────────────────────────────────────────────────────────────────────┐
│                                                                    │
│  v4 핵심 특징:                                                     │
│                                                                    │
│  ✅ 공용 테이블 USER_ID 절대 변경 안 함 (요건 1 충족)             │
│                                                                    │
│  ✅ snsId ↔ ktId는 credential_id 기반 매핑만 (요건 2 충족)        │
│     → TB_USER_IDENTITY_MAP 테이블에 매핑 관계 저장                 │
│     → 매핑 키 = credential_id (IAMUI 영구 식별자)                  │
│                                                                    │
│  ✅ SNS 이력이 KT ID 로그인 시에도 본인 이력으로 조회 (요건 3 충족)│
│     → Interceptor가 SELECT/UPDATE/DELETE 시                        │
│       본인의 모든 ID를 IN 조건으로 확장                             │
│     → WHERE USER_ID = #{userId}                                    │
│       → WHERE USER_ID IN ('ktId', 'snsId1', 'snsId2')            │
│                                                                    │
│  ✅ KT ID 바인딩 시 데이터 변경 ZERO (공용 테이블 UPDATE 없음)    │
│     → 바인딩 처리: INSERT 1건 + UPDATE 1건 (매핑 테이블)           │
│     → v3 대비 처리 시간 20배 단축 (~100ms → ~5ms)                 │
│     → 트랜잭션 크기 대폭 감소 → 안전성 향상                        │
│                                                                    │
│  ✅ 기존 Mapper XML 변경: 38곳에 <when> 분기 1개씩 추가            │
│     → 또는 옵션 B(BoundSql 직접 수정) 선택 시 변경 ZERO           │
│                                                                    │
│  ✅ TB_USER_KT PK 변경 없음 (DELETE+INSERT 불필요)                │
│     → SNS 레코드는 비활성화만 (데이터 보존)                         │
│                                                                    │
│  구현 파일: Java 4개 + XML 1개 + 설계 문서 1개                      │
│             + (옵션 A) Mapper XML 38곳 패턴 추가                    │
│                                                                    │
│  핵심 원칙:                                                        │
│  • 데이터는 변경하지 않는다 (원본 보존)                             │
│  • 매핑만 한다 (credential_id 기반)                                 │
│  • 조회 시 매핑된 모든 ID를 IN 조건으로 확장한다                    │
│  • INSERT 시에는 현재 로그인 ID만 사용한다                          │
│                                                                    │
└────────────────────────────────────────────────────────────────────┘
```

---

## 부록 A: v3 → v4 마이그레이션 체크리스트

```
[ ] 1. DDL 실행
    [ ] TB_USER_KT에 CREDENTIAL_ID 컬럼 추가
    [ ] TB_USER에 KT_ID, CREDENTIAL_ID 컬럼 추가
    [ ] TB_USER_IDENTITY_MAP 테이블 생성
    [ ] 인덱스 생성

[ ] 2. Java 파일 변경
    [ ] UserIdResolvingInterceptor.java 재작성 (다중 ID 확장)
    [ ] IdMappingCacheService.java 재작성 (Set<String> 캐시)
    [ ] KtIdMigrationService.java 삭제 → KtIdBindingService.java 신규
    [ ] MyBatisInterceptorConfig.java 수정 (v4 로그)

[ ] 3. XML 파일 변경
    [ ] idMappingMapper.xml 재작성 (매핑 테이블 CRUD)
    [ ] (옵션 A) eventMapper.xml 등 38곳 choose 블록 변경

[ ] 4. 기존 데이터 마이그레이션
    [ ] v3에서 이미 UPDATE된 공용 테이블 데이터는 되돌리지 않음
        (v3 마이그레이션이 아직 실행되지 않았다면 해당 없음)
    [ ] 기존 TB_USER_KT의 CREDENTIAL_ID 채우기 (backfill)

[ ] 5. 테스트
    [ ] SNS 신규 가입 → SNS ID로 이벤트 참여 → 이력 조회
    [ ] KT ID 바인딩 → KT ID 로그인 → SNS 이력 + KT 이력 조회
    [ ] 회선 사용자 로그인 → 기존 로직 동일
    [ ] SNS 재로그인 → KT ID로 세션 설정 → 이력 조회
    [ ] 중복 참여 체크 (SNS ID + KT ID 통합 체크)
```

---

## 부록 B: SQL 비교 (v3 vs v4)

### KT ID 바인딩 시 실행 SQL

```sql
-- ===== v3: KtIdMigrationService (14+ UPDATE) =====

-- 1) PK 변경
DELETE FROM TB_USER_KT WHERE USER_ID = 'SNS!S0001_abc123';
INSERT INTO TB_USER_KT (USER_ID, ...) SELECT 'honggildong', ... FROM ...;

-- 2) 종속 테이블 (4건)
UPDATE TB_TERMS_AGREE_KT SET USER_ID = 'honggildong' WHERE USER_ID = 'SNS!...';
UPDATE TB_SLEEP_USER_KT SET USER_ID = 'honggildong' WHERE USER_ID = 'SNS!...';
UPDATE TB_UUID_KT SET USER_ID = 'honggildong' WHERE USER_ID = 'SNS!...';
UPDATE TB_PREFERENCE_INFO_KT SET KT_ID = 'honggildong' WHERE KT_ID = 'SNS!...';

-- 3) 공용 테이블 (9건)
UPDATE TB_REWARD_INFO SET USER_ID = 'honggildong' WHERE USER_ID = 'SNS!...';
UPDATE TB_ATTEND_DAY_CHECK SET USER_ID = 'honggildong' WHERE USER_ID = 'SNS!...';
UPDATE TB_VOTE_HISTORY SET USER_ID = 'honggildong' WHERE USER_ID = 'SNS!...';
UPDATE TB_EVENT_LIKE SET USER_ID = 'honggildong' WHERE USER_ID = 'SNS!...';
UPDATE TB_EVENT_REPLY SET USER_ID = 'honggildong' WHERE USER_ID = 'SNS!...';
UPDATE TB_TICKET_REWARD_INFO SET USER_ID = 'honggildong' WHERE USER_ID = 'SNS!...';
UPDATE TB_CLASS_JOIN SET USER_ID = 'honggildong' WHERE USER_ID = 'SNS!...';
UPDATE TB_THREAD_PUSH_SEND SET USER_ID = 'honggildong' WHERE USER_ID = 'SNS!...';
UPDATE TB_TICKET_GIFT_REWARD_INFO SET USER_ID = 'honggildong' WHERE USER_ID = 'SNS!...';

-- 4) RECV_USER_ID (1건)
UPDATE TB_ENTRY_INFO SET RECV_USER_ID = 'honggildong' WHERE RECV_USER_ID = 'SNS!...';

-- 5) TB_USER
UPDATE TB_USER SET KT_ID = 'honggildong' WHERE CREDENTIAL_ID = 'cred_abc123';

-- ★ 총 SQL: 16+건, 영향 행: 수십~수백 건


-- ===== v4: KtIdBindingService (3건만!) =====

-- 1) KT ID 레코드 INSERT
INSERT INTO TB_USER_KT (USER_ID, CREDENTIAL_ID, MEM_STATUS, ...)
VALUES ('honggildong', 'cred_abc123', 'G0003', ...);

-- 2) SNS 레코드 비활성화
UPDATE TB_USER_KT SET MEM_STATUS = 'G0005' WHERE USER_ID = 'SNS!S0001_abc123';

-- 3) 매핑 테이블 UPDATE
UPDATE TB_USER_IDENTITY_MAP SET
    KT_USER_ID = 'honggildong', MAP_STATUS = 'M', MAP_DT = NOW()
WHERE SNS_USER_ID = 'SNS!S0001_abc123' AND CREDENTIAL_ID = 'cred_abc123';

-- (선택) TB_USER KT_ID 갱신
UPDATE TB_USER SET KT_ID = 'honggildong' WHERE CREDENTIAL_ID = 'cred_abc123';

-- ★ 총 SQL: 3~4건, 영향 행: 3~4건
-- ★ 공용 테이블 데이터 변경: ZERO!
```
