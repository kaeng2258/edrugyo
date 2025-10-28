package com.example.mfds.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DrugEasyListResponse(
        Header header,
        Body body
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Header(String resultCode, String resultMsg) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Body(
            Integer pageNo,
            Integer totalCount,
            Integer numOfRows,
            @JsonProperty("items") Object items  // 배열/객체 모두 수용
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record ItemsWrapper(
            @JsonProperty("item") List<Item> item
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Item(
            String itemSeq, String itemName, String entpName,
            String efcyQesitm, String useMethodQesitm,
            String atpnWarnQesitm, String atpnQesitm,
            String intrcQesitm, String seQesitm, String depositMethodQesitm,
            String openDe, String updateDe,
            String itemImage, String bizrno
    ) {}
}
