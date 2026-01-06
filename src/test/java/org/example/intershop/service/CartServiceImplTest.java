package org.example.intershop.service;

import org.example.intershop.model.Cart;
import org.example.intershop.model.CartItem;
import org.example.intershop.model.Item;
import org.example.intershop.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = CartServiceImpl.class)
public class CartServiceImplTest {
    @Autowired
    private CartServiceImpl cartService;

    @MockitoBean
    private ItemService itemService;

    private Cart testCart;
    private Item testItem1;
    private Item testItem2;

    @BeforeEach
    void init() {
        testCart = new Cart();
        testItem1 = new Item(1L, "name1", "image1", 100, "desc1");
        testItem2 = new Item(2L, "name2", "image2", 150, "desc2");
    }

    @Test
    void fetchItems_ShouldFetchItems() {
        Long id = testItem1.getId();
        List<Long> ids = List.of(id);
        testCart.increaseQuantity(id);
        when(itemService.findAllByIdsAsMap(ids)).thenReturn(Map.of(id, testItem1));

        cartService.fetchItems(testCart);
        List<CartItem> cartItems = testCart.getItems();
        assertThat(cartItems).isNotNull();
        assertThat(cartItems).isNotEmpty();

        CartItem testItem = testCart.getItemById(id);
        assertThat(testItem).isNotNull();
        assertThat(testItem.getItem()).isNotNull();
        assertThat(testItem.getItem()).isEqualTo(testItem1);
        verify(itemService, times(1)).findAllByIdsAsMap(ids);
    }

    @Test
    void getTotalAmount_withoutFetchedItems_shouldReturnTotalAmount() {
        Long id1 = testItem1.getId();
        Long id2 = testItem2.getId();
        List<Long> ids = List.of(id1, id2);
        testCart.increaseQuantity(id1);
        testCart.increaseQuantity(id2);
        when(itemService.findAllByIdsAsMap(ids)).thenReturn(Map.of(
                id1, testItem1,
                id2, testItem2
        ));

        Long totalAmount = cartService.getTotalAmount(testCart);
        Long expected = testCart.calculateTotalAmount();
        assertThat(totalAmount).isEqualTo(expected);
        verify(itemService, times(1)).findAllByIdsAsMap(ids);
    }

    @Test
    void getTotalAmount_withFetchedItems_shouldReturnTotalAmount() {
        Long id1 = testItem1.getId();
        Long id2 = testItem2.getId();
        testCart.increaseQuantity(id1);
        testCart.increaseQuantity(id2);
        testCart.getItemById(id1).setItem(testItem1);
        testCart.getItemById(id2).setItem(testItem2);

        Long totalAmount = cartService.getTotalAmount(testCart);
        Long expected = testCart.calculateTotalAmount();
        assertThat(totalAmount).isEqualTo(expected);
        verify(itemService, never()).findAllByIdsAsMap(any());
    }
}
