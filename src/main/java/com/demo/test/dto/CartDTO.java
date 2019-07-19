package com.demo.test.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartDTO {

    private String productId;

    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
