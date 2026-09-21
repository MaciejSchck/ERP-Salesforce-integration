package com.example.minierp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @NotBlank
    private String itemName;
    @NotBlank
    private String itemPrice;
    private String itemUnitOfMeasurement;
    private String itemQuantity;

    public Product() {
    }

    public Product(String itemName, String itemPrice, String itemUnitOfMeasurement, String itemQuantity) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemUnitOfMeasurement = itemUnitOfMeasurement;
        this.itemQuantity = itemQuantity;
    }

    public Long getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice(){
        return itemPrice;
    }

    public void setItemPrice(String itemPrice){
        this.itemPrice = itemPrice;
    }

    public String getItemUnitOfMeasurement() {
        return itemUnitOfMeasurement;
    }

    public void setItemUnitOfMeasurement(String itemUnitOfMeasurement) {
        this.itemUnitOfMeasurement = itemUnitOfMeasurement;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }
}
