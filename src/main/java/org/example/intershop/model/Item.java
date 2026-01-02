package org.example.intershop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 256)
    @Column(nullable = false)
    private String name;

    @Column
    private String image;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer price;

    @Size(max = 256)
    @Column
    private String description;
}
