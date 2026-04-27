package org.intershop.mvc.shared.util;

import org.intershop.mvc.user.model.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AuthUtil {
    public Long getUserId(Authentication authentication) {
        Objects.requireNonNull(authentication, "Пользователь должен быть аутентифицирован");
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Objects.requireNonNull(userPrincipal, "Нет данных пользователя");

        return userPrincipal.getId();
    }

    public boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }
}
