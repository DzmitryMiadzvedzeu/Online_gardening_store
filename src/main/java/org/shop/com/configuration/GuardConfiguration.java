package org.shop.com.configuration;

import org.shop.com.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class GuardConfiguration {

    //Набор ендпоинтов
    private static final String[] SWAGGER = {
            "/v2/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-resources",
            "/swagger-ui/",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/webjars/**",
            "/configuration/**",
            "/configuration/ui",
            "/configuration/security",
            "/public",
            "/favicon.ico",
            "/h2-console/**",
            "/conferenc/v1/swagger-ui.html",
            "/swagger-resources/configuration/ui",
            "/swagger-resources/configuration/security",
            "/",
            "/login", "/logout",
            "/csrf"
    };

    //Фильтр проверки авторизации по токену
    @Autowired
    private JwtAuthenticationFilter authenticationFilter;

    //Бин для шифрования паролей
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(SWAGGER).permitAll() //разрешаем все для сваггера
                        //разрешаем логиниться пользователю для получения токена
                        .requestMatchers("/v1/users/login").permitAll()
                        //разрешаем регистрировать нового пользователя без токенов и паролей
                        .requestMatchers("/v1/users/register").permitAll()
                        .anyRequest().authenticated()) //другие запросы через базовую или с токеном
                .httpBasic(Customizer.withDefaults())//возможность базовой аутентификации
                //отключаем хранение состояния между запросами
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //включаем работу с токеном
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}