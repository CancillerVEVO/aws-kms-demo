package com.stellatech.elopezo.kms.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.DecryptResponse;
import software.amazon.awssdk.services.kms.model.EncryptRequest;
import software.amazon.awssdk.services.kms.model.EncryptResponse;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RequiredArgsConstructor
@Log
@Component
public class KMSUtils{
    private final KmsClient kmsClient ;

    @Value("${cmkKeyARN}")
    private String cmkKeyARN;

    public String encrypt(String plainText) {
        log.info("KMSUtils: Encrypting data");
        EncryptRequest encryptRequest = buildEncryptRequest(plainText);
        EncryptResponse encryptResponse = kmsClient.encrypt(encryptRequest);
        SdkBytes cipherTextBytes = encryptResponse.ciphertextBlob();
        byte[] base64EncodedValue = Base64.getEncoder().encode(cipherTextBytes.asByteArray());
        String responseBase64 = new String(base64EncodedValue);
        log.info("Data encrypted successfully : " + responseBase64);
        return responseBase64;

    }

    public String decrypt(String base64EncodedValue) {
        log.info("KMSUtils: Decrypting data");
        DecryptRequest decryptRequest = buildDecryptRequest(base64EncodedValue);
        DecryptResponse decryptResponse = kmsClient.decrypt(decryptRequest);
        byte[] dataKey = decryptResponse.plaintext().asByteArray();
        String decryptedValue = new String(dataKey, StandardCharsets.UTF_8);
        log.info("Data decrypted successfully : " + decryptedValue);
        return decryptedValue;
    }


    private EncryptRequest buildEncryptRequest(String plainText) {
        log.info("Building encrypt request");
        SdkBytes plainTextBytes = SdkBytes.fromUtf8String(plainText);
        EncryptRequest encryptRequest = EncryptRequest.builder()
                .keyId(cmkKeyARN)
                .plaintext(plainTextBytes)
                .build();

        log.info("Encrypt request built successfully");
        return encryptRequest;
    }
    private DecryptRequest buildDecryptRequest(String base64EncodedValue) {
        log.info("Building decrypt request");
        SdkBytes cipherTextBytes = SdkBytes.fromByteArray(Base64.getDecoder().decode(base64EncodedValue));
        DecryptRequest decryptRequest = DecryptRequest.builder()
                .ciphertextBlob(cipherTextBytes)
                .build();

        log.info("Decrypt request built successfully");
        return decryptRequest;
    }

}
