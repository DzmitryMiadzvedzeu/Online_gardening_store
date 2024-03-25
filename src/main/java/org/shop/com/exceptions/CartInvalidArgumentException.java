package org.shop.com.exceptions;

public class CartInvalidArgumentException extends RuntimeException {

    public CartInvalidArgumentException(String message) {
        super(message);
    }

}
