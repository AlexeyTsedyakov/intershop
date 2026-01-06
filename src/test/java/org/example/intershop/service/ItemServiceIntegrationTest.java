package org.example.intershop.service;

import org.example.intershop.model.Item;
import org.example.intershop.repository.specification.ItemSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ItemServiceIntegrationTest {
    @Autowired
    private ItemService itemService;

    private Item testItem;

    @BeforeEach
    void setUp() {
        testItem = new Item(null, "test item", "image", 100, "test");
    }

    @Test
    void save_shouldSaveNewItem() {
        Item item = itemService.save(testItem);

        assertThat(item).isNotNull();
        assertThat(item.getId()).isNotNull();
        assertThat(item)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(testItem);
    }

    @Test
    void save_shouldUpdateExistentItem() {
        String newName = "new name";
        Item item = itemService.save(testItem);

        assertThat(item).isNotNull();
        assertThat(item.getId()).isNotNull();
        assertThat(item)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(testItem);

        item.setName(newName);
        item = itemService.save(item);

        assertThat(item.getName()).isEqualTo(newName);
    }

    @Test
    void findById_shouldReturnItem_withCorrectId() {
        Item item = itemService.save(testItem);

        assertThat(item).isNotNull();
        assertThat(item.getId()).isNotNull();

        Item savedItem = itemService.findById(item.getId());

        assertThat(savedItem).isNotNull();
        assertThat(savedItem.getId()).isEqualTo(item.getId());
    }

    @Test
    void findById_shouldThrowException_withWrongId() {
        long id = -1L;

        assertThatThrownBy(() -> itemService.findById(id)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void findAll_shouldReturnList() {
        Item item = itemService.save(testItem);
        List<Item> items = itemService.findAll();

        assertThat(items).isNotEmpty();
        assertThat(items).contains(item);
    }

    @Test
    void findAll_shouldReturnPage() {
        Item item = itemService.save(testItem);
        Pageable pageable = PageRequest.of(0, 1);
        Specification<Item> spec = ItemSpecification.nameContains(testItem.getName());

        Page<Item> page = itemService.findAll(spec, pageable);
        assertThat(page).isNotEmpty();
        assertThat(page).contains(item);
    }

    @Test
    void findAllByIds_shouldReturnList() {
        Item item = itemService.save(testItem);
        List<Item> items = itemService.findAllByIds(List.of(item.getId()));

        assertThat(items).isNotEmpty();
        assertThat(items).contains(item);
    }

    @Test
    void findAllByIdAsMap_shouldReturnMap() {
        Item item = itemService.save(testItem);
        Map<Long, Item> itemsById = itemService.findAllByIdsAsMap(List.of(item.getId()));

        assertThat(itemsById).isNotEmpty();
        assertThat(itemsById).containsKey(item.getId());
    }
}
