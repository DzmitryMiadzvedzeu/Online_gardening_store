package org.shop.com.exceptions;

public class CartItemsNotFoundException extends RuntimeException {

    public CartItemsNotFoundException(String message) {
        super(message);
    }
}
