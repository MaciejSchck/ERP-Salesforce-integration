package com.example.minierp;

import java.time.LocalDate;
import java.util.List;

public class OrderResponse {

    private Long id;
    private LocalDate orderDate;
    private String orderStatus;
    private String customerName;
    private String customerAddress;
    private List<OrderItemResponse> orderItems;

    public OrderResponse(
            Long id,
            LocalDate orderDate,
            String orderStatus,
            String customerName,
            String customerAddress,
            List<OrderItemResponse> orderItems) {

        this.id = id;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.orderItems = orderItems;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }
}