package com.example.adsmetric.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class AdMetricOutputObject {
    private final Integer appId;
    private final String countryCode;
    private final Integer impressions;
    private final Integer clicks;
    private final Double revenue;
}
