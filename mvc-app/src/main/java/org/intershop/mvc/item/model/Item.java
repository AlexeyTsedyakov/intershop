package org.intershop.mvc.item.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "items")
public class Item {
    @Id
    @Column("id")
    private Long id;

    @NotBlank
    @Size(min = 2, max = 256)
    @Column("name")
    private String name;

    @Column("image")
    private String image;

    @NotNull
    @Min(1)
    @Column("price")
    private Integer price;

    @Size(max = 256)
    @Column("description")
    private String description;
}
