package org.intershop.mvc.order.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(name = "orders")
public class Order {
    @Id
    @Column("id")
    private Long id;

    @Column("total_amount")
    private Long totalAmount;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @Transient
    private List<OrderItem> items = new ArrayList<>();

    public List<OrderItem> getItems() {
        return List.copyOf(items);
    }

    public void addItem(OrderItem item) {
        item.setOrderId(this.id);
        this.items.add(item);
    }

    public OrderItem getItemByItemId(long itemId) {
        return items.stream()
                .filter(orderItem -> orderItem.getItem().getId() == itemId)
                .findFirst()
                .orElse(null);
    }

    public long calculateTotalAmount() {
        return items.stream()
                .mapToLong(item -> (long) item.getPrice() * item.getQuantity())
                .sum();
    }
}
