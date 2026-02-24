package org.intershop.mvc.order.service;

import lombok.RequiredArgsConstructor;
import org.intershop.mvc.item.service.ItemService;
import org.intershop.mvc.order.model.OrderItem;
import org.intershop.mvc.order.repository.OrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ItemService itemService;

    @Override
    @Transactional
    public Flux<OrderItem> saveAll(Iterable<OrderItem> items) {
        return orderItemRepository.saveAll(items);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<OrderItem> findAllByOrderId(Long orderId) {
        return orderItemRepository.findAllByOrderId(orderId)
                .collectList()
                .flatMap(this::enrichItems)
                .flatMapMany(Flux::fromIterable);
    }

    private Mono<List<OrderItem>> enrichItems(List<OrderItem> orderItems) {
        if (orderItems.isEmpty()) {
            return Mono.just(orderItems);
        }

        List<Long> ids = orderItems.stream()
                .map(OrderItem::getItemId)
                .toList();

        return itemService.findAllByIdsAsMap(ids)
                .map(itemsById -> {
                    orderItems.forEach(orderItem ->
                            orderItem.setItem(itemsById.get(orderItem.getItemId())));
                    return orderItems;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Map<Long, List<OrderItem>>> findAllByOrderIdsAsMap(Collection<Long> orderIds) {
        return orderItemRepository.findAllByOrderIdIsIn(orderIds)
                .collectList()
                .flatMap(this::enrichItems)
                .map(orderItems -> orderItems.stream()
                        .collect(Collectors.groupingBy(OrderItem::getOrderId)));
    }
}
