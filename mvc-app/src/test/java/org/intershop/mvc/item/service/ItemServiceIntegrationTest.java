package org.intershop.mvc.item.service;

import org.intershop.mvc.item.model.Item;
import org.intershop.mvc.item.repository.ItemRepository;
import org.intershop.mvc.order.repository.OrderItemRepository;
import org.intershop.mvc.item.repository.criteria.ItemCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class ItemServiceIntegrationTest {
    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    private Item savedItem;

    @BeforeEach
    void setUp() {
        orderItemRepository.deleteAll().block();
        itemRepository.deleteAll().block();

        Item item = new Item(null, "test item", "image", 100, "test");
        savedItem = itemRepository.save(item).block();
    }

    @Test
    void save_shouldSaveNewItem() {
        Item newItem = new Item(null, "new item", "image", 100, "test");

        itemService.save(newItem)
                .as(StepVerifier::create)
                .assertNext(item -> {
                    assertThat(item).isNotNull();
                    assertThat(item.getId()).isNotNull();
                    assertThat(item)
                            .usingRecursiveComparison()
                            .ignoringFields("id")
                            .isEqualTo(newItem);
                })
                .verifyComplete();
    }

    @Test
    void save_shouldUpdateExistentItem() {
        String newName = "new name";
        savedItem.setName(newName);

        itemService.save(savedItem)
                .as(StepVerifier::create)
                .assertNext(item -> assertThat(item.getName()).isEqualTo(newName))
                .verifyComplete();
    }

    @Test
    void findById_shouldReturnItem_withCorrectId() {
        long id = savedItem.getId();

        itemService.findById(id)
                .as(StepVerifier::create)
                .assertNext(foundItem -> {
                    assertThat(foundItem).isNotNull();
                    assertThat(foundItem.getId()).isEqualTo(id);
                })
                .verifyComplete();
    }

    @Test
    void findById_shouldThrowError_withWrongId() {
        long id = -1L;

        itemService.findById(id)
                .as(StepVerifier::create)
                .verifyError(NoSuchElementException.class);
    }

    @Test
    void findAll_shouldReturnList() {
        itemService.findAll()
                .collectList()
                .as(StepVerifier::create)
                .assertNext(items -> {
                    assertThat(items).isNotEmpty();
                    assertThat(items).contains(savedItem);
                })
                .verifyComplete();
    }

    @Test
    void findAll_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 1);
        Criteria criteria = ItemCriteria.nameContains(savedItem.getName());

        itemService.findAll(criteria, pageable)
                .as(StepVerifier::create)
                .assertNext(page -> {
                    assertThat(page).isNotEmpty();
                    assertThat(page).contains(savedItem);
                })
                .verifyComplete();
    }

    @Test
    void findAllByIds_shouldReturnList() {
        itemService.findAllByIds(List.of(savedItem.getId()))
                .collectList()
                .as(StepVerifier::create)
                .assertNext(items -> {
                    assertThat(items).isNotEmpty();
                    assertThat(items).contains(savedItem);
                })
                .verifyComplete();
    }

    @Test
    void findAllByIdAsMap_shouldReturnMap() {
        long id = savedItem.getId();

        itemService.findAllByIdsAsMap(List.of(id))
                .as(StepVerifier::create)
                .assertNext(itemsById -> {
                    assertThat(itemsById).isNotEmpty();
                    assertThat(itemsById).containsKey(id);
                })
                .verifyComplete();
    }
}
