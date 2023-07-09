package com.retail.csb.model.vm.product;

import java.util.Set;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VariationVm {
    private Integer id;
    private String name;
    private Float defaultSellPrice;
    private Float sellPriceIncTax;
    private String comboVariations;
    private Set<ProductVariationVm> variations;
}
