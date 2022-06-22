package com.nipun.ecom.orderservice.dto;

import com.nipun.ecom.orderservice.model.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Nipun on 20/6/22
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto {

    private String orderNumber;
    private LocalDateTime createdOn;
    private String customerId;
    private BigDecimal totalPrice;
    private List<OrderProduct> orderProductsList;
}
