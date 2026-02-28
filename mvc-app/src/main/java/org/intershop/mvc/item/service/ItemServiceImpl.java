package org.intershop.mvc.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intershop.mvc.item.model.Item;
import org.intershop.mvc.item.repository.ItemRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    @Cacheable(cacheNames = "items", key = "#id")
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
    @Cacheable(cacheNames = "itemPages", key = "{#criteria, #pageable}")
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
    @Cacheable(cacheNames = "itemsById", key = "#ids")
    @Transactional(readOnly = true)
    public Mono<Map<Long, Item>> findAllByIdsAsMap(Collection<Long> ids) {
        return findAllByIds(ids)
                .collectMap(Item::getId);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "items", key = "#item.id"),
            @CacheEvict(cacheNames = {"itemPages", "itemsById"}, allEntries = true)
    })
    @Transactional
    public Mono<Item> save(Item item) {
        return itemRepository.save(item);
    }
}
