package org.example.intershop.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.intershop.model.Item;
import org.example.intershop.repository.ItemRepository;
import org.example.intershop.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    @Transactional(readOnly = true)
    public Mono<Item> findById(long id) {
        return itemRepository.findById(id)
                .switchIfEmpty(Mono.error(NoSuchElementException::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Page<Item>> findAll(Criteria criteria, Pageable pageable) {
        return itemRepository.findAll(criteria, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Item> findAllByIds(Collection<Long> ids) {
        return itemRepository.findAllById(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Map<Long, Item>> findAllByIdsAsMap(Collection<Long> ids) {
        return findAllByIds(ids)
                .collectMap(Item::getId);
    }

    @Override
    @Transactional
    public Mono<Item> save(Item item) {
        return itemRepository.save(item);
    }
}
