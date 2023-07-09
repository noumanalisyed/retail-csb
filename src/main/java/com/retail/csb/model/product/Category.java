package com.retail.csb.model.product;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.retail.csb.model.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@Entity
@Table(name = "categories")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer businessId;

    private String name;

    private String shortCode;

    private Integer parentId;

    private String categoryType;

    private String description;

    private String slug;

    private Timestamp deletedAt;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    @ManyToOne
    private User createdBy;
}
