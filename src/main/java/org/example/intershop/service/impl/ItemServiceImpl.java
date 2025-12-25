package org.example.intershop.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.intershop.model.Item;
import org.example.intershop.repository.ItemRepository;
import org.example.intershop.service.ItemService;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    @Transactional(readOnly = true)
    public Item findById(long id) {
        return itemRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<@NonNull Item> findAll(Specification<Item> spec, Pageable pageable) {
        return itemRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> findAllByIds(Collection<Long> ids) {
        return itemRepository.findAllById(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Item> findAllByIdsAsMap(Collection<Long> ids) {
        return findAllByIds(ids).stream()
                .collect(Collectors.toMap(Item::getId, Function.identity()));
    }
}
