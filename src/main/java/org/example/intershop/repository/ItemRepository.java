package org.example.intershop.repository;

import org.example.intershop.model.Item;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<@NonNull Item, @NonNull Long>, JpaSpecificationExecutor<Item> {
}
