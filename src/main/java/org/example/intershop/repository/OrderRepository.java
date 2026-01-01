package org.example.intershop.repository;

import org.example.intershop.model.Order;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @NonNull
    @EntityGraph(attributePaths = {"items", "items.item"})
    Optional<Order> findById(Long id);

    @NonNull
    @EntityGraph(attributePaths = {"items", "items.item"})
    List<Order> findAll();
}
