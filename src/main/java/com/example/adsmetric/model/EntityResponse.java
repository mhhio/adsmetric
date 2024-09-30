package com.example.adsmetric.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class EntityResponse<T> extends BaseResponse {
    private final T entity;

    @Builder(builderMethodName = "entityResponseBuilder")
    public EntityResponse(T entity,String status) {
        super(status);
        this.entity = entity;
    }
}
