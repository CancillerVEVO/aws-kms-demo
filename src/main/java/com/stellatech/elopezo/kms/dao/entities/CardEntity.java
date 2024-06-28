package com.stellatech.elopezo.kms.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(columnDefinition = "TEXT")
    private String publicToken;


    @OneToOne(mappedBy = "card", cascade = CascadeType.ALL)
    @JsonIgnore
    private CardVaultEntity cardVault;
}
