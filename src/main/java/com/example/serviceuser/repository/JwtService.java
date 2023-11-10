package com.example.serviceuser.repository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class JwtService {
    public String generateToken(String email){
        Map<String, Object> claims=new HashMap<>();
        return createToken(claims, email);
    }

    private String createToken(Map<String, Object> claims, String email){
        return  Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration((new Date(System.currentTimeMillis() + 1000 * 60 *30)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey(){
        byte [] keyBytes = Decoders.BASE64.decode("S7778D75Bg8UleczU82XGNTfbq64lDOubEliHhTy+6ZD+7DFBz0aPtGjPNeVdI7F");
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
