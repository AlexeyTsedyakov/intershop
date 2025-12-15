package org.example.intershop.repository;

import org.example.intershop.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@ActiveProfiles("test")
public class ItemRepositoryIntegrationTest {
    @Autowired
    private ItemRepository itemRepository;

    private Item testItem;

    @BeforeEach
    void setUp() {
        testItem = new Item();
        testItem.setName("name");
        testItem.setDescription("description");
        testItem.setPrice(100);
        testItem.setImage("image");
    }

    @Test
    void findById_shouldReturnId_forExistingId() {
        long id = 1L;

        Item item = itemRepository.findById(id).orElse(null);

        assertThat(item).isNotNull();
        assertThat(item.getId()).isNotNull();
        assertThat(item.getId()).isEqualTo(id);
    }

    @Test
    void findById_shouldReturnNull_forNonExistingId() {
        long id = -1L;

        Item item = itemRepository.findById(id).orElse(null);
        assertThat(item).isNull();
    }

    @Test
    void save_shouldSaveItem_forNewId() {
        Item item = itemRepository.save(testItem);
        assertThat(item).isNotNull();
        assertThat(item.getId()).isNotNull();

        Item savedItem = itemRepository.findById(item.getId()).orElse(null);
        assertThat(savedItem).isNotNull();
        assertThat(savedItem.getId()).isNotNull();
        assertThat(savedItem.getId()).isEqualTo(item.getId());
    }

    @Test
    void save_shouldUpdateItem_forExistingId() {
        Long id = 1L;
        String name = "test-name";

        Item item = itemRepository.findById(id).orElse(null);
        assertThat(item).isNotNull();
        assertThat(item.getId()).isNotNull();

        item.setName(name);
        Item updatedItem = itemRepository.save(item);
        assertThat(updatedItem).isNotNull();
        assertThat(updatedItem.getId()).isNotNull();
        assertThat(updatedItem.getId()).isEqualTo(id);
        assertThat(updatedItem.getName()).isEqualTo(name);
    }

    @Test
    void deleteById_shouldDelete_forExistingId() {
        Item item = itemRepository.save(testItem);
        assertThat(item).isNotNull();
        assertThat(item.getId()).isNotNull();

        itemRepository.deleteById(item.getId());
        Item deletedItem = itemRepository.findById(item.getId()).orElse(null);
        assertThat(deletedItem).isNull();
    }
}
