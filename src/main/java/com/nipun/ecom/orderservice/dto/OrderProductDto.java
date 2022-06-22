package com.nipun.ecom.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Nipun on 20/6/22
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderProductDto {
    private Long id;
    private String productCode;
    private BigDecimal unitPrice;
    private int quantity;
}
