package com.stellatech.elopezo.kms.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.DecryptResponse;
import software.amazon.awssdk.services.kms.model.InvalidCiphertextException;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class DataKeyProvider {

    private static final Logger log = LoggerFactory.getLogger(DataKeyProvider.class);
    @Autowired
    private  KmsClient kmsClient;



    private String encryptedDataKey = "AQIDAHhDthCnK7zqasZwhbGQ8UqBTy+mNM1/Ljph78dlLn5rGwE3OUWlxu8ERkL/j3uX/dqsAAAAfjB8BgkqhkiG9w0BBwagbzBtAgEAMGgGCSqGSIb3DQEHATAeBglghkgBZQMEAS4wEQQML9g35iTEgMe74YhBAgEQgDuvgvNFfj4wKrej66ouchhQDPsEn8uA3Ps8W0qMnhWnuXnG6ApoLZ+CKYJ8th0cdnPpRo5tMyQwV/TKBw==";


    public String getDataKey() {
        try {
            log.info("Starting decryption process");

            DecryptRequest decryptRequest = DecryptRequest.builder()
                    .ciphertextBlob(SdkBytes.fromByteBuffer(ByteBuffer.wrap(Base64.getDecoder().decode(encryptedDataKey.getBytes()))))
                    .build();

            DecryptResponse decryptResponse = kmsClient.decrypt(decryptRequest);

            log.info("Decryption successful");

            byte[] dataKey = decryptResponse.plaintext().asByteArray();

            return new String(dataKey, StandardCharsets.UTF_8);



        } catch (InvalidCiphertextException e) {
            throw new RuntimeException("Invalid ciphertext: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data key: " + e.getClass() + " - " + e.getMessage());
        }
    }
}
