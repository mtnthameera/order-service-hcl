package com.nipun.ecom.orderservice.service;

import com.nipun.ecom.orderservice.dto.OrderRequestDto;
import com.nipun.ecom.orderservice.dto.OrderResponseDto;

import java.util.List;

/**
 * @author Nipun on 3/6/22
 */


public interface OrderService {
    OrderRequestDto getOrderById(Long orderId);

    OrderResponseDto saveOrder(OrderRequestDto orders);

    List<OrderResponseDto> getOrderByCustomerId(String customerId, boolean isLastMonthOnly);
}
