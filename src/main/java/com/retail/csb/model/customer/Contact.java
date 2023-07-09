package com.retail.csb.model.customer;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@Entity
@Table(name = "contacts")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer businessId;

    private Integer createdBy;

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

    private Integer totalRp;

    private Integer totalRpUsed;

    private Integer totalRpExpired;

    private Integer isDefault;

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

    // @OneToOne(mappedBy = "contact", cascade = CascadeType.ALL)
    // private CustomerAuth customerAuth;
}
