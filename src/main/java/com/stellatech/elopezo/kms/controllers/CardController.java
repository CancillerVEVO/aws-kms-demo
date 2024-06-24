package com.stellatech.elopezo.kms.controllers;

import com.stellatech.elopezo.kms.dao.entities.CardEntity;
import com.stellatech.elopezo.kms.model.Card;
import com.stellatech.elopezo.kms.services.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
public class CardController {

    private static final Logger log = LoggerFactory.getLogger(CardController.class);
    private final CardService cardService;


    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public CardEntity saveCard(@RequestBody Card card) {
        log.info("Received request to save card: {}", card);

        return cardService.saveCard(card);
    }

    @GetMapping("/{id}")
    public CardEntity getCard(@PathVariable Long id) {
        log.info("Received request to get card with id: {}", id);

        CardEntity card = cardService.getCard(id);

        return card;
    }
}
