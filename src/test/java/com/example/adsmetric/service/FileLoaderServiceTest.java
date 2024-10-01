package com.example.adsmetric.service;

import com.example.adsmetric.entity.ClickEventEntity;
import com.example.adsmetric.entity.ImpressionEntity;
import com.example.adsmetric.exeption.InvalidJsonException;
import com.example.adsmetric.repository.ClickEventRepository;
import com.example.adsmetric.repository.ImpressionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class FileLoaderServiceTest {
    @Mock
    private ImpressionRepository impressionRepository;

    @Mock
    private ClickEventRepository clickEventRepository;

    private ObjectMapper objectMapper;
    private FileLoaderService fileLoaderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        fileLoaderService = new FileLoaderService(impressionRepository, clickEventRepository, objectMapper);
    }

    @Test
    void testLoadImpressions_ValidJson() {
        String jsonContent = "[{\"id\":\"imp1\",\"app_id\":1,\"advertiser_id\":100,\"country_code\":\"US\"}," +
                "{\"id\":\"imp2\",\"app_id\":2,\"advertiser_id\":200,\"country_code\":\"UK\"}]";

        fileLoaderService.loadImpressions(jsonContent);

        verify(impressionRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testLoadImpressions_InvalidJson() {
        String invalidJson = "This is not a valid JSON";

        assertThrows(InvalidJsonException.class, () -> fileLoaderService.loadImpressions(invalidJson));
    }

    @Test
    void testLoadClickEvents_ValidJson() {
        String jsonContent = "[{\"impression_id\":\"imp1\",\"revenue\":10.5}," +
                "{\"impression_id\":\"imp2\",\"revenue\":20.0}]";

        fileLoaderService.loadClickEvents(jsonContent);

        verify(clickEventRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testLoadClickEvents_InvalidJson() {
        String invalidJson = "This is not a valid JSON";

        assertThrows(InvalidJsonException.class, () -> fileLoaderService.loadClickEvents(invalidJson));
    }

    @Test
    void testMapToImpression_ValidData() {
        String jsonContent = "[{\"id\":\"imp1\",\"app_id\":1,\"advertiser_id\":100,\"country_code\":\"US\"}]";
        List<ImpressionEntity> expectedList = new ArrayList<>();
        ImpressionEntity expectedObject = new ImpressionEntity();
        expectedObject.setId("imp1");
        expectedObject.setAppId(1);
        expectedObject.setCountryCode("US");
        expectedObject.setAdvertiserId(100);
        expectedList.add(expectedObject);
        when(impressionRepository.saveAll(anyList())).thenReturn(expectedList);
        List<ImpressionEntity> result = fileLoaderService.loadImpressions(jsonContent);

        assertEquals(1, result.size());
        ImpressionEntity impression = result.get(0);
        assertEquals("imp1", impression.getId());
        assertEquals(1, impression.getAppId());
        assertEquals(100, impression.getAdvertiserId());
        assertEquals("US", impression.getCountryCode());
    }

    @Test
    void testMapToImpression_InvalidData() {
        String jsonContent = "[{\"id\":\"imp1\",\"country_code\":\"USA\"}]";
        List<ImpressionEntity> result = fileLoaderService.loadImpressions(jsonContent);

        assertTrue(result.isEmpty());
    }

    @Test
    void testMapToClickEvent_ValidData() {
        String jsonContent = "[{\"impression_id\":\"imp1\",\"revenue\":10.5}]";
        List<ClickEventEntity> expectedList = new ArrayList<>();
        ClickEventEntity expectedObject = new ClickEventEntity();
        expectedObject.setRevenue(10.5d);
        expectedObject.setImpressionId("imp1");
        expectedList.add(expectedObject);
        when(clickEventRepository.saveAll(anyList())).thenReturn(expectedList);
        List<ClickEventEntity> result = fileLoaderService.loadClickEvents(jsonContent);

        assertEquals(1, result.size());
        ClickEventEntity clickEvent = result.get(0);
        assertEquals("imp1", clickEvent.getImpressionId());
        assertEquals(10.5, clickEvent.getRevenue());
    }

    @Test
    void testMapToClickEvent_InvalidData() {
        String jsonContent = "[{\"impression_id\":\"imp1\"}]";
        List<ClickEventEntity> result = fileLoaderService.loadClickEvents(jsonContent);

        assertTrue(result.isEmpty());
    }

}