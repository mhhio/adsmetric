package com.example.adsmetric.controller;

import com.example.adsmetric.model.BaseResponse;
import com.example.adsmetric.service.FileLoaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
}
