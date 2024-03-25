package org.shop.com.exceptions;

public class CartItemInvalidArgumentException extends RuntimeException {

    public CartItemInvalidArgumentException(String message) {
        super(message);
    }
}