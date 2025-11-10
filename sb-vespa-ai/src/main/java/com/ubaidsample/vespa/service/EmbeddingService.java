/*
 * @author Muhammad Ubaid Ur Raheem Ahmad AKA Shahbaz Haroon
 * Email: shahbazhrn@gmail.com
 * Cell: +923002585925
 * GitHub: https://github.com/ShahbazHaroon
 */

package com.ubaidsample.vespa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingService {

    //private static final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");

    @Value("${vespa.embedding.dim}")
    private int embeddingDim;

    public Mono<double[]> embed(String text) {
        return Mono.fromSupplier(() -> {
            double[] vec = new double[embeddingDim];
            for (int i = 0; i < embeddingDim; i++) {
                vec[i] = i < text.length() ? (text.charAt(i) % 255) / 255.0 : 0.0;
            }
            return vec;
        }).subscribeOn(Schedulers.boundedElastic()); // offload CPU work
    }
}