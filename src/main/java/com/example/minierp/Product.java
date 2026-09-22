package com.example.minierp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @PositiveOrZero
    private BigDecimal itemPrice;

    private String itemUnitOfMeasurement;
    private Double itemStock;

    public Product() {
    }

    public Product(String itemName, BigDecimal itemPrice, String itemUnitOfMeasurement, Double itemStock) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemUnitOfMeasurement = itemUnitOfMeasurement;
        this.itemStock = itemStock;
    }

    public Long getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getItemPrice(){
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice){
        this.itemPrice = itemPrice;
    }

    public String getItemUnitOfMeasurement() {
        return itemUnitOfMeasurement;
    }

    public void setItemUnitOfMeasurement(String itemUnitOfMeasurement) {
        this.itemUnitOfMeasurement = itemUnitOfMeasurement;
    }

    public Double getItemStock() {
        return itemStock;
    }

    public void setItemStock(Double itemStock) {
        this.itemStock = itemStock;
    }
}
