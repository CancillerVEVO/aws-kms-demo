package com.stellatech.elopezo.kms.repositories;

import com.stellatech.elopezo.kms.dao.entities.CardEntity;
import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends CrudRepository<CardEntity, Long> {
}
