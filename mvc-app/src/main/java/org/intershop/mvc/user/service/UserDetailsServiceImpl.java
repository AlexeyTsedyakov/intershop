package org.intershop.mvc.user.service;

import lombok.RequiredArgsConstructor;
import org.intershop.mvc.user.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {
    private final UserRepository userRepository;

    @Override
    @NonNull
    public Mono<UserDetails> findByUsername(@NonNull String username) {
        return userRepository.findByUsernameIgnoreCase(username)
                .switchIfEmpty(Mono.error(() ->
                        new UsernameNotFoundException("User not found: " + username)))
                .map(user -> User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .build());
    }
}
