package com.cdtlab19.coffeeapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CoffeeDTO
 */
@Data
@NoArgsConstructor
public class CoffeeDTO {
    private String id;
    private String type;
    private String owner;

}