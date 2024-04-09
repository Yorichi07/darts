package com.darts.server.functions;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import java.util.Base64;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import io.jsonwebtoken.SignatureAlgorithm;

@SuppressWarnings("deprecation")
public class TokenClass {
    private String secretKey;

    public TokenClass(String key){
        this.secretKey = key;
    }

    public String generateToken(int UID){
        String userId=String.valueOf(UID);      //typecasting UID to string
        
        long currentTimeMillis=System.currentTimeMillis();
        long expirationTimeMillis=currentTimeMillis + 3600000;  //token will expire after 1hr

        Date expirationDate=new Date(expirationTimeMillis);

        return Jwts.builder().setSubject(userId).setIssuedAt(new Date(currentTimeMillis)).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    public Integer getPayload(String token){
        String[] chunks = token.split("\\.");



        return 0;
    }
    

    public boolean verifyToken(String token){        

        try {
            Jwts.parser().verifyWith(new SecretKeySpec(Base64.getDecoder().decode(secretKey), "HS512")).build();
            return true;
        } catch (JwtException e) {
            return false;
        }

    }

    private static String decode(String encStr){
        return new String(Base64.getUrlDecoder().decode(encStr));
    }
}
