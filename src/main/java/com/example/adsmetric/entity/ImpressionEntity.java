package com.example.adsmetric.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "impressions",indexes = {
        @Index(name = "idx_app_country", columnList = "appId,countryCode")
})
@Getter
@Setter
public class ImpressionEntity {
    @Id
    private String id;
    private Integer appId;
    private Integer advertiserId;
    private String countryCode;
}