package org.intershop.mvc.user.repository;

import org.intershop.mvc.user.model.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {
    Mono<User> findByUsernameIgnoreCase(String username);
}
