package org.example.intershop.service;

import org.example.intershop.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.query.Criteria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;

public interface ItemService {
    Mono<Item> findById(long id);

    Flux<Item> findAll();

    Flux<Item> findAllByIds(Collection<Long> ids);

    Mono<Map<Long, Item>> findAllByIdsAsMap(Collection<Long> ids);

    Mono<Page<Item>> findAll(Criteria criteria, Pageable pageable);

    Mono<Item> save(Item item);
}
