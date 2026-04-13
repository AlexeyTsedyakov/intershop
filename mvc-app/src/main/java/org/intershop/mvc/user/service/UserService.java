package org.intershop.mvc.user.service;

import org.intershop.mvc.user.model.User;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> create(User user);
}
