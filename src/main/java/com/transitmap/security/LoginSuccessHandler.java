package com.transitmap.security;

import com.transitmap.entity.DemandeInscription.StatutDemande;
import com.transitmap.repository.DemandeInscriptionRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Gestionnaire de succès d'authentification.
 * Redirige chaque utilisateur vers son tableau de bord selon son rôle.
 * Les agents en attente ou rejetés sont redirigés vers la page d'attente.
 */
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final DemandeInscriptionRepository demandeRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        String username = authentication.getName();

        if (hasRole(authentication, "ROLE_ADMIN")) {
            response.sendRedirect("/admin/dashboard");

        } else if (hasRole(authentication, "ROLE_AGENT")) {
            // Vérifier si la demande est approuvée
            var demande = demandeRepository
                    .findByEmail(username).orElse(null);

            if (demande != null &&
                demande.getStatut() == StatutDemande.EN_ATTENTE) {
                response.sendRedirect("/inscription/en-attente");
            } else if (demande != null &&
                       demande.getStatut() == StatutDemande.REJETEE) {
                response.sendRedirect("/inscription/en-attente");
            } else {
                response.sendRedirect("/agent/dashboard");
            }

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