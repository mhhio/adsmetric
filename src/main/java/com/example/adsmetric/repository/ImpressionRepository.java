package com.example.adsmetric.repository;

import com.example.adsmetric.entity.ImpressionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImpressionRepository extends JpaRepository<ImpressionEntity, String> {
    @Query("SELECT i FROM ImpressionEntity i WHERE i.appId = ?1 AND i.countryCode=?2")
    List<ImpressionEntity> findByAppIdAndCountryCode(Integer appId, String countryCode);
}
