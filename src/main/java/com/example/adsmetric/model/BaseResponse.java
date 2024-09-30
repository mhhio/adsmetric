package com.example.adsmetric.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
@Getter
@AllArgsConstructor
@Builder
public class BaseResponse {
    private final String status;
}
