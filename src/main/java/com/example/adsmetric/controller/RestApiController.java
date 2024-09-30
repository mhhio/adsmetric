package com.example.adsmetric.controller;

import com.example.adsmetric.model.BaseResponse;
import com.example.adsmetric.model.EntityResponse;
import com.example.adsmetric.service.AdMetricService;
import com.example.adsmetric.service.FileLoaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Impressions", description = "Impression management APIs")
public class RestApiController {
    private final FileLoaderService fileLoaderService;
    private final AdMetricService adMetricService;


    @Operation(summary = "Load impression data", description = "Upload a file containing impression data to be processed and loaded into the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/impressions/load")
    public ResponseEntity<BaseResponse> loadImpressions(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(BaseResponse.builder().status("failed").build());
            }

            fileLoaderService.loadImpressions(new String(file.getBytes()));
            return ResponseEntity.ok(BaseResponse.builder().status("success").build());
        } catch (Exception e) {
            //log error
            return ResponseEntity.badRequest().body(BaseResponse.builder().status("failed").build());
        }
    }

    @Operation(summary = "Load click events data", description = "Upload a file containing click events data to be processed and loaded into the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/click-events/load")
    public ResponseEntity<BaseResponse> loadClickEvents(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(BaseResponse.builder().status("failed").build());
            }

            fileLoaderService.loadClickEvents(new String(file.getBytes()));
            return ResponseEntity.ok(BaseResponse.builder().status("success").build());
        } catch (Exception e) {
            //log error
            return ResponseEntity.badRequest().body(BaseResponse.builder().status("failed").build());
        }
    }

    @Operation(summary = "Calculate metrics for some dimensions", description = "Calculate Count of impressions and Count of clicks and Sum of revenue metrics based on app_id and country_code dimensions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/analytic/calculate")
    public ResponseEntity<EntityResponse<?>> calculateMetrics() {
        try {
            return ResponseEntity.ok(EntityResponse.entityResponseBuilder().entity(adMetricService.calculate()).status("success").build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Calculate top advertisers", description = "Calculate top 5 advertiser with the highest rate of revenue/impressions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/analytic/top-advertiser")
    public ResponseEntity<EntityResponse<?>> topAdvertiser() {
        try {
            return ResponseEntity.ok(EntityResponse.entityResponseBuilder().entity(adMetricService.topAdvertisers()).status("sucess").build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
