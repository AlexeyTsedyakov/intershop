package org.example.intershop.service;

import org.example.intershop.model.Item;
import org.example.intershop.repository.ItemRepository;
import org.example.intershop.repository.criteria.ItemCriteria;
import org.example.intershop.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ItemServiceImpl.class)
public class ItemServiceImplTest {
    @MockitoBean
    private ItemRepository itemRepository;

    @Autowired
    private ItemServiceImpl itemService;

    private Item testItem;

    @BeforeEach
    void setUp() {
        testItem = new Item(1L, "name", "image", 100, "desc");
    }

    @Test
    void findById_shouldFindItem_forExistingId() {
        long id = testItem.getId();
        when(itemRepository.findById(id)).thenReturn(Mono.just(testItem));

        StepVerifier.create(itemService.findById(id))
                .assertNext(item -> {
                    assertThat(item).isNotNull();
                    assertThat(item.getId()).isEqualTo(id);
                })
                .verifyComplete();

        verify(itemRepository, times(1)).findById(id);
    }

    @Test
    void findById_shouldThrowException_forNonExistentId() {
        long id = -1L;
        when(itemRepository.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(itemService.findById(id))
                .verifyError(NoSuchElementException.class);

        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    void findAll_shouldReturnList() {
        List<Item> items = List.of(testItem);
        when(itemRepository.findAll()).thenReturn(Flux.fromIterable(items));

        StepVerifier.create(itemService.findAll())
                .expectNextSequence(items)
                .verifyComplete();

        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void findAll_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 1);
        Criteria criteria = ItemCriteria.nameContains("test");
        Page<Item> testPage = new PageImpl<>(List.of(testItem));

        when(itemRepository.findAll(criteria, pageable)).thenReturn(Mono.just(testPage));

        StepVerifier.create(itemService.findAll(criteria, pageable))
                .assertNext(foundPage -> {
                    assertThat(foundPage).isNotNull();
                    assertThat(foundPage).isNotEmpty();
                })
                .verifyComplete();

        verify(itemRepository, times(1)).findAll(eq(criteria), eq(pageable));
    }

    @Test
    void findAllByIds_shouldReturnList() {
        List<Long> ids = List.of(testItem.getId());
        when(itemRepository.findAllById(ids)).thenReturn(Flux.just(testItem));

        StepVerifier.create(itemService.findAllByIds(ids).collectList())
                .assertNext(items -> {
                    assertThat(items).isNotNull();
                    assertThat(items).isNotEmpty();
                })
                .verifyComplete();

        verify(itemRepository, times(1)).findAllById(ids);
    }

    @Test
    void findAllByIdsAsMap_shouldReturnMap() {
        List<Long> ids = List.of(testItem.getId());
        when(itemRepository.findAllById(ids)).thenReturn(Flux.just(testItem));

        StepVerifier.create(itemService.findAllByIdsAsMap(ids))
                .assertNext(itemsById -> {
                    assertThat(itemsById).isNotNull();
                    assertThat(itemsById).isNotEmpty();
                })
                .verifyComplete();

        verify(itemRepository, times(1)).findAllById(ids);
    }

    @Test
    void save_shouldReturnSavedItem() {
        Item item = new Item();
        when(itemRepository.save(any(Item.class))).thenReturn(Mono.just(testItem));

        StepVerifier.create(itemService.save(item))
                .assertNext(savedItem -> {
                    assertThat(savedItem).isNotNull();
                    assertThat(savedItem.getId()).isEqualTo(testItem.getId());
                })
                .verifyComplete();

        verify(itemRepository, times(1)).save(item);
    }
}
