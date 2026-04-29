package org.intershop.mvc.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.intershop.mvc.shared.util.AuthUtil;
import org.intershop.mvc.user.exception.UserAlreadyExistsException;
import org.intershop.mvc.user.model.User;
import org.intershop.mvc.user.service.UserService;
import org.springframework.security.core.Authentication;
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
    private final AuthUtil authUtil;

    @GetMapping("/registration")
    public Mono<Rendering> showRegistrationForm(Authentication authentication) {
        return Mono.just(Rendering.view("user-registration")
                .modelAttribute("user", new User())
                .modelAttribute("isAuthenticated", authUtil.isAuthenticated(authentication))
                .build());
    }

    @PostMapping("/registration")
    public Mono<Rendering> register(@Valid @ModelAttribute User user, BindingResult bindingResult,
                                    Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return renderErrors(user, bindingResult, authentication);
        }

        return userService.create(user)
                .map(savedUser -> Rendering.redirectTo("/login").build())
                .onErrorResume(UserAlreadyExistsException.class,
                        e -> renderErrors(user, createUsernameError(user, e), authentication));
    }

    private Mono<Rendering> renderErrors(User user, BindingResult errors, Authentication authentication) {
        return Mono.just(Rendering.view("user-registration")
                .modelAttribute("user", user)
                .modelAttribute(BindingResult.MODEL_KEY_PREFIX + "user", errors)
                .modelAttribute("isAuthenticated", authUtil.isAuthenticated(authentication))
                .build());
    }

    private BindingResult createUsernameError(User user, Exception e) {
        var errors = new BeanPropertyBindingResult(user, "user");
        errors.addError(new FieldError("user", "username", e.getMessage()));

        return errors;
    }
}
