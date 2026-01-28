package com.voluntree.backend.config.security;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class CryptoUtils {
    
    private static final String SECRET_KEY_ENV = System.getenv("ENCRYPTION_KEY");
    private static final String ALGORITHM = "AES";

    
    @PostConstruct
    public void init() {
        if (SECRET_KEY_ENV == null || SECRET_KEY_ENV.length() != 16 && SECRET_KEY_ENV.length() != 24 && SECRET_KEY_ENV.length() != 32) {
            throw new IllegalStateException("ERRO CRÍTICO: Variável de ambiente 'ENCRYPTION_KEY' não configurada ou com tamanho inválido (deve ser 16, 24 ou 32 chars).");
        }
    }

    public static String encrypt(String value) {
        if (value == null) return null;
        try {
            Key key = new SecretKeySpec(SECRET_KEY_ENV.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes()));
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            throw new RuntimeException("Erro ao criptografar dados", e);
        }
    }

    public static String decrypt(String encryptedValue) {
        if (encryptedValue == null) return null;
        try {
            Key key = new SecretKeySpec(SECRET_KEY_ENV.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedValue)));
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            throw new RuntimeException("Erro ao descriptografar dados", e);
        }
    }
}
