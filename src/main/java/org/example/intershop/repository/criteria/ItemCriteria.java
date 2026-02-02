package org.example.intershop.repository.criteria;

import org.springframework.data.relational.core.query.Criteria;

import static org.springframework.data.relational.core.query.Criteria.empty;

public class ItemCriteria {

    public static Criteria nameContains(String search) {
        if (search != null && !search.isBlank()) {
            return Criteria
                    .where("name")
                    .like("%" + search.toLowerCase() + "%")
                    .ignoreCase(true);
        }
        return empty();
    }
}
