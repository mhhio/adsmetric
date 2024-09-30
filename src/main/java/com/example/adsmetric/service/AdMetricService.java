package com.example.adsmetric.service;

import com.example.adsmetric.entity.ClickEventEntity;
import com.example.adsmetric.entity.ImpressionEntity;
import com.example.adsmetric.model.AdMetricInputObject;
import com.example.adsmetric.model.AdMetricOutputObject;
import com.example.adsmetric.repository.ClickEventRepository;
import com.example.adsmetric.repository.ImpressionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdMetricService {
    private final ImpressionRepository impressionRepository;
    private final ClickEventRepository clickEventRepository;


    public List<AdMetricOutputObject> calculateRevenue(AdMetricInputObject inputObject) {
        List<ImpressionEntity> impressionEntityList = impressionRepository.findAll();
        List<ClickEventEntity> clickEventEntityList = clickEventRepository.findAll();
        Map<String, ClickEventEntity> clickEventEntityMap = new HashMap<>();

        return null;
    }

}
