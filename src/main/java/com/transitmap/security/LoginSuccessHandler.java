package com.transitmap.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Gestionnaire de succès d'authentification.
 * Redirige chaque utilisateur vers son tableau de bord selon son rôle.
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        if (hasRole(authentication, "ROLE_ADMIN")) {
            response.sendRedirect("/admin/dashboard");
        } else if (hasRole(authentication, "ROLE_AGENT")) {
            response.sendRedirect("/agent/dashboard");
        } else if (hasRole(authentication, "ROLE_CHAUFFEUR")) {
            response.sendRedirect("/chauffeur/dashboard");
        } else {
            response.sendRedirect("/map");
        }
    }

    /** Vérifie si l'utilisateur possède un rôle donné */
    private boolean hasRole(Authentication auth, String role) {
        return auth.getAuthorities().contains(
                new SimpleGrantedAuthority(role));
    }
}