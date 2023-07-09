package com.retail.csb.model.vm.product;

import java.util.Set;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ProductVm {
    private Long id;
    private String name;
    private String image;
    private String productDescription;
    private CategoryVm category;
    private Set<VariationVm> variations;
    private UnitVm unit;
    private BrandVm brand;
}
