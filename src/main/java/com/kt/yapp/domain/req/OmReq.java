package com.kt.yapp.domain.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

/*
{
        "caVal1": "CMP000020237",
        "caVal2": "혜택토스트팝업",
        "caVal3": "OFFERAREA188",
        "caVal4": "BNR000020936",
        "html": "{\n  \"BG_COLOR\" : \"#3A3A3A\"\n , \"URL_NM\" : \"https://m.my.kt.com/product/s_MobilePriceView.do\"\n , \"IMAGE_FILE_NAME\": \"https://tb.offer.onmas.kt.com/images/omscms_20230426151204.png\"\n}",
        "bnStDt": "20230426000000",
        "bnFnsDt": "20230531235900",
        "statCd": "^ONMAS_고객센터^APP_혜택_토스트팝업^CMP000020237",
        "hashtagId1": null,
        "hashtagId2": null,
        "hashtagId3": null
    }
* */

/**
 * 온라인마켓팅(온마시) 배너정보
 */
@Setter @Getter
public class OmReq {

    private String caVal1;//statCd
    private String caVal2;//배너명
    private String caVal3;//zoneCode
    private String caVal4;
    private String html;
    private String bnStDt;
    private String bnFnsDt;
    private String statCd;
    private String hashtagId1;
    private String hashtagId2;
    private String hashtagId3;

}
