/*
 * @author Muhammad Ubaid Ur Raheem Ahmad AKA Shahbaz Haroon
 * Email: shahbazhrn@gmail.com
 * Cell: +923002585925
 * GitHub: https://github.com/ShahbazHaroon
 */

package com.ubaidsample.vespa.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class VespaService {

    private final WebClient webClient;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${vespa.default.hits}")
    private int defaultHits;

    public Mono<JsonNode> query(String query, double[] embedding) {
        ObjectNode root = mapper.createObjectNode();
        root.put("yql", "select * from sources * where ([{\"targetHits\":" + defaultHits +
                "}]nearestNeighbor(embedding, q_emb)) or content contains \"" + escapeQuery(query) + "\";");
        root.put("hits", defaultHits);

        ObjectNode props = root.putObject("ranking.features.query");
        ArrayNode arr = props.putArray("q_emb");
        for (double d : embedding) arr.add(d);

        return webClient.post()
                .uri("/search/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(root)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnNext(resp -> log.info("Vespa query successful for '{}'", query))
                .doOnError(e -> log.error("Vespa query failed for '{}'", query, e));
    }

    public Mono<String> feedDocument(String schema, String docId, String documentJson) {
        return webClient.post()
                .uri("/document/v1/{schema}/docid/{docId}/", schema, docId)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(documentJson)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("Failed to feed document. Status={}, Body={}", response.statusCode(), errorBody);
                                    return Mono.error(new RuntimeException("Failed to feed document: " + errorBody));
                                })
                )
                .bodyToMono(String.class)
                .doOnNext(resp -> log.info("Successfully fed document to schema={} docId={}", schema, docId))
                .doOnError(e -> log.error("Error feeding document to schema={} docId={}", schema, docId, e));
    }

    private String escapeQuery(String query) {
        return query.replace("\"", "\\\"");
    }
}