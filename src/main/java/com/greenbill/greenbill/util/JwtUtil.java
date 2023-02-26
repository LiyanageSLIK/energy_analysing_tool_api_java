package com.greenbill.greenbill.util;

import com.greenbill.greenbill.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@Component
public class JwtUtil {

    private static final String SECRET_KEY = "73357538782F413F4428472B4B6250655368566D5971337436773979244226452948404D635166546A576E5A7234753778214125432A462D4A614E645267556B";


    public String generateAccessToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createAccessToken(claims, email);
    }

    public String generateRefreshToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createRefreshToken(claims, email);
    }

    private String createAccessToken(Map<String, Object> claims, String subject) {
        long expirationTimeInMilliseconds = 3600000; // 1 hour
        Date expirationTime = new Date(System.currentTimeMillis() + expirationTimeInMilliseconds);

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationTime).signWith(SignatureAlgorithm.HS512, getSecretKey()).compact();
    }

    private String createRefreshToken(Map<String, Object> claims, String subject) {
        long expirationTimeInMilliseconds = 86400000; // 1 day
        Date expirationTime = new Date(System.currentTimeMillis() + expirationTimeInMilliseconds);

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationTime).signWith(SignatureAlgorithm.HS512, getSecretKey()).compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Boolean validateToken(String token, UserEntity userEntity) {
        final String email = extractEmail(token);
        return (email.equals(userEntity.getEmail()) && !isTokenExpired(token));
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSecretKey() {
        byte[] keyByte = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
