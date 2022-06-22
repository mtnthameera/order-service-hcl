package com.nipun.ecom.orderservice.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author Nipun on 6/6/22
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class OrderRequestDto {

    //@NotEmpty(message = "")
    private String customerId;
    private List<OrderProductDto> productList;
}
