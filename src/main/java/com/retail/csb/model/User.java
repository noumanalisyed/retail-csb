package com.retail.csb.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.retail.csb.constant.Constants;
import com.retail.csb.model.enumeration.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@JsonIgnoreProperties(ignoreUnknown = true, value = { "password" }, allowSetters = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(name = "username", length = 50, unique = true, nullable = false)
    private String username;

    /**
     * Refer to {@code @JsonIgnoreProperties(ignoreUnknown = true, value = {
     * "password" }, allowSetters = true)} Password field will take part in JSON
     * deserialization but not in serialization. It will allow for the password
     * field on creation and disable when fetching the user object for view purposes
     */
    @NotNull
    @Size(min = 8, max = 60)
    @Column(name = "password", length = 60, nullable = false)
    private String password;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email;

    @Size(max = 8)
    private String userType;

    private String address;
    private String bankDetails;
    private String bloodGroup;
    private BigDecimal cmmsnPercent = new BigDecimal(0.0); // * Default commission
    private String contactNo;
    private String contactNumber;
    private Timestamp createdAt;
    private String currentAddress;
    private String customField1;
    private String customField2;
    private String customField3;
    private String customField4;
    private Timestamp deletedAt;
    private Date dob;
    private String fbLink;
    private String firstName;
    private String gender;
    private String guardianName;
    private String idProofName;
    private String idProofNumber;
    private byte isCmmsnAgnt;
    private String language = "en"; // * Default language English
    private String lastName;
    private String maritalStatus;
    private String permanentAddress;
    private String rememberToken;
    private byte selectedContacts;
    private String socialMedia1;
    private String socialMedia2;

    @Enumerated(EnumType.STRING)
    private UserStatus status;
    private String surname;
    private String twitterLink;
    private Timestamp updatedAt;

    @ManyToOne
    private Business business;
}
