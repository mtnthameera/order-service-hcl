package com.nipun.ecom.orderservice.controller;

import com.nipun.ecom.orderservice.dto.OrderRequestDto;
import com.nipun.ecom.orderservice.dto.OrderResponseDto;
import com.nipun.ecom.orderservice.model.Order;
import com.nipun.ecom.orderservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nipun on 3/6/22
 */

@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private List<Order> ordersCart = new ArrayList<>();

    @Autowired
    OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderRequestDto> getOrderById(@PathVariable Long orderId) {
        LOGGER.info("Fetching order details for : {}", orderId);
        return ResponseEntity.ok()
                .body(orderService.getOrderById(orderId));
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> saveOrder(@Valid @RequestBody OrderRequestDto order) {
        LOGGER.info("Saving Order.");
        return ResponseEntity.ok()
                .body(orderService.saveOrder(order));
    }


    @GetMapping("/customer/{customerId}")
    private ResponseEntity<List<OrderResponseDto>> getAllOrderByCustomer(@PathVariable String customerId, @RequestParam boolean lastMonth) {
        LOGGER.info("Fetching orders for customer : {}", customerId);
        if (lastMonth) {
            return ResponseEntity.ok()
                    .body(orderService.getOrderByCustomerId(customerId, true));
        }
        return ResponseEntity.ok()
                .body(orderService.getOrderByCustomerId(customerId, false));
    }


}
