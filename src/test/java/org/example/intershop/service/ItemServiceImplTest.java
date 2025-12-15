package org.example.intershop.service;

import org.example.intershop.model.Item;
import org.example.intershop.repository.ItemRepository;
import org.example.intershop.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
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
    }
}
