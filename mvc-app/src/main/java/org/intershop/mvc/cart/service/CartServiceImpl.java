package org.intershop.mvc.cart.service;

import lombok.RequiredArgsConstructor;
import org.intershop.mvc.cart.model.Cart;
import org.intershop.mvc.item.model.Item;
import org.intershop.mvc.item.service.ItemService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {
    private final ItemService itemService;

    @Override
    public Mono<Long> getTotalAmount(Cart cart) {
        return ensureItemsLoaded(cart)
                .then(Mono.fromSupplier(cart::calculateTotalAmount));
    }

    private Mono<Void> ensureItemsLoaded(Cart cart) {
        if (cart.hasNullItems()) {
            return fetchItems(cart);
        } else {
            return Mono.empty();
        }
    }

    @Override
    public Mono<Void> fetchItems(Cart cart) {
        List<Long> ids = cart.getItemsIds();

        return itemService.findAllByIdsAsMap(ids)
                .doOnNext(itemsById -> validateFetchedItems(ids, itemsById))
                .doOnNext(itemsById -> fillItems(cart, itemsById))
                .then();
    }

    private void validateFetchedItems(List<Long> ids, Map<Long, Item> fetchedItemsById) {
        if (ids.size() != fetchedItemsById.size()) {
            throw new RuntimeException("Some items were not found");
        }
    }

    private void fillItems(Cart cart, Map<Long, Item> itemsById) {
        cart.getItems().forEach(cartItem -> {
            Item item = itemsById.get(cartItem.getItemId());
            cartItem.setItem(item);
            cartItem.setPrice(item.getPrice());
        });
    }
}
