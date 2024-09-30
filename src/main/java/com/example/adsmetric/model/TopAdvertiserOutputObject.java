package com.example.adsmetric.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Builder
@Getter
@AllArgsConstructor
public class TopAdvertiserOutputObject {
    private final Integer appId;
    private final String countryCode;
    private final List<Integer> recommendedAdvertiserIds;
}
