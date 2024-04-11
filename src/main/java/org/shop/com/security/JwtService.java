package org.shop.com.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.shop.com.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final SecretKey secretSigningKey;

    //Читаем ключ для подписи из файла проперти jwttoken.signing.key кодированного в Base64
    //это должна быть любая комбинация символов(но не короткая), кодированная в Base64
    public JwtService(@Value("${jwttoken.signing.key}") String jwttokenSigningKey) {
        this.secretSigningKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwttokenSigningKey));
    }

    // Генерация токена
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // заполняем данные о пользователе
        if (userDetails instanceof UserEntity userEntity) {
            claims.put("userId", userEntity.getId());
            claims.put("login", userEntity.getName());
            claims.put("role", userEntity.getRole());
        }
        return generateToken(claims, userDetails);
    }

    // Метод непосредственно генерирует токен на основании набора данных о пользователе
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claims()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
                .subject(userDetails.getUsername())
                .add(extraClaims)
                .and()
                .signWith(secretSigningKey) // resume JwtBuilder calls
                .compact();
    }

    //Извлечение имени пользователя из токена
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //Проверяем что токен валиден,именно для этого пользователя и не истек срок действия
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Извлечение данных из токена @param claimsResolvers функция извлечения данных
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    // Проверка токена на просроченность @return true, если токен просрочен
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Извлечение даты истечения токена @return дата истечения
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Извлечение всех данных из токена
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .setSigningKey(secretSigningKey)
                .build().parseSignedClaims(token).getPayload();
    }
}