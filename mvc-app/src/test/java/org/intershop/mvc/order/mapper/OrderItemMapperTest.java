package org.intershop.mvc.order.mapper;

import org.intershop.mvc.cart.model.CartItem;
import org.intershop.mvc.item.model.Item;
import org.intershop.mvc.order.model.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest(classes = OrderItemMapper.class)
class OrderItemMapperTest {
    @Autowired
    private OrderItemMapper mapper;

    private CartItem cartItem1;
    private CartItem cartItem2;

    @BeforeEach
    void setUp() {
        Item item1 = new Item(1L, "name1", "img1", 100, "desc1");
        Item item2 = new Item(2L, "name2", "img2", 150, "desc2");

        cartItem1 = new CartItem(item1, 1, item1.getPrice());
        cartItem2 = new CartItem(item2, 2, item2.getPrice());
    }

    @Test
    void fromCartItems_shouldReturnOrderItems() {
        List<CartItem> cartItems = List.of(cartItem1, cartItem2);

        List<OrderItem> orderItems = mapper.fromCartItems(cartItems);

        assertThat(orderItems)
                .hasSize(cartItems.size())
                .extracting(OrderItem::getItem, OrderItem::getQuantity, OrderItem::getPrice)
                .containsExactlyInAnyOrder(
                        tuple(cartItem1.getItem(), cartItem1.getQuantity(), cartItem1.getPrice()),
                        tuple(cartItem2.getItem(), cartItem2.getQuantity(), cartItem2.getPrice())
                );
    }
}