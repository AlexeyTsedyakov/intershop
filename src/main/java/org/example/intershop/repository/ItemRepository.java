package org.example.intershop.repository;

import org.example.intershop.model.Item;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends R2dbcRepository<Item, Long>, ItemRepositoryCustom {
}
