package com.musiienko.library.service.impl;

import com.musiienko.library.constant.Constant;
import com.musiienko.library.entity.User;
import com.musiienko.library.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class BaseJwtService implements JwtService {


    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof User customUserDetails) {
            claims.put(Constant.ROLE_CLAIM, customUserDetails.getRole());
        }
        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Constant.JWT_EXPIRATION_TIME_MILLIS))
                .signWith(SignatureAlgorithm.HS256, Constant.JWT_SIGNING_KEY).compact();
    }

    @Override
    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(Constant.JWT_SIGNING_KEY).parseClaimsJws(token)
                .getBody();
    }

}
