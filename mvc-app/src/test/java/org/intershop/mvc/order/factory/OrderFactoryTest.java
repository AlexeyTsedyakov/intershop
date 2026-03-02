package org.intershop.mvc.order.factory;

import org.intershop.mvc.cart.model.Cart;
import org.intershop.mvc.item.model.Item;
import org.intershop.mvc.order.mapper.OrderItemMapper;
import org.intershop.mvc.order.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = OrderFactory.class)
class OrderFactoryTest {
    @MockitoBean
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderFactory orderFactory;

    private Cart cart;

    @BeforeEach
    void setUp() {
        Item item1 = new Item(1L, "name1", "img1", 100, "desc1");
        Item item2 = new Item(2L, "name2", "img2", 150, "desc2");

        cart = new Cart();
        cart.increaseQuantity(item1);
        cart.increaseQuantity(item2);
        cart.increaseQuantity(item2);
    }

    @Test
    void createFromCart_shouldReturnOrder() {
        Order order = orderFactory.createFromCart(cart);

        assertThat(order.getTotalAmount())
                .isEqualTo(cart.calculateTotalAmount());
    }
}