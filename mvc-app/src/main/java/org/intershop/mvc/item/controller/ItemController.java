package org.intershop.mvc.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.intershop.mvc.cart.model.Cart;
import org.intershop.mvc.cart.service.CartSessionService;
import org.intershop.mvc.item.repository.criteria.ItemCriteria;
import org.intershop.mvc.item.service.ItemService;
import org.intershop.mvc.item.model.Item;
import org.intershop.mvc.upload.service.FileUploadService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Controller
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final CartSessionService cartSessionService;
    private final FileUploadService fileUploadService;


    @GetMapping
    public Mono<Rendering> showItems(WebSession session,
                                     Authentication authentication,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "3") int size,
                                     @RequestParam(required = false) String sort,
                                     @RequestParam(required = false) String search) {
        Pageable pageable = PageRequest.of(page, size, parseSort(sort));
        Criteria criteria = ItemCriteria.nameContains(search);
        Cart cart = cartSessionService.getCart(session);
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();

        return itemService.findAll(criteria, pageable)
                .map(itemPage -> Rendering.view("items")
                        .modelAttribute("items", itemPage)
                        .modelAttribute("currentPage", itemPage.getNumber())
                        .modelAttribute("totalPages", itemPage.getTotalPages())
                        .modelAttribute("size", size)
                        .modelAttribute("sort", sort)
                        .modelAttribute("search", search)
                        .modelAttribute("cart", cart)
                        .modelAttribute("isAuthenticated", isAuthenticated)
                        .build()
                );
    }

    @GetMapping("/{id}")
    public Mono<Rendering> showItem(@PathVariable Long id, WebSession session) {
        Cart cart = cartSessionService.getCart(session);

        return itemService.findById(id)
                .map(item -> Rendering.view("item")
                        .modelAttribute("item", item)
                        .modelAttribute("cart", cart)
                        .build()
                );
    }

    @GetMapping("/new")
    public Mono<Rendering> showNewItem() {
        return Mono.just(Rendering.view("new-item")
                .modelAttribute("item", new Item())
                .build());
    }

    @PostMapping
    public Mono<Rendering> createItem(@Valid @ModelAttribute Item item, BindingResult bindingResult,
                                      @RequestPart FilePart imageFile) {

        if (bindingResult.hasErrors()) {
            return Mono.just(Rendering.view("new-item")
                    .modelAttribute("item", item)
                    .modelAttribute("errors", bindingResult.getAllErrors())
                    .build());
        }

        return fileUploadService.uploadImage(imageFile)
                .doOnNext(item::setImage)
                .flatMap(it -> itemService.save(item))
                .map(savedItem -> Rendering.redirectTo("/items/" + savedItem.getId()).build());
    }

    private Sort parseSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.unsorted();
        }

        String[] parts = sort.split(",", 2);
        String property = parts[0];
        Sort.Direction direction = (parts.length == 2) ? Sort.Direction.fromString(parts[1]) : Sort.Direction.ASC;

        return Sort.by(direction, property);
    }
}
