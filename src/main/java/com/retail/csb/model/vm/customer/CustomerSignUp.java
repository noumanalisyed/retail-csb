package com.retail.csb.model.vm.customer;

import java.sql.Date;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CustomerSignUp {

    private Integer businessId;

    private String type;

    private String supplierBusinessName;

    private String name;

    private String prefix;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String contactId;

    private String contactStatus;

    private String taxNumber;

    private String city;

    private String state;

    private String country;

    private String addressLine1;

    private String addressLine2;

    private String landmark;

    private String zipCode;

    private Date dob;

    private String mobile;

    private String landline;

    private String alternateNumber;

    private Integer payTermNumber;

    private Float creditLimit;

    private Integer totalRp = 0;

    private Integer totalRpUsed = 0;

    private Integer totalRpExpired = 0;

    private Integer isDefault = 0;

    private String shippingAddress;

    private String position;

    private Integer customerGroupId;

    private String customField1;

    private String customField2;

    private String customField3;

    private String customField4;

    private Timestamp deletedAt;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    // * Auth information
    private String username;
    private String password;
}
