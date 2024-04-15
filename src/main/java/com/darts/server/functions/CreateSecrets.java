package com.darts.server.functions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.crypto.SecretKey;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

@SuppressWarnings("deprecation")
public class CreateSecrets {
    static SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final static String secret_key = Encoders.BASE64.encode(key.getEncoded());
    public void writeSecret() {
        Properties properties = new Properties();

        try{
            FileInputStream in = new FileInputStream("./src/main/resources/application.properties");
            properties.load(in);
        }catch(Exception err){
            err.printStackTrace();
        }

        try{
            OutputStream outputStream = new FileOutputStream("./src/main/resources/application.properties");
            properties.setProperty("secrets.secretkeydoc", secret_key);
            properties.store(outputStream, null);
        }catch(Exception err){
            err.printStackTrace();
        }
    }
}
