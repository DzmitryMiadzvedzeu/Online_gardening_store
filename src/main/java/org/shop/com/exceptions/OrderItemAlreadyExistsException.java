package org.shop.com.exceptions;

public class OrderItemAlreadyExistsException extends RuntimeException{
    public OrderItemAlreadyExistsException(String message) {
        super(message);
    }
}
