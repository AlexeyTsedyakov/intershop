package org.example.intershop.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(name = "order_items")
public class OrderItem {
    @Id
    @Column("id")
    private Long id;

    @Column("order_id")
    private Long orderId;

    @Column("item_id")
    private Long itemId;

    @Column("quantity")
    private Integer quantity;

    @Column("price")
    private Integer price;

    @Transient
    private Item item;

    public OrderItem(Item item, Integer quantity, Integer price) {
        this.item = item;
        this.itemId = item.getId();
        this.quantity = quantity;
        this.price = price;
    }
}
