package com.retail.csb.model.product;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.retail.csb.model.enumeration.ProductBarcodeType;
import com.retail.csb.model.enumeration.ProductExpiryPeriodType;
import com.retail.csb.model.enumeration.ProductTaxType;
import com.retail.csb.model.enumeration.ProductType;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@Entity
@Table(name = "products")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer businessId;

    private Integer createdBy;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    private String subUnitIds;

    private Integer tax;

    @Enumerated(EnumType.STRING)
    private ProductTaxType taxType;

    private Integer enableStock;

    private Float alertQuantity;

    private String sku;

    @Enumerated(EnumType.STRING)
    private ProductBarcodeType barcodeType;

    private Float expiryPeriod;

    @Enumerated(EnumType.STRING)
    private ProductExpiryPeriodType expiryPeriodType;

    private Integer enableSrNo;

    private String weight;

    private String productCustomField1;

    private String productCustomField2;

    private String productCustomField3;

    private String productCustomField4;

    private String image;

    private String productDescription;

    private Integer warrantyId;

    private Integer isInactive;

    private Integer notForSelling;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    @JoinColumn(name = "product_id")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Variation> variations;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Unit unit;

    @ManyToOne
    private Brand brand;
}
