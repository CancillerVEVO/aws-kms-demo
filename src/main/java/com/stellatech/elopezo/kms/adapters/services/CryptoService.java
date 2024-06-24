package com.stellatech.elopezo.kms.adapters.services;

public interface CryptoService {
    public String encrypt(String plaintext);
    public String decrypt(String ciphertext);
}
