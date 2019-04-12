package com.cdtlab19.coffeeapi.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * UseCoffeeDTO
 */
@Data
@NoArgsConstructor
public class UseCoffeeDTO {
    private String coffeeId;
    private String userId;

    public String getCoffeeId() {
        return coffeeId;
    }

    public String getUserId() {
        return userId;
    }
}
