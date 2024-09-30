package com.example.adsmetric.repository;

import com.example.adsmetric.entity.ClickEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClickEventRepository extends JpaRepository<ClickEventEntity, Integer> {
}
