package com.stellatech.elopezo.kms.services;

import com.stellatech.elopezo.kms.adapters.services.CryptoService;
import com.stellatech.elopezo.kms.dao.entities.CardEntity;
import com.stellatech.elopezo.kms.model.Card;
import com.stellatech.elopezo.kms.repositories.CardRepository;
import com.stellatech.elopezo.kms.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    private static final Logger log = LoggerFactory.getLogger(CardService.class);
    private final CardRepository cardRepository;
    private final CryptoService cryptoService;

    @Autowired
    public CardService(CardRepository cardRepository, CryptoService cryptoService) {
        this.cardRepository = cardRepository;
        this.cryptoService = cryptoService;
    }

    public CardEntity saveCard(Card card) {

        String serializedCard = Utils.serializerCard(
                card.getCardNumber(),
                card.getExpirationDate(),
                card.getCvv()
        );

        String encryptedCard = cryptoService.encrypt(serializedCard);

        String maskedCard = Utils.maskCard(card.getCardNumber());

        CardEntity cardEntity = CardEntity.builder().
                cardNumber(maskedCard).
                cardHolderName(card.getCardHolderName()).
                expirationDate(card.getExpirationDate()).
                cvv("***").
                lastFourDigits(card.getCardNumber().substring(card.getCardNumber().length() - 4)).
                token(encryptedCard).
                build();

        return cardRepository.save(cardEntity);
    }

    public CardEntity getCard(Long id) {

        CardEntity cardEntity = cardRepository.findById(id).orElseThrow(() -> new RuntimeException("Card not found"));

        String decryptedCard = cryptoService.decrypt(cardEntity.getToken());


        return cardEntity;
    }


}
