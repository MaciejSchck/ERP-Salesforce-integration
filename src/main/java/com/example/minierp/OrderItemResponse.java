package com.example.minierp;

import java.math.BigDecimal;

public class OrderItemResponse {

    private Long id;
    private String productName;
    private Integer quantity;
    private BigDecimal itemPrice;

    public OrderItemResponse(Long id, String productName, Integer quantity, BigDecimal itemPrice) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }

    public Long getId() {
        return id;
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