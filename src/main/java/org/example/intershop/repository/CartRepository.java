package org.example.intershop.repository;

import org.example.intershop.model.Cart;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<@NonNull Cart, @NonNull Long> {
}
