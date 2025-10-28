package com.example.mfds;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/drugs")
@Validated
public class DrugInfoController {

    private final DrugInfoService service;

    public DrugInfoController(DrugInfoService service) {
        this.service = service;
    }

    @GetMapping("/search")
    public Mono<List<DrugInfoService.DrugSummary>> search(
            @RequestParam @NotBlank String query,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        return service.searchByName(query, page, size);
    }

    @GetMapping("/debug-raw")
    public Mono<String> raw(
            @RequestParam @NotBlank String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.debugRaw(query, page, size);
    }

    @GetMapping("/ping")
    public String ping() { return "ok"; }
}
