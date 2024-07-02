package com.stellatech.elopezo.kms.adapters.services;

public interface KeyWrapperService {
    public String encrypt(String plaintext);
    public String decrypt(String ciphertext);
}
