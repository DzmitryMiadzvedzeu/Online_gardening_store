package org.shop.com.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategoryInvalidArgumentException extends RuntimeException {

    public CategoryInvalidArgumentException(String message) {
        super(message);
    }
}