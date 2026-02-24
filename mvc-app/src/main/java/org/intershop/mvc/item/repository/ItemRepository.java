package org.intershop.mvc.item.repository;

import org.intershop.mvc.item.model.Item;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends R2dbcRepository<Item, Long>, ItemRepositoryCustom {
}
