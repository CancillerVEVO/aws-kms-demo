package com.stellatech.elopezo.kms.model;

import lombok.Data;

import java.util.Date;

@Data
public class Card {
    private Long id;
    private String cardNumber;
    private String cardHolderName;
    private String lastFourDigits;
    private String cvv;
    private Date expirationDate;
    private String token;
}
