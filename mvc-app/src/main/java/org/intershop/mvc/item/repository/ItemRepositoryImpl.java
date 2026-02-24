package org.intershop.mvc.item.repository;

import lombok.RequiredArgsConstructor;
import org.intershop.mvc.item.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Mono;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {
    private final R2dbcEntityTemplate entityTemplate;

    @Override
    public Mono<Page<Item>> findAll(Criteria criteria, Pageable pageable) {
        Query query = Query.query(criteria);

        return entityTemplate.select(Item.class)
                .matching(query.with(pageable))
                .all()
                .collectList()
                .zipWith(entityTemplate.count(query, Item.class))
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }
}
