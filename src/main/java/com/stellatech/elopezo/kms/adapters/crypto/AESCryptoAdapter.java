package com.stellatech.elopezo.kms.adapters.crypto;

import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.kms.KmsClient;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
@Log
public class AESCryptoAdapter {


    private final KmsClient kmsClient;


    public AESCryptoAdapter(KmsClient kmsClient) {
        this.kmsClient = kmsClient;
    }

    public static String encrypt(String plaintText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal(plaintText.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String cipherText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(decrypted);
    }


}
