package com.retail.csb.model.product;

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
@Table(name = "units")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String actualName;

    private String shortName;

    private Integer allowFloat;

    private Integer baseUnitId;

    private Float baseUnitMultiplier;

    private Timestamp deletedAt;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}
