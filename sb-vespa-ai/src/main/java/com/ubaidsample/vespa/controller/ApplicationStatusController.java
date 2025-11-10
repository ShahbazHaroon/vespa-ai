package com.ubaidsample.vespa.controller;

import com.ubaidsample.vespa.dto.response.ApplicationStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/status")
@Tag(name = "ApplicationStatusController", description = "Operations related to Application Status")
public class ApplicationStatusController {

    @Value("${spring.application.name}")
    private String applicationName;

    @Operation(
            summary = "Fetch application status",
            description = "This endpoint retrieves the current status of the application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resource retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApplicationStatusResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping({"", "/"})
    public ApplicationStatusResponse getApplicationStatus() {
        log.info("ApplicationStatusController -> getApplicationStatus() called");
        return new ApplicationStatusResponse(applicationName, "Up and running");
    }

    @GetMapping("/health")
    public Mono<ResponseEntity<String>> health() {
        return Mono.just(ResponseEntity.ok("Service is up and running!"));
    }
}