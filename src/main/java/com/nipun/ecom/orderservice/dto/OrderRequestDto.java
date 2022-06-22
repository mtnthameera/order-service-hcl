package com.nipun.ecom.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author Nipun on 6/6/22
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestDto {

    @NotEmpty(message = "Please provide customer ID")
    private String customerId;
    @NotEmpty(message = "Please provide product items to order")
    private List<OrderProductDto> productList;
}
