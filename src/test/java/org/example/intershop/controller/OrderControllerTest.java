package org.example.intershop.controller;

import org.example.intershop.model.Cart;
import org.example.intershop.model.Order;
import org.example.intershop.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private Cart cart;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setId(1L);
    }

    @Test
    void showOrder_shouldReturnView() throws Exception {
        Long id = testOrder.getId();
        when(orderService.findById(id)).thenReturn(testOrder);

        mockMvc.perform(get("/orders/" + id))
                .andExpect(status().isOk())
                .andExpect(view().name("order"))
                .andExpect(model().attribute("order", testOrder));

        verify(orderService, times(1)).findById(id);
    }

    @Test
    void showOrders_shouldReturnView() throws Exception {
        List<Order> orders = List.of(testOrder);
        long ordersAmount = 155L;
        when(orderService.findAll()).thenReturn(orders);
        when(orderService.getOrdersAmount(anyList())).thenReturn(ordersAmount);

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attribute("orders", orders))
                .andExpect(model().attribute("ordersAmount", ordersAmount));

        verify(orderService, times(1)).findAll();
        verify(orderService, times(1)).getOrdersAmount(orders);
    }

    @Test
    void placeOrder_shouldRedirect() throws Exception {
        when(orderService.placeOrder(any(Cart.class))).thenReturn(testOrder);

        mockMvc.perform(post("/orders"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/" + testOrder.getId()));

        verify(orderService, times(1)).placeOrder(any(Cart.class));
    }
}
