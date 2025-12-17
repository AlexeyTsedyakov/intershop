package org.example.intershop.repository;

import org.example.intershop.model.Item;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface ItemRepository extends JpaRepository<@NonNull Item, @NonNull Long>, JpaSpecificationExecutor<Item> {
    Set<Item> findAllByIdIn(Collection<Long> ids);
}
