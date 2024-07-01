package com.stellatech.elopezo.kms.adapters.crypto;


import com.stellatech.elopezo.kms.adapters.services.CryptoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.core.VaultTransitOperations;

@Component
@Profile("vault")
public class VaultCryptoAdapter implements CryptoService {

    @Value("${transit.key}")
    private String keyName;

    private static final Logger log = LoggerFactory.getLogger(VaultCryptoAdapter.class);
    private final VaultTemplate vaultTemplate;

    public VaultCryptoAdapter(VaultTemplate vaultTemplate) {
        this.vaultTemplate = vaultTemplate;
    }

    public String encrypt(String plaintext) {

        VaultTransitOperations transitOperations = vaultTemplate.opsForTransit();

        String ciphertext = transitOperations.encrypt(keyName, plaintext);
        log.info("Encrypted data: " + ciphertext);
        return ciphertext;
    }

    public String decrypt(String ciphertext) {

        VaultTransitOperations transitOperations = vaultTemplate.opsForTransit();

        String text = transitOperations.decrypt(keyName, ciphertext);

        log.info("Decrypted data: " + text);
        return text;

    }
}
