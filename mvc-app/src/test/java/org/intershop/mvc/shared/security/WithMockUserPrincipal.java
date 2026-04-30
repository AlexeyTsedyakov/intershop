package org.intershop.mvc.shared.security;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockUserPrincipalSecurityContextFactory.class)
@WithMockUser
public @interface WithMockUserPrincipal {
    long id() default 1L;

    String username() default "user";

    String password() default "password";

    String[] authorities() default {"ROLE_USER"};
}
