package com.retail.csb.model.product;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@Entity
@Table(name = "product_variations")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ProductVariation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Integer isDummy;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    // @OneToMany(mappedBy = "productId")
    // private Set<Product> productId = new HashSet<>();

    // @ManyToOne
    // @JsonIgnoreProperties(value = "variationIds", allowSetters = true)
    // private SellingPriceGroups sellingPriceGroups;

    @ManyToOne
    @JsonIgnoreProperties(value = "productVariationId", allowSetters = true)
    private Variation variation;

}
