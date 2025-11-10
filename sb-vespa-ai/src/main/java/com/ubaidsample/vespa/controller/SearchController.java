/*
 * @author Muhammad Ubaid Ur Raheem Ahmad AKA Shahbaz Haroon
 * Email: shahbazhrn@gmail.com
 * Cell: +923002585925
 * GitHub: https://github.com/ShahbazHaroon
 */

package com.ubaidsample.vespa.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ubaidsample.vespa.service.EmbeddingService;
import com.ubaidsample.vespa.service.VespaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SearchController {

    private final EmbeddingService embeddingService;
    private final VespaService vespaService;

    private final ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/search")
    public Mono<ResponseEntity<JsonNode>> search(@RequestParam String query) {
        return embeddingService.embed(query)
                .flatMap(embedding -> vespaService.query(query, embedding))
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Search failed for query='{}'", query, e);
                    ObjectNode err = mapper.createObjectNode();
                    err.put("error", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err));
                });
    }

    @PostMapping("/documents/{schema}/{docId}")
    public Mono<ResponseEntity<String>> feedDocument(
            @PathVariable String schema,
            @PathVariable String docId,
            @RequestBody String documentJson) {

        return vespaService.feedDocument(schema, docId, documentJson)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Feeding document failed for schema={} docId={}", schema, docId, e);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()));
                });
    }
}