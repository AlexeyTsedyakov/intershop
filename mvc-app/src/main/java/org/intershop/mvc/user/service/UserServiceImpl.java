package org.intershop.mvc.user.service;

import lombok.RequiredArgsConstructor;
import org.intershop.mvc.user.exception.UserAlreadyExistsException;
import org.intershop.mvc.user.model.User;
import org.intershop.mvc.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Mono<User> create(User user) {
        return userRepository.findByUsernameIgnoreCase(user.getUsername())
                .flatMap(existing -> Mono.<User>error(
                        new UserAlreadyExistsException("User already exists: " + existing.getUsername())
                ))
                .switchIfEmpty(Mono.defer(() -> {
                    user.setPassword(encodePassword(user.getPassword()));
                    return userRepository.save(user);
                }));
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
