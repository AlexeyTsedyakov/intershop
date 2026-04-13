package org.intershop.mvc.shared.controller;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ModelAttribute("currentUrl")
    public String currentUrl(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        String path = uri.getPath();
        String query = uri.getQuery() == null ? "" : "?" + uri.getQuery();
        return path + query;
    }
}
