package org.shop.com.exceptions;

public class CartItemsInvalidArgumentException extends RuntimeException {

    public CartItemsInvalidArgumentException(String message) {
        super(message);
    }
}