package org.intershop.mvc.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.intershop.mvc.user.exception.UserAlreadyExistsException;
import org.intershop.mvc.user.model.User;
import org.intershop.mvc.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/registration")
    public Mono<Rendering> showRegistrationForm() {
        return Mono.just(Rendering.view("user-registration")
                .modelAttribute("user", new User())
                .build());
    }

    @PostMapping("/registration")
    public Mono<Rendering> register(@Valid @ModelAttribute User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return renderErrors(user, bindingResult);
        }

        return userService.create(user)
                .map(savedUser -> Rendering.redirectTo("/login").build())
                .onErrorResume(UserAlreadyExistsException.class,
                        e -> renderErrors(user, createUsernameError(user, e)));
    }

    private Mono<Rendering> renderErrors(User user, BindingResult errors) {
        return Mono.just(Rendering.view("user-registration")
                .modelAttribute("user", user)
                .modelAttribute(BindingResult.MODEL_KEY_PREFIX + "user", errors)
                .build());
    }

    private BindingResult createUsernameError(User user, Exception e) {
        var errors = new BeanPropertyBindingResult(user, "user");
        errors.addError(new FieldError("user", "username", e.getMessage()));

        return errors;
    }
}
