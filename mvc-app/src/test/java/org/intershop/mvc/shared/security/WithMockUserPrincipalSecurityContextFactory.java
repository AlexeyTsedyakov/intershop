package org.intershop.mvc.shared.security;

import org.intershop.mvc.user.model.UserPrincipal;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockUserPrincipalSecurityContextFactory implements WithSecurityContextFactory<WithMockUserPrincipal> {

    @Override
    public @NonNull SecurityContext createSecurityContext(@NonNull WithMockUserPrincipal annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserPrincipal principal = UserPrincipal.builder()
                .id(annotation.id())
                .username(annotation.username())
                .password(annotation.password())
                .authorities(AuthorityUtils.createAuthorityList(annotation.authorities()))
                .build();
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        context.setAuthentication(auth);

        return context;
    }

}
