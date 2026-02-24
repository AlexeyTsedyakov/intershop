package org.intershop.mvc.item.repository;

import org.intershop.mvc.item.model.Item;
import org.intershop.mvc.order.repository.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.r2dbc.test.autoconfigure.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
@ActiveProfiles("test")
public class ItemRepositoryIntegrationTest {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    private Item savedItem;

    @BeforeEach
    void setUp() {
        Item item = new Item(null, "name", "image", 100, "description");
        StepVerifier.create(orderItemRepository.deleteAll()
                        .then(itemRepository.deleteAll())
                        .then(itemRepository.save(item)))
                .assertNext(saved -> savedItem = saved)
                .verifyComplete();
    }

    @Test
    void save_shouldSaveItem_forNewId() {
        Item item = new Item(null, "test", "test", 80, "test");

        StepVerifier.create(itemRepository.save(item))
                .assertNext(saved -> {
                    assertThat(saved).isNotNull();
                    assertThat(saved.getId()).isNotNull();
                    assertThat(saved)
                            .usingRecursiveComparison()
                            .ignoringFields("id")
                            .isEqualTo(item);
                })
                .verifyComplete();
    }

    @Test
    void findById_shouldReturnId_forExistingId() {
        long id = savedItem.getId();

        StepVerifier.create(itemRepository.findById(id))
                .assertNext(item -> assertThat(item)
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem))
                .verifyComplete();
    }

    @Test
    void findById_shouldReturnEmpty_forNonExistingId() {
        long id = -1L;

        StepVerifier.create(itemRepository.findById(id))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void save_shouldUpdateItem_forExistingId() {
        Long id = savedItem.getId();
        String name = "new name";

        Mono<Item> updatedMono = itemRepository.findById(id)
                .map(item -> {
                    item.setName(name);
                    return item;
                })
                .flatMap(itemRepository::save);

        StepVerifier.create(updatedMono)
                .assertNext(updatedItem -> {
                    assertThat(updatedItem).isNotNull();
                    assertThat(updatedItem.getId()).isEqualTo(id);
                    assertThat(updatedItem.getName()).isEqualTo(name);
                })
                .verifyComplete();
    }

    @Test
    void deleteById_shouldDelete_forExistingId() {
        long id = savedItem.getId();

        StepVerifier.create(itemRepository.deleteById(id))
                .verifyComplete();

        StepVerifier.create(itemRepository.findById(id))
                .verifyComplete();
    }
}
