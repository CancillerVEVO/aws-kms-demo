package com.stellatech.elopezo.kms.adapters.keywrapper;

import com.stellatech.elopezo.kms.adapters.services.KeyWrapperService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.core.VaultTransitOperations;

@Component
@Log
@Profile("vault")
public class VaultKeyWrapperAdapter implements KeyWrapperService {

    @Value("${transit.key}")
    private String keyName;

    private final VaultTemplate vaultTemplate;

    public VaultKeyWrapperAdapter(VaultTemplate vaultTemplate) {
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
