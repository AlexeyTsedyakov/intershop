package org.example.intershop.service;

import org.example.intershop.model.Item;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ItemService {
    Item findById(long id);

    List<Item> findAll();

    List<Item> findAllByIds(Collection<Long> ids);

    Map<Long, Item> findAllByIdsAsMap(Collection<Long> ids);

    Page<@NonNull Item> findAll(Specification<Item> spec, Pageable pageable);

    Item save(Item item);
}
