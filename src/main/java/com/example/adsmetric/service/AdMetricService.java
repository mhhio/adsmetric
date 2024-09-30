package com.example.adsmetric.service;

import com.example.adsmetric.dto.AdvertiserImpactRatioDto;
import com.example.adsmetric.dto.AppCountryKey;
import com.example.adsmetric.dto.RevenueClickDto;
import com.example.adsmetric.entity.ClickEventEntity;
import com.example.adsmetric.entity.ImpressionEntity;
import com.example.adsmetric.model.AdMetricOutputObject;
import com.example.adsmetric.model.TopAdvertiserOutputObject;
import com.example.adsmetric.repository.ClickEventRepository;
import com.example.adsmetric.repository.ImpressionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdMetricService {
    private final ImpressionRepository impressionRepository;
    private final ClickEventRepository clickEventRepository;


    public List<AdMetricOutputObject> calculate() {
        List<ImpressionEntity> impressions = impressionRepository.findAll();
        List<ClickEventEntity> clickEvents = clickEventRepository.findAll();

        Map<String, RevenueClickDto> revenueClickMap = calculateRevenueAndClicks(clickEvents);
        Map<AppCountryKey, List<String>> dimensionToImpressionMap = groupImpressionsByAppAndCountry(impressions);

        return dimensionToImpressionMap.entrySet().stream()
                .map(entry -> createAdMetricOutputObject(entry.getKey(), entry.getValue(), revenueClickMap))
                .collect(Collectors.toList());
    }

    private Map<String, RevenueClickDto> calculateRevenueAndClicks(List<ClickEventEntity> clickEvents) {
        return clickEvents.stream()
                .collect(Collectors.groupingBy(ClickEventEntity::getImpressionId,
                        Collectors.collectingAndThen(Collectors.toList(),
                                list -> new RevenueClickDto(
                                        list.stream().mapToDouble(ClickEventEntity::getRevenue).sum(),
                                        list.size()
                                )
                        )
                ));
    }

    private Map<AppCountryKey, List<String>> groupImpressionsByAppAndCountry(List<ImpressionEntity> impressions) {
        return impressions.stream()
                .collect(Collectors.groupingBy(
                        impression -> new AppCountryKey(impression.getAppId(), impression.getCountryCode()),
                        Collectors.mapping(ImpressionEntity::getId, Collectors.toList())
                ));
    }

    private AdMetricOutputObject createAdMetricOutputObject(AppCountryKey key, List<String> impressionIds, Map<String, RevenueClickDto> revenueClickMap) {
        double revenue = impressionIds.stream()
                .filter(revenueClickMap::containsKey)
                .mapToDouble(id -> revenueClickMap.get(id).revenue())
                .sum();

        int clicks = impressionIds.stream()
                .filter(revenueClickMap::containsKey)
                .mapToInt(id -> revenueClickMap.get(id).clicks())
                .sum();

        return AdMetricOutputObject.builder()
                .appId(key.appId())
                .countryCode(key.countryCode())
                .revenue(revenue)
                .clicks(clicks)
                .build();
    }

    public List<TopAdvertiserOutputObject> topAdvertisers() {
        List<ImpressionEntity> impressions = impressionRepository.findAll();
        List<ClickEventEntity> clickEvents = clickEventRepository.findAll();

        Map<String, Double> revenueMap = calculateRevenueMap(clickEvents);
        Map<Integer, List<String>> advertiserImpressionMap = groupImpressionsByAdvertiser(impressions);
        Map<Integer, Double> advertiserRevenueMap = calculateAdvertiserRevenue(advertiserImpressionMap, revenueMap);

        Map<AppCountryKey, List<AdvertiserImpactRatioDto>> advertiserMetricsMap =
                calculateAdvertiserMetrics(impressions, advertiserRevenueMap, advertiserImpressionMap);

        return createTopAdvertiserOutputObjects(advertiserMetricsMap);
    }

    private Map<String, Double> calculateRevenueMap(List<ClickEventEntity> clickEvents) {
        return clickEvents.stream()
                .collect(Collectors.groupingBy(
                        ClickEventEntity::getImpressionId,
                        Collectors.summingDouble(ClickEventEntity::getRevenue)
                ));
    }

    private Map<Integer, List<String>> groupImpressionsByAdvertiser(List<ImpressionEntity> impressions) {
        return impressions.stream()
                .collect(Collectors.groupingBy(
                        ImpressionEntity::getAdvertiserId,
                        Collectors.mapping(ImpressionEntity::getId, Collectors.toList())
                ));
    }

    private Map<Integer, Double> calculateAdvertiserRevenue(
            Map<Integer, List<String>> advertiserImpressionMap,
            Map<String, Double> revenueMap) {
        return advertiserImpressionMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .mapToDouble(impressionId -> revenueMap.getOrDefault(impressionId, 0.0))
                                .sum()
                ));
    }

    private Map<AppCountryKey, List<AdvertiserImpactRatioDto>> calculateAdvertiserMetrics(
            List<ImpressionEntity> impressions,
            Map<Integer, Double> advertiserRevenueMap,
            Map<Integer, List<String>> advertiserImpressionMap) {
        return impressions.stream()
                .collect(Collectors.groupingBy(
                        impression -> new AppCountryKey(impression.getAppId(), impression.getCountryCode()),
                        Collectors.mapping(
                                impression -> createAdvertiserImpactRatioDto(
                                        impression.getAdvertiserId(),
                                        advertiserRevenueMap,
                                        advertiserImpressionMap
                                ),
                                Collectors.filtering(Optional::isPresent, Collectors.mapping(Optional::get, Collectors.toList()))
                        )
                ));
    }

    private Optional<AdvertiserImpactRatioDto> createAdvertiserImpactRatioDto(
            Integer advertiserId,
            Map<Integer, Double> advertiserRevenueMap,
            Map<Integer, List<String>> advertiserImpressionMap) {
        return Optional.ofNullable(advertiserRevenueMap.get(advertiserId))
                .map(revenue -> {
                    int impressionCount = advertiserImpressionMap.getOrDefault(advertiserId, Collections.emptyList()).size();
                    return impressionCount > 0
                            ? new AdvertiserImpactRatioDto(advertiserId, revenue / impressionCount)
                            : null;
                });
    }

    private List<TopAdvertiserOutputObject> createTopAdvertiserOutputObjects(
            Map<AppCountryKey, List<AdvertiserImpactRatioDto>> advertiserMetricsMap) {
        return advertiserMetricsMap.entrySet().stream()
                .map(entry -> {
                    List<Integer> topAdvertisers = entry.getValue().stream()
                            .sorted(Comparator.comparing(AdvertiserImpactRatioDto::ratio).reversed())
                            .limit(5)
                            .map(AdvertiserImpactRatioDto::advertiserId)
                            .collect(Collectors.toList());

                    return new TopAdvertiserOutputObject(
                            entry.getKey().appId(),
                            entry.getKey().countryCode(),
                            topAdvertisers
                    );
                })
                .collect(Collectors.toList());
    }

}
