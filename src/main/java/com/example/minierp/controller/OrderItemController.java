package com.example.minierp.controller;

import com.example.minierp.*;
import com.example.minierp.repository.ProductRepository;
import com.example.minierp.repository.OrderRepository;
import com.example.minierp.repository.OrderItemRepository;
import org.springframework.http.ResponseEntity;
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
                        orderItem.getId(),
                        orderItem.getProduct().getItemName(),
                        orderItem.getQuantity(),
                        orderItem.getItemPrice()
                ))
                .toList();
    }

    @PostMapping
    public OrderItemResponse createOrderItem(@RequestBody OrderItemRequest request) {

        Order order = orderRepository.findById(request.getOrder().getId())
                .orElseThrow();

        Product product = productRepository.findById(request.getProduct().getId())
                .orElseThrow();

        OrderItem orderItem = new OrderItem();

        orderItem.setQuantity(request.getQuantity());
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setItemPrice(product.getItemPrice());

        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        return new OrderItemResponse(
                savedOrderItem.getId(),
                savedOrderItem.getProduct().getItemName(),
                savedOrderItem.getQuantity(),
                savedOrderItem.getItemPrice()
        );
    }

    @PutMapping("/{id}")
    public OrderItem updateOrderItem(
            @PathVariable Long id,
            @RequestBody OrderItemRequest request) {

        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow();

        orderItem.setQuantity(request.getQuantity());

        return orderItemRepository.save(orderItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {

        if (!orderItemRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        orderItemRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}