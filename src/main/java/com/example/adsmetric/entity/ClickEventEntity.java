package com.example.adsmetric.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "click_event", indexes = {
        @Index(name = "idx_impression", columnList = "impressionId")
})
@Getter
@Setter
public class ClickEventEntity {
    @Id
    @GeneratedValue
    private Integer id;

    private String impressionId;

    private Double revenue;

}
