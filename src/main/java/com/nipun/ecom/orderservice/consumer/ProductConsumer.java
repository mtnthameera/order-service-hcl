package com.nipun.ecom.orderservice.consumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * @author Nipun on 20/6/22
 */

@FeignClient(name = "PRODUCT-SERVICE/v1/products")
public interface ProductConsumer {

    @GetMapping("/stocks/{productCode}")
    ResponseEntity<Integer> getAvailableUnits(@Valid @PathVariable String productCode);

    @PutMapping("/stocks/{productCode}/update/{soldCount}")
    ResponseEntity<Integer> updateStocks(@PathVariable String productCode, @PathVariable Integer soldCount);

    @GetMapping("/{productCode}/unit-price")
    ResponseEntity<BigDecimal> getUnitPrice(@PathVariable String productCode);
}
