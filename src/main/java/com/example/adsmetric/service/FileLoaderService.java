package com.example.adsmetric.service;

import com.example.adsmetric.entity.ClickEventEntity;
import com.example.adsmetric.entity.ImpressionEntity;
import com.example.adsmetric.exeption.InvalidJsonException;
import com.example.adsmetric.repository.ClickEventRepository;
import com.example.adsmetric.repository.ImpressionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.adsmetric.util.Utils.*;

@Service
@RequiredArgsConstructor
public class FileLoaderService {
    private final ImpressionRepository impressionRepository;
    private final ClickEventRepository clickEventRepository;
    private final ObjectMapper objectMapper;

    public void loadImpressions(String jsonContent) {
        try {
            List<Map<String, Object>> rawData = objectMapper.readValue(jsonContent, new TypeReference<List<Map<String, Object>>>() {
            });
            List<ImpressionEntity> impressionEntityList = rawData.stream().map(this::mapToImpression)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
            impressionRepository.saveAll(impressionEntityList);
        } catch (JsonProcessingException e) {
            throw new InvalidJsonException(e);
        }
    }

    public void loadClickEvents(String jsonContent) {
        try {
            List<Map<String, Object>> rawData = objectMapper.readValue(jsonContent, new TypeReference<List<Map<String, Object>>>() {
            });
            List<ClickEventEntity> clickEventsEntityList = rawData.stream().map(this::mapToClickEvent)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
            clickEventRepository.saveAll(clickEventsEntityList);
        } catch (JsonProcessingException e) {
            throw new InvalidJsonException(e);
        }
    }

    private Optional<ClickEventEntity> mapToClickEvent(Map<String, Object> rawItem) {
        try {
            String impressionId = getStringValue(rawItem, "impression_id");
            Double revenue = getDoubleValue(rawItem, "revenue");
            if(impressionId==null || revenue==null) {
                return Optional.empty();
            }
            ClickEventEntity clickEventEntity = new ClickEventEntity();
            clickEventEntity.setImpressionId(impressionId);
            clickEventEntity.setRevenue(revenue);
            return Optional.of(clickEventEntity);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<ImpressionEntity> mapToImpression(Map<String, Object> rawItem) {
        try {
            String id = getStringValue(rawItem, "id");
            Integer appId = getIntegerValue(rawItem, "app_id");
            Integer advertiserId = getIntegerValue(rawItem, "advertiser_id");
            String countryCode = getStringValue(rawItem, "country_code");
            if ((countryCode != null) && (countryCode.length() > 2)) {
                countryCode = null;
            }
            // If any of the required fields are missing, return an empty Optional
            if (id == null) {
                return Optional.empty();
            }

            if (appId == null && countryCode == null && advertiserId == null) {
                return Optional.empty();
            }

            ImpressionEntity impressionEntity = new ImpressionEntity();
            impressionEntity.setId(id);
            impressionEntity.setAppId(appId);
            impressionEntity.setAdvertiserId(advertiserId);
            impressionEntity.setCountryCode(countryCode); // This can be null

            return Optional.of(impressionEntity);
        } catch (Exception e) {
            // Log the error if needed
            // logger.error("Error mapping data: " + e.getMessage(), e);
            return Optional.empty();
        }
    }
}
