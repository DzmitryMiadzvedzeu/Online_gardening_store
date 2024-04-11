package org.shop.com.security;

import lombok.extern.slf4j.Slf4j;
import org.shop.com.security.model.JwtAuthenticationResponse;
import org.shop.com.security.model.SignInRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userService;

    @Autowired
    private JwtService jwtService;

    @Override
    public JwtAuthenticationResponse authenticate(SignInRequest request) {
        //По факту здесь будет проверка нашего логина и пароля с нашим пользователем в базе данных
        //как и при базовой аутентификации, просто обернуто через менеджера
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(),
                        request.getPassword()));

        //Если с аутентификацией проблемы, то выше будет выброшено исключение
        //если проблем нет, то генерируем токен для этого пользователя

        UserDetails user = userService.loadUserByUsername(request.getLogin());

        String token = jwtService.generateToken(user);

        // для интереса, сгенерированный токен можно проверить на сайте jwt.io
        return new JwtAuthenticationResponse(token);
    }
}