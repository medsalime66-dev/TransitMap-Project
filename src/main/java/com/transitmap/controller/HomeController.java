package com.transitmap.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur principal de redirection selon le rôle.
 */
@Controller
public class HomeController {

    /** Redirige vers le tableau de bord selon le rôle */
    @GetMapping("/")
    public String accueil(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/map";
        }
        if (hasRole(authentication, "ROLE_ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        if (hasRole(authentication, "ROLE_AGENT")) {
            return "redirect:/agent/dashboard";
        }
        if (hasRole(authentication, "ROLE_CHAUFFEUR")) {
            return "redirect:/chauffeur/dashboard";
        }
        return "redirect:/map";
    }

    /** Page de connexion */
    @GetMapping("/login")
    public String connexion(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/";
        }
        return "login";
    }

    /** Méthode utilitaire de vérification de rôle */
    private boolean hasRole(Authentication auth, String role) {
        return auth.getAuthorities()
                .contains(new SimpleGrantedAuthority(role));
    }
}