package org.example.intershop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.intershop.model.Cart;
import org.example.intershop.model.Item;
import org.example.intershop.repository.specification.ItemSpecification;
import org.example.intershop.service.FileUploadService;
import org.example.intershop.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final Cart cart;
    private final FileUploadService fileUploadService;


    @GetMapping
    public String showItems(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "3") int size,
                            @RequestParam(required = false) String sort,
                            @RequestParam(required = false) String search) {
        Pageable pageable = PageRequest.of(page, size, parseSort(sort));
        Specification<Item> spec = ItemSpecification.nameContains(search);
        Page<Item> itemPage = itemService.findAll(spec, pageable);

        model.addAttribute("items", itemPage);
        model.addAttribute("currentPage", itemPage.getNumber());
        model.addAttribute("totalPages", itemPage.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("sort", sort);
        model.addAttribute("search", search);
        model.addAttribute("cart", cart);

        return "items";
    }

    @GetMapping("/{id}")
    public String showItem(Model model, @PathVariable Long id) {
        Item item = itemService.findById(id);

        model.addAttribute("item", item);
        model.addAttribute("cart", cart);

        return "item";
    }

    @GetMapping("/new")
    public String showNewItem(Model model) {
        model.addAttribute("item", new Item());
        return "new-item";
    }

    @PostMapping
    public String createItem(@Valid @ModelAttribute Item item, BindingResult bindingResult,
                             @RequestParam MultipartFile imageFile, Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("item", item);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "new-item";
        }

        String imageFilename = fileUploadService.uploadImage(imageFile);
        item.setImage(imageFilename);
        Item savedItem = itemService.save(item);

        return "redirect:/items/" + savedItem.getId();
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
