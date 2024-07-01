package com.stellatech.elopezo.kms.dao.entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.util.Date;


@Entity
@Data
@Table(name = "card_vault")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "customerCardId", callSuper = false)
public class CardVaultEntity {

    @Id
    private Long customerCardId;

    @NaturalId
    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String privateToken;

    @OneToOne
    @MapsId
    private CardEntity card;

    private Date createdAt;
    private Date updatedAt;


}