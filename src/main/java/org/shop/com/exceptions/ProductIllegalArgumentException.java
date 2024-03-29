package org.shop.com.exceptions;

public class ProductIllegalArgumentException extends RuntimeException{

    public ProductIllegalArgumentException(String message) {
        super(message);
    }
}
