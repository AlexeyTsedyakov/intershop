package org.example.intershop.service;

import org.example.intershop.model.Item;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ItemService {
    Item findById(long id);

    List<Item> findAll();

    Page<@NonNull Item> findAll(Specification<Item> spec, Pageable pageable);
}
