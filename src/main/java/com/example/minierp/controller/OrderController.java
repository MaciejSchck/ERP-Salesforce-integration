package com.example.minierp.controller;

import com.example.minierp.*;
import com.example.minierp.repository.CustomerRepository;
import com.example.minierp.repository.OrderItemRepository;
import com.example.minierp.repository.OrderRepository;
import com.example.minierp.repository.ProductRepository;
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
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderController(OrderRepository orderRepository, CustomerRepository customerRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(order -> {

                    List<OrderItemResponse> orderItems = order.getOrderItems()
                            .stream()
                            .map(orderItem -> new OrderItemResponse(
                                    orderItem.getId(),
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
                                    orderItem.getId(),
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
    public OrderResponse createOrder(@RequestBody CreateOrderRequest request) {

        Long customerId = request.getCustomer().getId();

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow();

        Order order = new Order();

        order.setOrderDate(request.getOrderDate());
        order.setOrderStatus(request.getOrderStatus());
        order.setCustomer(customer);

        Order savedOrder = orderRepository.save(order);

        List<OrderItemResponse> orderItems = request.getOrderItems()
                .stream()
                .map(itemRequest -> {

                    Long productId = itemRequest.getProduct().getId();

                    Product product = productRepository.findById(productId)
                            .orElseThrow();

                    OrderItem orderItem = new OrderItem();

                    orderItem.setQuantity(itemRequest.getQuantity());
                    orderItem.setOrder(savedOrder);
                    orderItem.setProduct(product);
                    orderItem.setItemPrice(product.getItemPrice());

                    OrderItem savedOrderItem = orderItemRepository.save(orderItem);

                    return new OrderItemResponse(
                            savedOrderItem.getId(),
                            savedOrderItem.getProduct().getItemName(),
                            savedOrderItem.getQuantity(),
                            savedOrderItem.getItemPrice()
                    );
                })
                .toList();

        BigDecimal total = orderItems.stream()
                .map(item -> item.getItemPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getOrderDate(),
                savedOrder.getOrderStatus(),
                savedOrder.getCustomer().getName(),
                savedOrder.getCustomer().getAddress(),
                savedOrder.getCustomer().getTaxIdNo(),
                orderItems,
                total
        );
    }
}