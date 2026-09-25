package com.example.minierp.controller;

import com.example.minierp.*;
import com.example.minierp.repository.CustomerRepository;
import com.example.minierp.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public OrderController(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(order -> {

                    List<OrderItemResponse> orderItems = order.getOrderItems()
                            .stream()
                            .map(orderItem -> new OrderItemResponse(
                                    orderItem.getProduct().getItemName(),
                                    orderItem.getQuantity(),
                                    orderItem.getItemPrice()
                            ))
                            .toList();

                    BigDecimal total = order.getOrderItems().stream()
                            .map(item -> item.getItemPrice()
                                    .multiply(BigDecimal.valueOf(item.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return new OrderResponse(
                            order.getId(),
                            order.getOrderDate(),
                            order.getOrderStatus(),
                            order.getCustomer().getName(),
                            order.getCustomer().getAddress(),
                            order.getCustomer().getTaxIdNo(),
                            orderItems,
                            total
                    );
                })
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {

        return orderRepository.findById(id)
                .map(order -> {

                    List<OrderItemResponse> orderItems = order.getOrderItems()
                            .stream()
                            .map(orderItem -> new OrderItemResponse(
                                    orderItem.getProduct().getItemName(),
                                    orderItem.getQuantity(),
                                    orderItem.getItemPrice()
                            ))
                            .toList();

                    BigDecimal total = order.getOrderItems().stream()
                            .map(item -> item.getItemPrice()
                                    .multiply(BigDecimal.valueOf(item.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    OrderResponse response = new OrderResponse(
                            order.getId(),
                            order.getOrderDate(),
                            order.getOrderStatus(),
                            order.getCustomer().getName(),
                            order.getCustomer().getAddress(),
                            order.getCustomer().getTaxIdNo(),
                            orderItems,
                            total
                    );

                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody Order updatedOrder) {

        return orderRepository.findById(id)
                .map(order -> {

                    order.setOrderDate(updatedOrder.getOrderDate());
                    order.setOrderStatus(updatedOrder.getOrderStatus());
                    Long customerId = updatedOrder.getCustomer().getId();

                    Customer customer = customerRepository.findById(customerId)
                            .orElseThrow();

                    order.setCustomer(customer);

                    Order savedOrder = orderRepository.save(order);

                    return ResponseEntity.ok(savedOrder);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {

        if (!orderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        orderRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public OrderResponse createOrder(@RequestBody Order order) {

        Long customerId = order.getCustomer().getId();

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow();

        order.setCustomer(customer);

        orderRepository.save(order);

        List<OrderItemResponse> orderItems = order.getOrderItems() == null
                ? List.of()
                : order.getOrderItems()
                .stream()
                .map(orderItem -> new OrderItemResponse(
                        orderItem.getProduct().getItemName(),
                        orderItem.getQuantity(),
                        orderItem.getItemPrice()
                ))
                .toList();

        BigDecimal total = order.getOrderItems().stream()
                .map(item -> item.getItemPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OrderResponse(
                order.getId(),
                order.getOrderDate(),
                order.getOrderStatus(),
                order.getCustomer().getName(),
                order.getCustomer().getAddress(),
                order.getCustomer().getTaxIdNo(),
                orderItems,
                total
        );
    }
}