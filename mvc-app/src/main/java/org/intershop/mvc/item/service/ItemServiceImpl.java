package org.intershop.mvc.item.service;

import lombok.RequiredArgsConstructor;
import org.intershop.mvc.item.model.Item;
import org.intershop.mvc.item.repository.ItemRepository;
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
