package org.intershop.mvc.item.repository;

import org.intershop.mvc.item.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.query.Criteria;
import reactor.core.publisher.Mono;

public interface ItemRepositoryCustom {
    Mono<Page<Item>> findAll(Criteria criteria, Pageable pageable);
}
