package com.nipun.ecom.orderservice.exception;

/**
 * @author Nipun on 20/6/22
 */

public class NoStocksAvailableException extends RuntimeException{

    public NoStocksAvailableException(String message) {
        super(message);
    }
}
