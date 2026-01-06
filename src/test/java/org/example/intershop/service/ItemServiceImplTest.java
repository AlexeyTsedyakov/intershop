package org.example.intershop.service;

import org.example.intershop.model.Item;
import org.example.intershop.repository.ItemRepository;
import org.example.intershop.repository.specification.ItemSpecification;
import org.example.intershop.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        testItem = new Item();
        testItem.setId(1L);
        testItem.setName("name");
        testItem.setDescription("description");
        testItem.setPrice(100);
        testItem.setImage("image");
    }

    @Test
    void findById_shouldFindItem_forExistingId() {
        long id = testItem.getId();
        when(itemRepository.findById(id)).thenReturn(Optional.of(testItem));

        Item item = itemService.findById(id);
        assertThat(item).isNotNull();
        assertThat(item.getId()).isNotNull();
        assertThat(item.getId()).isEqualTo(id);
        verify(itemRepository, times(1)).findById(id);
    }

    @Test
    void findById_shouldThrowException_forNonExistentId() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.findById(-1L)).isInstanceOf(NoSuchElementException.class);
        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    void findAll_shouldReturnList() {
        when(itemRepository.findAll()).thenReturn(List.of(testItem));

        List<Item> items = itemService.findAll();
        assertThat(items).isNotNull();
        assertThat(items).isNotEmpty();
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void findAll_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 1);
        Specification<Item> spec = ItemSpecification.nameContains("test");
        Page<Item> testPage = new PageImpl<>(List.of(testItem));

        when(itemRepository.findAll(spec, pageable)).thenReturn(testPage);

        Page<Item> foundPage = itemService.findAll(spec, pageable);
        assertThat(foundPage).isNotNull();
        assertThat(foundPage).isNotEmpty();
        verify(itemRepository, times(1)).findAll(eq(spec), eq(pageable));
    }

    @Test
    void findAllByIds_shouldReturnList() {
        List<Long> ids = List.of(testItem.getId());
        when(itemRepository.findAllById(ids)).thenReturn(List.of(testItem));

        List<Item> items = itemService.findAllByIds(ids);
        assertThat(items).isNotNull();
        assertThat(items).isNotEmpty();
        verify(itemRepository, times(1)).findAllById(ids);
    }

    @Test
    void findAllByIdsAsMap_shouldReturnMap() {
        List<Long> ids = List.of(testItem.getId());
        when(itemRepository.findAllById(ids)).thenReturn(List.of(testItem));

        Map<Long, Item> itemsById = itemService.findAllByIdsAsMap(ids);
        assertThat(itemsById).isNotNull();
        assertThat(itemsById).isNotEmpty();
        verify(itemRepository, times(1)).findAllById(ids);
    }

    @Test
    void save_shouldReturnSavedItem() {
        when(itemRepository.save(any(Item.class))).thenReturn(testItem);

        Item item = new Item();
        Item savedItem = itemService.save(item);
        assertThat(savedItem).isNotNull();
        assertThat(savedItem.getId()).isNotNull();
        assertThat(savedItem.getId()).isEqualTo(testItem.getId());
        verify(itemRepository, times(1)).save(item);
    }
}
