package com.example.mfds;

import com.example.mfds.dto.DrugEasyListResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class DrugInfoService {

    private final DrugInfoClient client;
    private final ObjectMapper om = new ObjectMapper();

    public DrugInfoService(DrugInfoClient client) {
        this.client = client;
    }

    public Mono<List<DrugSummary>> searchByName(String query, int page, int size) {
        return client.getEasyDrugList(query, page, size)
                .map(resp -> {
                    var items = normalizeItems(resp != null ? resp.body() : null);
                    return items.stream()
                            .map(i -> new DrugSummary(
                                    i.itemSeq(), i.itemName(), i.entpName(),
                                    i.efcyQesitm(), i.useMethodQesitm(), i.itemImage()
                            ))
                            .toList();
                });
    }

    public Mono<String> debugRaw(String q, int p, int s) {
        return client.getEasyDrugListRaw(q, p, s);
    }

    @SuppressWarnings("unchecked")
    private List<DrugEasyListResponse.Item> normalizeItems(DrugEasyListResponse.Body body) {
        if (body == null || body.items() == null) return List.of();
        Object any = body.items();

        if (any instanceof List<?> asList) {
            return asList.stream().map(e -> om.convertValue(e, DrugEasyListResponse.Item.class)).toList();
        }
        if (any instanceof Map<?, ?> map) {
            Object inner = map.get("item");
            if (inner == null) return List.of();
            if (inner instanceof List<?> list) {
                return list.stream().map(e -> om.convertValue(e, DrugEasyListResponse.Item.class)).toList();
            } else {
                return List.of(om.convertValue(inner, DrugEasyListResponse.Item.class));
            }
        }
        return List.of();
    }
    public record DrugSummary(
            String itemSeq, String itemName, String entpName,
            String efficacy, String howToUse, String itemImage
    ) {}
}
