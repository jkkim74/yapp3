package com.kt.yapp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * OIF_265 (getComLoginTokenIdNoCredtId) 요청 VO (BE-CMN-003)
 *
 * <pre>
 * POST /scap/v1.0/getComLoginTokenIdNoCredtId
 * Content-Type: application/json;charset=UTF-8
 * Authorization: Basic {base64(connId:connPwd)}
 *
 * {
 *   "request": {
 *     "enc_token_id": "7BE15E1E1BB36F7..."
 *   }
 * }
 *
 * ── OIF_265 API 기본정보 ────────────────────────────────────────
 *   API ID   : OIF_265
 *   API명    : getComLoginTokenIdNoCredtId [공통로그인 전용]
 *   설명     : 공통로그인을 통해 발급한 암호화된 token_id를 복호화하여 제공
 *              (OIF_259에서 계정의 식별키를 제외하고 제공)
 *   Method   : POST
 *   Path     : /scap/v1.0/getComLoginTokenIdNoCredtId
 *   Host     : https://cus.api.kt.com
 * ──────────────────────────────────────────────────────────────
 * </pre>
 *
 * @author YBOX IAMUI 전환 개발팀
 * @version 1.0 (2026-03-04, BE-CMN-003)
 */
@Data
public class Oif265Request {

    /** API Link Gateway 시스템 발급 일련번호 (선택) */
    @ApiModelProperty(value = "API Link Gateway 시스템이 발급한 일련번호", required = false)
    @JsonProperty("transactionid")
    private String transactionId;

    /** 시스템 내부 구간 순서 (선택) */
    @ApiModelProperty(value = "시스템 내부 구간 순서", required = false)
    @JsonProperty("sequenceno")
    private String sequenceNo;

    /** 요청 본문 (필수) */
    @ApiModelProperty(value = "OIF_265 request body", required = true)
    @JsonProperty("request")
    private RequestBody request;

    /**
     * OIF_265 request body inner object.
     * JSON 구조: { "request": { "enc_token_id": "..." } }
     */
    @Data
    public static class RequestBody {

        /**
         * 암호화된 토큰ID.
         * 공통로그인(IAMUI)에서 발급받은 enc_token_id.
         * AES128 암호화된 token_id 값.
         * 예: "7BE15E1E1BB36F7ED8E89E4D936B69FF142CC63750906F2465F769FB959CC0B"
         */
        @ApiModelProperty(
            value = "암호화된 토큰ID (IAMUI 공통로그인 발급)",
            example = "7BE15E1E1BB36F7ED8E89E4D936B69FF142CC63750906F2465F769FB959CC0B",
            required = false
        )
        @JsonProperty("enc_token_id")
        private String encTokenId;
    }

    // ── 정적 팩토리 메서드 ──────────────────────────────────────

    /**
     * encTokenId만으로 요청 객체를 생성하는 편의 메서드.
     *
     * @param encTokenId IAMUI에서 발급받은 암호화 토큰ID
     * @return Oif265Request
     */
    public static Oif265Request of(String encTokenId) {
        Oif265Request req = new Oif265Request();
        RequestBody body = new RequestBody();
        body.setEncTokenId(encTokenId);
        req.setRequest(body);
        return req;
    }
}
