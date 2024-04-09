package com.darts.server.functions;

import java.security.MessageDigest;

public class PasswordHash {
    
    public String getHash(String psswrd){
        try{
            MessageDigest mDigest  = MessageDigest.getInstance("SHA-256");
            mDigest.update(psswrd.getBytes());
            byte[] digest = mDigest.digest();
            StringBuilder sBuilder = new StringBuilder();
            for (byte b:digest){
                sBuilder.append(String.format("%02x", b & 0xff));
            }
            return sBuilder.toString();
        }catch(Exception err){
            return "500";
        }
    }

    public boolean checkHash(String psswrd){
        return false;
    }

}
