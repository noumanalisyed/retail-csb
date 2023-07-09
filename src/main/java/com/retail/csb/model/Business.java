package com.retail.csb.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "business")
@JsonIgnoreProperties(ignoreUnknown = true, value = { "apiKey" }, allowSetters = true)
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private LocalDate startLocalDate;

    private String taxNumber1;

    private String taxLabel1;

    private String taxNumber2;

    private String taxLabel2;

    private Double defaultProfitPercent;

    private Integer ownerId;

    private String timeZone;

    private Integer fyStartMonth;

    // @Enumerated(EnumType.STRING)
    // private BusinessAccountingMethod accountingMethod;

    private Float defaultSalesDiscount;

    // @Enumerated(EnumType.STRING)
    // private BusinessSellPriceTax sellPriceTax;

    private String logo;

    private String skuPrefix;

    private Integer enableProductExpiry;

    // @Enumerated(EnumType.STRING)
    // private BusinessExpiryType expiryType;

    // @Enumerated(EnumType.STRING)
    // private BusinessOnProductExpiry onProductExpiry;

    private Integer stopSellingBefore;

    private Integer enableTooltip;

    private Integer purchaseInDiffCurrency;

    private Integer purchaseCurrencyId;

    private Float pExchangeRate;

    private Integer transactionEditDays;

    private Integer stockExpiryAlertDays;

    private String keyboardShortcuts;

    private String posSettings;

    private String weighingScaleSetting;

    private Integer enableBrand;

    private Integer enableCategory;

    private Integer enableSubCategory;

    private Integer enablePriceTax;

    private Integer enablePurchaseStatus;

    private Integer enableLotNumber;

    private Integer defaultUnit;

    private Integer enableSubUnits;

    private Integer enableRacks;

    private Integer enableRow;

    private Integer enablePosition;

    private Integer enableEditingProductFromPurchase;

    @Column(unique = true)
    private String apiKey;
}
