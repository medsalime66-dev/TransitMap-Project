package com.transitmap.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapController {

    @GetMapping("/map")
    public String map(Authentication authentication, Model model) {
        boolean isAuth = authentication != null && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser");

        String role = "GUEST";
        if (isAuth) {
            if (authentication.getAuthorities().contains(
                    new SimpleGrantedAuthority("ROLE_ADMIN"))) role = "ADMIN";
            else if (authentication.getAuthorities().contains(
                    new SimpleGrantedAuthority("ROLE_AGENT"))) role = "AGENT";
            else if (authentication.getAuthorities().contains(
                    new SimpleGrantedAuthority("ROLE_VOYAGEUR"))) role = "VOYAGEUR";
        }

        model.addAttribute("isAuth", isAuth);
        model.addAttribute("userRole", role);
        return "map";
    }
}