package com.darts;

import io.jsonwebtoken.Jwts;
import java.util.Date;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import java.util.Scanner;
import javax.crypto.SecretKey;


@SuppressWarnings("deprecation")
public class jwtToken {
    static SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final static String secret_key = Encoders.BASE64.encode(key.getEncoded());

    private static String generateJWTToken(int UID){

        String userId=String.valueOf(UID);      //typecasting UID to string
        
        long currentTimeMillis=System.currentTimeMillis();
        long expirationTimeMillis=currentTimeMillis + 3600000;  //token will expire after 1hr

        Date expirationDate=new Date(expirationTimeMillis);

        return Jwts.builder().setSubject(userId).setIssuedAt(new Date(currentTimeMillis)).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret_key).compact();
    }

    public void getJWT(Scanner scanner){         //main function
        System.out.println("Enter UID: ");
        int UID = scanner.nextInt();

        String JWT= generateJWTToken(UID);
        System.out.println("JWT token is: "+JWT);
    }
}
