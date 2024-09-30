package com.example.adsmetric.model;

import lombok.Data;

@Data
public class AdMetricOutputObject {
    private Integer appId;
    private String countryCode;
    private Integer impressions;
    private Integer clicks;
    private Double revenue;
}
