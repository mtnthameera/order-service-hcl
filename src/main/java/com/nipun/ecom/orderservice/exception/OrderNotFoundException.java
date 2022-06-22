package com.nipun.ecom.orderservice.exception;

/**
 * @author Nipun on 6/6/22
 */

public class OrderNotFoundException extends RuntimeException{
 public OrderNotFoundException() {
  super();
 }

 public OrderNotFoundException(String message) {
  super(message);
 }

}
