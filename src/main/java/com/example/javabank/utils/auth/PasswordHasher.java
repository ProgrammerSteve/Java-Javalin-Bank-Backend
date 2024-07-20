package com.example.javabank.utils.auth;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

public final class PasswordHasher {
    private PasswordHasher() {}

    public static byte[] generateSalt(){
        SecureRandom random=new SecureRandom();
        byte[] salt=new byte[16];
        random.nextBytes(salt);
        return salt;
    }
    public static String encodeBase64(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }
    public static byte[] hashPassword(final char[] password, final byte[] salt){
        try{
            KeySpec spec=new PBEKeySpec(password,salt,65536,128);
            SecretKeyFactory factory=SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return factory.generateSecret(spec).getEncoded();
        }catch(NoSuchAlgorithmException e){
            throw new RuntimeException("Error during password hashing algorithm: ",e);
        }catch(InvalidKeySpecException e){
            throw new RuntimeException("Error during password hashing keyspec: ",e);
        }
    }

    public static String hashPasswordWithSalt(String password){
        byte[] salt=generateSalt();
        byte[] hashedPassword= hashPassword(password.toCharArray(),salt);
        String saltBase64=encodeBase64(salt);
        String hashedPassword64=encodeBase64(hashedPassword);
        return saltBase64+":"+hashedPassword64;
    }

    public static boolean verifyPassword(String originalPassword, String storedPassword){
        String[] parts = storedPassword.split(":");
        byte[] salt=Base64.getDecoder().decode(parts[0]);
        byte[] storedHash=Base64.getDecoder().decode(parts[1]);
        byte[] hashedPassword= hashPassword(originalPassword.toCharArray(),salt);
        return Arrays.equals(hashedPassword,storedHash);
    }
}
