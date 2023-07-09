package com.retail.csb.model.cart;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@Entity
@Table(name = "order_requests")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderTrackingId;

    private Long customerId;

    private String instructions;
    private String address;
    private String orderSource;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.RECEIVED;
    private String orderConfirmationPin;

    private String reviewedBy;

    private Float cartTotal;
    private Integer cartQuantity;

    private String rejectionRemarks;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    @Column(columnDefinition = "TEXT")
    private String cart; // * Holds the json form of cart
}
