package org.example.intershop.controller;

import org.example.intershop.model.Cart;
import org.example.intershop.model.Item;
import org.example.intershop.service.FileUploadService;
import org.example.intershop.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemService itemService;

    @MockitoBean
    private Cart cart;

    @MockitoBean
    private FileUploadService fileUploadService;

    private Item testItem;

    @BeforeEach
    void setUp() {
        testItem = new Item(1L, "Test Item", "image.jpg", 100, "Description");
    }

    @Test
    void showItems_shouldReturnView() throws Exception {
        Page<Item> testPage = new PageImpl<>(List.of(testItem));
        when(itemService.findAll(any(), any(Pageable.class))).thenReturn(testPage);

        mockMvc.perform(get("/items")
                        .param("page", "0")
                        .param("size", "3")
                        .param("search", "test")
                        .param("sort", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name("items"))
                .andExpect(model().attribute("items", testPage))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("size"))
                .andExpect(model().attributeExists("sort"))
                .andExpect(model().attributeExists("search"))
                .andExpect(model().attributeExists("cart"))
                .andExpect(model().attribute("items", testPage));

        verify(itemService, times(1)).findAll(any(), any(Pageable.class));
    }

    @Test
    void showItem_shouldReturnView() throws Exception {
        long id = testItem.getId();
        when(itemService.findById(id)).thenReturn(testItem);

        mockMvc.perform(get("/items/" + id))
                .andExpect(status().isOk())
                .andExpect(view().name("item"))
                .andExpect(model().attributeExists("item", "cart"))
                .andExpect(model().attribute("item", testItem));

        verify(itemService, times(1)).findById(id);
    }

    @Test
    void showNewItem_shouldReturnView() throws Exception {
        mockMvc.perform(get("/items/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("new-item"))
                .andExpect(model().attributeExists("item"));
    }

    @Test
    void createItem_shouldRedirect_whenValid() throws Exception {
        long id = testItem.getId();
        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "test.jpg",
                "image/jpeg", "content".getBytes());
        when(fileUploadService.uploadImage(any())).thenReturn("uploaded_test.jpg");
        when(itemService.save(any(Item.class))).thenReturn(testItem);

        mockMvc.perform(multipart("/items")
                        .file(imageFile)
                        .param("name", "New Item")
                        .param("price", "200")
                        .param("description", "New Desc"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items/" + id));

        verify(fileUploadService).uploadImage(imageFile);
        verify(itemService).save(any(Item.class));
    }

    @Test
    void createItem_shouldReturnView_whenInvalid() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "test.jpg",
                "image/jpeg", "content".getBytes());

        mockMvc.perform(multipart("/items")
                        .file(imageFile)
                        .param("name", "") // Invalid: blank
                        .param("price", "0")) // Invalid: min 1
                .andExpect(status().isOk())
                .andExpect(view().name("new-item"))
                .andExpect(model().attributeExists("errors", "item"));

        verify(itemService, never()).save(any(Item.class));
    }
}
