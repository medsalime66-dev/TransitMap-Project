package com.transitmap.controller.chauffeur;

import com.transitmap.entity.Chauffeur;
import com.transitmap.repository.ChauffeurRepository;
import com.transitmap.repository.UserRepository;
import com.transitmap.service.voyageur.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Contrôleur du tableau de bord du chauffeur.
 * Affiche le profil, le véhicule assigné et les réservations en attente.
 */
@Controller
@RequestMapping("/chauffeur")
@RequiredArgsConstructor
public class ChauffeurDashboardController {

    private final ChauffeurRepository chauffeurRepository;
    private final UserRepository userRepository;
    private final ReservationService reservationService;

    /** Tableau de bord principal du chauffeur */
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {

        var user = userRepository.findByUsername(principal.getName())
                .orElseThrow();

        Chauffeur chauffeur = chauffeurRepository
                .findAll()
                .stream()
                .filter(c -> c.getUser() != null &&
                        c.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElse(null);

        model.addAttribute("chauffeur", chauffeur);
        model.addAttribute("vehicule",
                chauffeur != null ? chauffeur.getLigne() : null);

        return "chauffeur/dashboard";
    }

    /** Page de validation des codes de réservation */
    @GetMapping("/validation")
    public String pageValidation(Model model) {
        model.addAttribute("message", null);
        return "chauffeur/validation";
    }

    /** Traite la validation d'un code textuel */
    @PostMapping("/validation")
    public String validerCode(
            @RequestParam String codeTexte,
            Model model) {
        try {
            var reservation = reservationService.validerCode(
                    codeTexte.trim().toUpperCase());
            model.addAttribute("succes",
                    "Réservation validée — " +
                    reservation.getArretDepartNom() +
                    " → " + reservation.getArretArriveeNom());
        } catch (Exception e) {
            model.addAttribute("erreur", e.getMessage());
        }
        return "chauffeur/validation";
    }
}