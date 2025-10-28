package com.example.mfds;

import com.example.mfds.dto.DrugEasyListResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class DrugInfoClient {

    private final WebClient webClient;
    private final MfdsProperties props;

    public DrugInfoClient(WebClient mfdsWebClient, MfdsProperties props) {
        this.webClient = mfdsWebClient;
        this.props = props;
    }

    public Mono<DrugEasyListResponse> getEasyDrugList(String query, int page, int size) {
        final String key = props.serviceKey() == null ? "" : props.serviceKey().trim();
        return webClient.get()
                .uri(u -> u.path("/getDrbEasyDrugList")
                        .queryParam("serviceKey", key)
                        .queryParam("type", "json")
                        .queryParam("pageNo", page)
                        .queryParam("numOfRows", size)
                        .queryParam("itemName", query)
                        .build(true))   // 이미 인코딩된 키 재인코딩 방지
                .retrieve()
                .bodyToMono(DrugEasyListResponse.class);
    }

    public Mono<String> getEasyDrugListRaw(String query, int page, int size) {
        final String key = props.serviceKey() == null ? "" : props.serviceKey().trim();
        return webClient.get()
                .uri(u -> u.path("/getDrbEasyDrugList")
                        .queryParam("serviceKey", key)
                        .queryParam("type", "json")
                        .queryParam("pageNo", page)
                        .queryParam("numOfRows", size)
                        .queryParam("itemName", query)
                        .build(true))
                .retrieve()
                .bodyToMono(String.class);
    }
}
