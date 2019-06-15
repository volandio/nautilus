package com.nautilus.util;

import com.nautilus.model.StaticGroups;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class InfoOfCurrentUser {

    public static String ANONYMOUS_USER = "anonymousUser";

    private final AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();

    public String getLoginOfAuthUser() {
        return getAuthenticationUser().getUsername();
    }

    public boolean isAdmin() {
        return getAuthenticationUser().getAuthorities().contains((GrantedAuthority) StaticGroups.ADMIN_GROUP::getName);
    }

    public User getAuthenticationUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean anonymous = authenticationTrustResolver.isAnonymous(authentication);
        if (anonymous) {
            throw new RuntimeException("Текущий пользователь не авторизован, операция прервана!");
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (User) principal;
    }
}
