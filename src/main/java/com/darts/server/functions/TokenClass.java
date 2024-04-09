package com.darts.server.functions;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import java.util.Base64;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import io.jsonwebtoken.SignatureAlgorithm;

@SuppressWarnings("deprecation")
public class TokenClass {
    private String secretKey;
    private Jws<Claims> payload; 

    public TokenClass(String key){
        this.secretKey = key;
    }

    public String generateToken(int UID){
        String userId=String.valueOf(UID);      //typecasting UID to string
        
        long currentTimeMillis=System.currentTimeMillis();
        long expirationTimeMillis=currentTimeMillis + 3600000;  //token will expire after 1hr

        Date expirationDate=new Date(expirationTimeMillis);

        return Jwts.builder().claim("UID", userId).expiration(expirationDate).signWith(new SecretKeySpec(Base64.getDecoder().decode(secretKey),SignatureAlgorithm.HS512.getJcaName())).compact();
    }

    public String getPayload(){
        return this.payload.getPayload().get("UID").toString();
    }
    

    public boolean verifyToken(String token){        

        try {
            this.payload = Jwts.parser().verifyWith(new SecretKeySpec(Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS512.getJcaName())).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }

    }
}
