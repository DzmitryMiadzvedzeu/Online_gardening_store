package org.shop.com.security.model;

import lombok.Data;

@Data
public class SignInRequest {

    private String login;

    private String password;
}