package com.example.minierp;

import java.math.BigDecimal;

public class OrderItemResponse {

    private String productName;
    private Integer quantity;
    private BigDecimal itemPrice;

    public OrderItemResponse(String productName, Integer quantity, BigDecimal itemPrice) {
        this.productName = productName;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }


    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }
}