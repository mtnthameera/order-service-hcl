package com.nipun.ecom.orderservice.service;

import com.nipun.ecom.orderservice.consumer.ProductConsumer;
import com.nipun.ecom.orderservice.dto.OrderRequestDto;
import com.nipun.ecom.orderservice.dto.OrderResponseDto;
import com.nipun.ecom.orderservice.exception.NoStocksAvailableException;
import com.nipun.ecom.orderservice.exception.OrderNotFoundException;
import com.nipun.ecom.orderservice.model.Order;
import com.nipun.ecom.orderservice.model.OrderProduct;
import com.nipun.ecom.orderservice.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Nipun on 3/6/22
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ProductConsumer consumer;


    @Override
    public OrderRequestDto getOrderById(Long orderId) {
        LOGGER.debug("OrderServiceImpl::getOrderById");
        return repository.findById(orderId).map(order -> mapper.map(order, OrderRequestDto.class))
                .orElseThrow(() -> new OrderNotFoundException("Invalid Order Id :" + orderId));
    }

    @Override
    public OrderResponseDto saveOrder(OrderRequestDto order) {
        LOGGER.debug("OrderServiceImpl::saveOrder");
        if (!order.getProductList().stream().allMatch(odr -> isStocksAvailable(odr.getProductCode(), odr.getQuantity())))
            throw new NoStocksAvailableException("No Stocks Available");

        List<OrderProduct> orderList = order.getProductList().stream().map(orderProductDto -> {
            orderProductDto.setUnitPrice(consumer.getUnitPrice(orderProductDto.getProductCode()).getBody());
            return mapper.map(orderProductDto, OrderProduct.class);
        }).collect(Collectors.toList());

        Order orders = Order.builder()
                .orderProductsList(orderList)
                .customerId(order.getCustomerId())
                .createdOn(LocalDateTime.now())
                .totalPrice(calculateTotalPrice(orderList))
                .orderNumber(UUID.randomUUID().toString())
                .build();
        Order odr = repository.save(orders);

        orderList.forEach(op -> consumer.updateStocks(op.getProductCode(), op.getQuantity()));

        return OrderResponseDto.builder()
                .orderNumber(odr.getOrderNumber())
                .orderProductsList(odr.getOrderProductsList())
                .createdOn(odr.getCreatedOn())
                .customerId(odr.getCustomerId())
                .totalPrice(odr.getTotalPrice())
                .build();
    }

    public boolean isStocksAvailable(String productCode, Integer quantity) {
        return consumer.getAvailableUnits(productCode).getBody() - quantity >= 0;
    }

    private BigDecimal calculateTotalPrice(List<OrderProduct> products) {
        LOGGER.debug("OrderServiceImpl::calculateTotalPrice");
        BigDecimal total = BigDecimal.ZERO;
        for (OrderProduct product : products) {
            total = total.add(product.getUnitPrice())
                    .multiply(BigDecimal.valueOf(product.getQuantity()));
        }
        return total;
    }

    @Override
    public List<OrderResponseDto> getOrderByCustomerId(String customerId, boolean isLastMonthOnly) {
        LOGGER.debug("CustomerServiceImpl::getOrderByCustomerId");
        if (!isLastMonthOnly)
            return repository.findByCustomerId(customerId).stream()
                    .map(order -> mapper.map(order, OrderResponseDto.class))
                    .collect(Collectors.toList());

        return repository.findByCustomerId(customerId).stream()
                .filter(order -> order.getCreatedOn().isAfter(LocalDateTime.now().minusMonths(1)))
                .map(order -> mapper.map(order, OrderResponseDto.class))
                .collect(Collectors.toList());
    }
}
