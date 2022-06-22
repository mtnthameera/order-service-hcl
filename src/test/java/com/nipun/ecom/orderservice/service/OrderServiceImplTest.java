package com.nipun.ecom.orderservice.service;

import com.nipun.ecom.orderservice.consumer.ProductConsumer;
import com.nipun.ecom.orderservice.dto.OrderProductDto;
import com.nipun.ecom.orderservice.dto.OrderRequestDto;
import com.nipun.ecom.orderservice.dto.OrderResponseDto;
import com.nipun.ecom.orderservice.exception.OrderNotFoundException;
import com.nipun.ecom.orderservice.model.Order;
import com.nipun.ecom.orderservice.model.OrderProduct;
import com.nipun.ecom.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

/**
 * @author Nipun on 8/6/22
 */
@ExtendWith(SpringExtension.class)
class OrderServiceImplTest {

    @Mock
    OrderRepository repository;

    @Mock
    ModelMapper mapper;

    @InjectMocks
    OrderServiceImpl orderService;

    @Mock
    ProductConsumer consumer;

    @Mock
    private Predicate<OrderProductDto> predicate;


    private OrderRequestDto orderRequestDto;
    private OrderProduct orderProduct;
    private Order order;
    private Order orderOlderThanMonth;
    private OrderResponseDto orderResponseDto;
    private OrderProductDto orderProductDto;

    @BeforeEach
    public void setUp() {
        orderProduct = OrderProduct.builder()
                .id(1L)
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(20.0))
                .build();

        orderProductDto = OrderProductDto.builder()
                .id(1L)
                .productCode("xx123")
                .quantity(4)
                .unitPrice(BigDecimal.valueOf(12.30))
                .build();

        order = Order.builder()
                .totalPrice(BigDecimal.valueOf(123))
                .createdOn(LocalDateTime.now())
                .customerId("10")
                .orderNumber("x123dd")
                .orderProductsList(List.of(orderProduct))
                .build();

        orderOlderThanMonth = Order.builder()
                .totalPrice(BigDecimal.valueOf(123.0))
                .createdOn(LocalDateTime.now().minusMonths(2))
                .customerId("10")
                .id(2L)
                .orderProductsList(List.of(orderProduct))
                .build();

        orderResponseDto = OrderResponseDto.builder()
                .totalPrice(BigDecimal.valueOf(123.0))
                .customerId("10")
                .createdOn(LocalDateTime.now())
                .orderProductsList(List.of(orderProduct))
                .orderNumber("x123dd")
                .build();

        orderRequestDto = OrderRequestDto.builder()
                .productList(List.of(orderProductDto))
                .customerId("1")
                .build();

    }

    @Test
    @DisplayName("OrderNotFoundException should be thrown when invalid order ID passed")
    public void testGetOrderByIdNotFoundException() {
        //Given
        Mockito.when(repository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            //When
            orderService.getOrderById(anyLong());
        } catch (Exception e) {
            //Then
            assertEquals(OrderNotFoundException.class, e.getClass());
        }
    }

    @Test
    @DisplayName("Boolean True should return if stocks available of given product code")
    public void testIsStocksAvailableSuccess() {
        //Given
        Mockito.when(consumer.getAvailableUnits(anyString()))
                .thenReturn(ResponseEntity.ok().body(10));
        //When
        Boolean actual = orderService.isStocksAvailable(anyString(), 3);
        //Then
        assertEquals(actual, Boolean.TRUE);
    }

    @Test
    @DisplayName("All orders should return when lastMonth is false")
    public void testGetOrderByCustomerId_all() {
        //Given
        Mockito.when(repository.findByCustomerId(anyString()))
                .thenReturn(List.of(order));
        Mockito.when(mapper.map(any(), any())).thenReturn(orderResponseDto);
        //When
        List<OrderResponseDto> actual = orderService.getOrderByCustomerId(anyString(), Boolean.FALSE);
        //Then
        assertEquals(List.of(orderResponseDto), actual);

    }

    @Test
    @DisplayName("Orders created on current month should return when lastMonth is true")
    public void testGetOrderByCustomerId_lastMonthOnly() {
        //Given
        Mockito.when(repository.findByCustomerId(anyString()))
                .thenReturn(List.of(order, orderOlderThanMonth));
        Mockito.when(mapper.map(any(), any())).thenReturn(orderResponseDto);
        //When
        List<OrderResponseDto> expected = orderService.getOrderByCustomerId(anyString(), Boolean.TRUE);
        //Then
        assertEquals(List.of(orderResponseDto), expected);
        assertEquals(1, expected.size());

    }

    @Test
    @DisplayName("OrderNotFoundException should be thrown when no order is found by ID")
    public void testGetOrderByIdThrowException() {
        //Given
        Mockito.when(repository.findById(anyLong())).thenReturn(Optional.empty());
        //When
        try {
            OrderRequestDto actual = orderService.getOrderById(anyLong());
        } catch (Exception e) {
            //Then
            assertEquals(OrderNotFoundException.class, e.getClass());
        }
    }

}