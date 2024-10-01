package com.example.adsmetric.service;
import com.example.adsmetric.entity.ClickEventEntity;
import com.example.adsmetric.entity.ImpressionEntity;
import com.example.adsmetric.model.AdMetricOutputObject;
import com.example.adsmetric.model.TopAdvertiserOutputObject;
import com.example.adsmetric.repository.ClickEventRepository;
import com.example.adsmetric.repository.ImpressionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdMetricServiceTest {

    @Mock
    private ImpressionRepository impressionRepository;

    @Mock
    private ClickEventRepository clickEventRepository;

    private AdMetricService adMetricService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adMetricService = new AdMetricService(impressionRepository, clickEventRepository);
    }

    @Test
    void testCalculate() {

        ImpressionEntity impression1 = new ImpressionEntity();
        impression1.setId("imp1");
        impression1.setAppId(1);
        impression1.setCountryCode("US");
        impression1.setAdvertiserId(100);

        ImpressionEntity impression2 = new ImpressionEntity();
        impression2.setId("imp2");
        impression2.setAppId(1);
        impression2.setCountryCode("US");
        impression2.setAdvertiserId(200);

        ClickEventEntity clickEvent1 = new ClickEventEntity();
        clickEvent1.setImpressionId("imp1");
        clickEvent1.setRevenue(10.0);

        ClickEventEntity clickEvent2 = new ClickEventEntity();
        clickEvent2.setImpressionId("imp2");
        clickEvent2.setRevenue(20.0);

        when(impressionRepository.findAll()).thenReturn(Arrays.asList(impression1, impression2));
        when(clickEventRepository.findAll()).thenReturn(Arrays.asList(clickEvent1, clickEvent2));


        List<AdMetricOutputObject> result = adMetricService.calculate();


        assertEquals(1, result.size());
        AdMetricOutputObject output = result.get(0);
        assertEquals(1, output.getAppId());
        assertEquals("US", output.getCountryCode());
        assertEquals(30.0, output.getRevenue());
        assertEquals(2, output.getClicks());

        verify(impressionRepository, times(1)).findAll();
        verify(clickEventRepository, times(1)).findAll();
    }

    @Test
    void testTopAdvertisers() {
        ImpressionEntity impression1 = new ImpressionEntity();
        impression1.setId("imp1");
        impression1.setAppId(1);
        impression1.setCountryCode("US");
        impression1.setAdvertiserId(100);

        ImpressionEntity impression2 = new ImpressionEntity();
        impression2.setId("imp2");
        impression2.setAppId(1);
        impression2.setCountryCode("US");
        impression2.setAdvertiserId(200);

        ImpressionEntity impression3 = new ImpressionEntity();
        impression3.setId("imp3");
        impression3.setAppId(1);
        impression3.setCountryCode("US");
        impression3.setAdvertiserId(300);

        ClickEventEntity clickEvent1 = new ClickEventEntity();
        clickEvent1.setImpressionId("imp1");
        clickEvent1.setRevenue(10.0);

        ClickEventEntity clickEvent2 = new ClickEventEntity();
        clickEvent2.setImpressionId("imp2");
        clickEvent2.setRevenue(20.0);

        ClickEventEntity clickEvent3 = new ClickEventEntity();
        clickEvent3.setImpressionId("imp3");
        clickEvent3.setRevenue(30.0);

        when(impressionRepository.findAll()).thenReturn(Arrays.asList(impression1, impression2, impression3));
        when(clickEventRepository.findAll()).thenReturn(Arrays.asList(clickEvent1, clickEvent2, clickEvent3));


        List<TopAdvertiserOutputObject> result = adMetricService.topAdvertisers();


        assertEquals(1, result.size());
        TopAdvertiserOutputObject output = result.get(0);
        assertEquals(1, output.getAppId());
        assertEquals("US", output.getCountryCode());
        assertEquals(3, output.getRecommendedAdvertiserIds().size());
        assertEquals(Arrays.asList(300, 200, 100), output.getRecommendedAdvertiserIds());

        verify(impressionRepository, times(1)).findAll();
        verify(clickEventRepository, times(1)).findAll();
    }

    @Test
    void testCalculateWithNoData() {

        when(impressionRepository.findAll()).thenReturn(List.of());
        when(clickEventRepository.findAll()).thenReturn(List.of());


        List<AdMetricOutputObject> result = adMetricService.calculate();


        assertTrue(result.isEmpty());

        verify(impressionRepository, times(1)).findAll();
        verify(clickEventRepository, times(1)).findAll();
    }

    @Test
    void testTopAdvertisersWithNoData() {

        when(impressionRepository.findAll()).thenReturn(List.of());
        when(clickEventRepository.findAll()).thenReturn(List.of());


        List<TopAdvertiserOutputObject> result = adMetricService.topAdvertisers();


        assertTrue(result.isEmpty());

        verify(impressionRepository, times(1)).findAll();
        verify(clickEventRepository, times(1)).findAll();
    }

    @Test
    void testCalculateWithMismatchedData() {

        ImpressionEntity impression = new ImpressionEntity();
        impression.setId("imp1");
        impression.setAppId(1);
        impression.setCountryCode("US");
        impression.setAdvertiserId(100);

        ClickEventEntity clickEvent = new ClickEventEntity();
        clickEvent.setImpressionId("imp2");
        clickEvent.setRevenue(10.0);

        when(impressionRepository.findAll()).thenReturn(List.of(impression));
        when(clickEventRepository.findAll()).thenReturn(List.of(clickEvent));


        List<AdMetricOutputObject> result = adMetricService.calculate();


        assertEquals(1, result.size());
        AdMetricOutputObject output = result.get(0);
        assertEquals(1, output.getAppId());
        assertEquals("US", output.getCountryCode());
        assertEquals(0.0, output.getRevenue());
        assertEquals(0, output.getClicks());

        verify(impressionRepository, times(1)).findAll();
        verify(clickEventRepository, times(1)).findAll();
    }
}