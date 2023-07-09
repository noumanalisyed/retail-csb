package com.retail.csb.model.vm;

import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Checkout {

    private float orderTotal;
    private int orderItems;

    private Map<String, CheckoutItem> orders;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CheckoutItem {
        private int productId;
        private int quantity;
        private int tax;
        private double total;
        private int variationId;
        private int productVariationId;
        private Float unitPriceIncTax;
    }
}
