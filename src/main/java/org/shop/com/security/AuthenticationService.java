package org.shop.com.security;

import org.shop.com.security.model.JwtAuthenticationResponse;
import org.shop.com.security.model.SignInRequest;

public interface AuthenticationService {

    JwtAuthenticationResponse authenticate(SignInRequest request);
}