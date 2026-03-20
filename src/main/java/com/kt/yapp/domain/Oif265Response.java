package com.kt.yapp.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * OIF_265 getComLoginTokenIdNoCredtId 응답 VO (루트)
 *
 * <pre>
 * KT API Link OIF_265 REST 응답 전체를 매핑한다.
 *
 * ── 응답 JSON 구조 ──────────────────────────────────────────────
 * {
 *   "returncode"        : "1",          // 결과코드 (1=Success, 0=Fail)
 *   "returndescription" : "Success",    // 결과메세지
 *   "sequenceno"        : "4",          // 시스템 내부 구간 순서
 *   "transactionid"     : "9287...",    // API Gateway 일련번호
 *   "errorcode"         : "E0001",      // 오류코드 (실패 시)
 *   "errordescription"  : "인증키오류", // 오류설명 (실패 시)
 *   "response" : {
 *     "username"                : "KTIDks",  // 로그인 ID (kt_userid)
 *     "virtual_subscpn_type_cd" : "01",      // 계정유형 (01=KT ID, 09=회선ID)
 *     "olleh_text_id"           : "...",     // KT text ID (이메일 인증 시)
 *     "token_id"                : "...",     // 복호화된 tokenId
 *     "token_expiry"            : "20261231235959", // 만료일 (yyyyMMddHHmmss)
 *     "phone_number"            : "01012345678",    // 대표 전화번호
 *     "list_of_ctn" : {
 *       "ctn" : ["01012345678", ...]   // 모바일 번호 목록 (최대 5개)
 *     }
 *   }
 * }
 * ──────────────────────────────────────────────────────────────
 *
 * ── 주요 사용처 ──────────────────────────────────────────────────
 *   ShubService.callFn265(encTokenId) 반환값
 *   IamUiTokenServiceImpl 에서 token_id 추출 후 callFn188 대체 또는 직접 사용
 * ──────────────────────────────────────────────────────────────
 * </pre>
 *
 * @author YBOX IAMUI 전환 개발팀
 * @version 1.0 (2026-03-04, BE-CMN-003 / OIF_265)
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Oif265Response {

    /** 결과코드 ("1" = Success, "0" = Fail) */
    @ApiModelProperty(value = "결과코드 (1=Success, 0=Fail)")
    private String returncode;

    /** 결과메세지 */
    @ApiModelProperty(value = "결과메세지")
    private String returndescription;

    /** 시스템 내부 구간 순서 */
    @ApiModelProperty(value = "시스템 내부 구간 순서")
    private String sequenceno;

    /** API Link Gateway 일련번호 */
    @ApiModelProperty(value = "API Gateway 일련번호")
    private String transactionid;

    /** 오류코드 (실패 시, 예: E0001=인증키오류) */
    @ApiModelProperty(value = "오류코드 (실패 시)")
    private String errorcode;

    /** 오류설명 (실패 시) */
    @ApiModelProperty(value = "오류설명 (실패 시)")
    private String errordescription;

    /** OIF_265 응답 데이터 */
    @ApiModelProperty(value = "OIF_265 응답 데이터 객체")
    private ResponseBody response;

    // ─────────────────────────────────────────────────────────────
    // 편의 메서드
    // ─────────────────────────────────────────────────────────────

    /** 성공 여부 ("1" == returncode) */
    public boolean isSuccess() {
        return "1".equals(returncode);
    }

    /** response.token_id (복호화된 tokenId) – null-safe */
    public String getTokenId() {
        return (response != null) ? response.getToken_id() : null;
    }

    /** response.username (로그인 ID / kt_userid) – null-safe */
    public String getUsername() {
        return (response != null) ? response.getUsername() : null;
    }

    /** response.virtual_subscpn_type_cd (계정유형: 01=KT ID, 09=회선ID) – null-safe */
    public String getVirtualSubscpnTypeCd() {
        return (response != null) ? response.getVirtual_subscpn_type_cd() : null;
    }

    /** response.phone_number (대표 전화번호) – null-safe */
    public String getPhoneNumber() {
        return (response != null) ? response.getPhone_number() : null;
    }

    /** response.token_expiry (토큰 만료일 yyyyMMddHHmmss) – null-safe */
    public String getTokenExpiry() {
        return (response != null) ? response.getToken_expiry() : null;
    }

    // ─────────────────────────────────────────────────────────────
    // 중첩 클래스: response 객체
    // ─────────────────────────────────────────────────────────────

    /**
     * OIF_265 response 내부 객체
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResponseBody {

        /** 로그인 ID (KT 아이디, kt_userid) */
        @ApiModelProperty(value = "로그인 ID (KT 아이디)")
        private String username;

        /** 계정 유형 코드 (01=KT ID, 09=회선ID) */
        @ApiModelProperty(value = "계정 유형 코드 (01=KT ID, 09=회선ID)")
        private String virtual_subscpn_type_cd;

        /** KT text ID (이메일 ID 인증 시 제공) */
        @ApiModelProperty(value = "KT text ID (이메일 인증 시)")
        private String olleh_text_id;

        /** 복호화된 tokenId */
        @ApiModelProperty(value = "복호화된 tokenId")
        private String token_id;

        /** 토큰 만료일 (yyyyMMddHHmmss) */
        @ApiModelProperty(value = "토큰 만료일 (yyyyMMddHHmmss)")
        private String token_expiry;

        /** 대표 전화번호 (쇼정회원 관계 중 대표 번호) */
        @ApiModelProperty(value = "대표 전화번호")
        private String phone_number;

        /** 모바일 번호 목록 (최대 5개) */
        @ApiModelProperty(value = "모바일 번호 목록")
        private ListOfCtn list_of_ctn;
    }

    /**
     * OIF_265 list_of_ctn 객체 (쇼 정회원 관계 모바일 번호)
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListOfCtn {

        /** 전화번호 배열 (최대 5개) */
        @ApiModelProperty(value = "전화번호 목록 (최대 5개)")
        @JsonProperty("ctn")
        private List<String> ctn;
    }
}
