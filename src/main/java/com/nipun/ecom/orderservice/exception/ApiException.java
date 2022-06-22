package com.nipun.ecom.orderservice.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Nipun on 3/6/22
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiException {

    private HttpStatus status;
    private String message;

}
