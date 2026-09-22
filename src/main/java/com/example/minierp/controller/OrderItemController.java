package com.example.minierp.controller;

import com.example.minierp.Order;
import com.example.minierp.OrderItemResponse;
import com.example.minierp.Product;
import com.example.minierp.repository.ProductRepository;
import com.example.minierp.repository.OrderRepository;
import com.example.minierp.OrderItem;
import com.example.minierp.repository.OrderItemRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderItemController(OrderItemRepository orderItemRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<OrderItemResponse> getAllOrderItems() {

        return orderItemRepository.findAll()
                .stream()
                .map(orderItem -> new OrderItemResponse(
                        orderItem.getProduct().getItemName(),
                        orderItem.getQuantity(),
                        orderItem.getItemPrice()
                ))
                .toList();
    }

    @PostMapping
    public OrderItem createOrderItem(@RequestBody OrderItem orderItem) {

        Long orderId = orderItem.getOrder().getId();
        Long productId = orderItem.getProduct().getId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow();

        Product product = productRepository.findById(productId)
                .orElseThrow();

        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setItemPrice(product.getItemPrice());

        return orderItemRepository.save(orderItem);
    }
}