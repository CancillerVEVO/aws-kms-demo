package com.stellatech.elopezo.kms.dao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Table(name = "cards")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cardNumber;
    private String cardHolderName;
    private String lastFourDigits;
    private String cvv;
    private Date expirationDate;
    private String token;


    @OneToOne(mappedBy = "card", cascade = CascadeType.ALL)
    private CardVaultEntity cardVault;
}
