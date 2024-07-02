package com.stellatech.elopezo.kms.services;

import com.stellatech.elopezo.kms.adapters.crypto.AESCryptoAdapter;
import com.stellatech.elopezo.kms.adapters.services.KeyWrapperService;
import com.stellatech.elopezo.kms.dao.entities.CardEntity;
import com.stellatech.elopezo.kms.dao.entities.CardVaultEntity;
import com.stellatech.elopezo.kms.model.Card;
import com.stellatech.elopezo.kms.repositories.CardRepository;
import com.stellatech.elopezo.kms.utils.AESUtil;
import com.stellatech.elopezo.kms.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class CardService {

    private static final Logger log = LoggerFactory.getLogger(CardService.class);
    private final CardRepository cardRepository;
    private final KeyWrapperService keyWrapperService;

    @Autowired
    public CardService(CardRepository cardRepository, KeyWrapperService keyWrapperService) {
        this.cardRepository = cardRepository;
        this.keyWrapperService = keyWrapperService;
    }

    public CardEntity saveCard(Card card) throws Exception {

        String serializedCard = Utils.serializerCard(
                card.getCardNumber(),
                card.getExpirationDate(),
                card.getCvv()
        );

        String maskedCard = Utils.maskCard(card.getCardNumber());

        String publicToken = AESUtil.generateAESKey();
        SecretKey pulicTokenKey = AESUtil.decodeAESKey(publicToken);

        // Utilizar el public token para cifrar los datos de la tarjeta y crear el (privateToken)
        String privateToken = AESCryptoAdapter.encrypt(serializedCard, pulicTokenKey);

        // Utilizamos el servicio externo de KMS/Vault para cifrar el publicToken
        String encryptedPublicToken = keyWrapperService.encrypt(publicToken);

        CardEntity cardEntity = CardEntity.builder()
                .cardNumber(maskedCard)
                .cardHolderName(card.getCardHolderName())
                .expirationDate(card.getExpirationDate())
                .publicToken(encryptedPublicToken)
                .lastFourDigits(card.getCardNumber().substring(card.getCardNumber().length() - 4))
                .cvv("***")
                .build();

        CardEntity savedCard = cardRepository.save(cardEntity);

        CardVaultEntity cardVaultEntity = CardVaultEntity.builder()
                .privateToken(privateToken)
                .card(savedCard)
                .build();

        savedCard.setCardVault(cardVaultEntity);

        cardRepository.save(savedCard);

        return savedCard;

    }

    public CardEntity getCard(Long id) throws Exception {

        CardEntity cardEntity = cardRepository.findById(id).orElseThrow(() -> new RuntimeException("Card not found"));


        // Utilizamos el servicio externo de KMS/Vault para descifrar el publicToken
        String publicToken = keyWrapperService.decrypt(cardEntity.getPublicToken());

        SecretKey pulicTokenKey = AESUtil.decodeAESKey(publicToken);

        // Utilizamos el publicToken para descifrar los datos de la tarjeta
        String serializedCard = AESCryptoAdapter.decrypt(cardEntity.getCardVault().getPrivateToken(), pulicTokenKey);

        log.info("Card decrypted: {}", serializedCard);

        return cardEntity;
    }


}
