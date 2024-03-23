package org.shop.com.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, выбрасываемое, когда запрашиваемая категория не найдена.
 * Это исключение приведёт к ответу со статусом HTTP 404 (Not Found).
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
