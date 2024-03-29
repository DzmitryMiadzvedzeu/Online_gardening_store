package org.shop.com.exceptions;

public class FavoritesNotFoundException extends RuntimeException{

    public FavoritesNotFoundException(String message){
        super(message);
    }
}
