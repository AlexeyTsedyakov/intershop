package org.intershop.mvc.user.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(name = "users")
public class User {
    @Id
    @Column("id")
    private Long id;

    @NotBlank
    @Size(max = 256)
    @Column("username")
    private String username;

    @NotBlank
    @Column("password")
    private String password;
}
