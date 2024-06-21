package com.stellatech.elopezo.kms;

public interface EncryptionService {
    String encrypt(String data, String key);
    String decrypt(String data, String key);
}
