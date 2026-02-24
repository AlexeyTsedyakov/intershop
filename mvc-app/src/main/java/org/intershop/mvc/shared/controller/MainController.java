package org.intershop.mvc.shared.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
public class MainController {
    @GetMapping
    public Mono<Rendering> showMain() {
        return Mono.just(Rendering.redirectTo("/items").build());
    }
}
