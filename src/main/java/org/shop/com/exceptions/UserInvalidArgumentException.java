package org.shop.com.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserInvalidArgumentException extends RuntimeException {

    public UserInvalidArgumentException(String message) {
        super(message);
    }
}