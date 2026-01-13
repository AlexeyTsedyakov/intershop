package org.example.intershop.repository;

import org.example.intershop.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ItemRepositoryIntegrationTest {
    @Autowired
    private ItemRepository itemRepository;

    private Item savedItem;

    @BeforeEach
    void setUp() {
        Item item = new Item(null, "name", "image", 100, "description");
        savedItem = itemRepository.saveAndFlush(item);
    }

    @Test
    void save_shouldSaveItem_forNewId() {
        Item item = new Item(null, "test", "test", 80, "test");

        Item savedItem = itemRepository.saveAndFlush(item);

        assertThat(savedItem).isNotNull();
        assertThat(savedItem.getId()).isNotNull();
        assertThat(savedItem)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(item);
    }

    @Test
    void findById_shouldReturnId_forExistingId() {
        long id = savedItem.getId();

        Optional<Item> item = itemRepository.findById(id);

        assertThat(item)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(savedItem);
    }

    @Test
    void findById_shouldReturnNull_forNonExistingId() {
        long id = -1L;

        Optional<Item> item = itemRepository.findById(id);
        assertThat(item).isEmpty();
    }

    @Test
    void save_shouldUpdateItem_forExistingId() {
        Long id = savedItem.getId();
        String name = "new name";

        Item item = itemRepository.findById(id).orElse(null);
        assertThat(item).isNotNull();
        assertThat(item.getId()).isEqualTo(id);

        item.setName(name);
        Item updatedItem = itemRepository.saveAndFlush(item);

        assertThat(updatedItem).isNotNull();
        assertThat(updatedItem.getId()).isEqualTo(id);
        assertThat(updatedItem.getName()).isEqualTo(name);
    }

    @Test
    void deleteById_shouldDelete_forExistingId() {
        long id = savedItem.getId();

        itemRepository.deleteById(id);

        Optional<Item> deletedItem = itemRepository.findById(id);
        assertThat(deletedItem).isEmpty();
    }
}
