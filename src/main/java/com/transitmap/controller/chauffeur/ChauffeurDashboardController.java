package com.transitmap.controller.chauffeur;

import com.transitmap.dto.ChauffeurDto;
import com.transitmap.entity.Chauffeur;
import com.transitmap.repository.ChauffeurRepository;
import com.transitmap.repository.UserRepository;
import com.transitmap.service.voyageur.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/chauffeur")
@RequiredArgsConstructor
public class ChauffeurDashboardController {

    private final ChauffeurRepository chauffeurRepository;
    private final UserRepository userRepository;
    private final ReservationService reservationService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        var user = userRepository.findByUsername(principal.getName()).orElseThrow();

        Chauffeur chauffeur = chauffeurRepository.findAll()
                .stream()
                .filter(c -> c.getUser() != null && c.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElse(null);

        ChauffeurDto dto = chauffeur == null ? null : ChauffeurDto.builder()
                .id(chauffeur.getId())
                .nomComplet(chauffeur.getNomComplet())
                .email(chauffeur.getEmail())
                .telephone(chauffeur.getTelephone())
                .numeroPermis(chauffeur.getNumeroPermis())
                .build();

        model.addAttribute("chauffeur", dto);
        return "chauffeur/dashboard";
    }

    @GetMapping("/validation")
    public String pageValidation(Model model) {
        model.addAttribute("message", null);
        return "chauffeur/validation";
    }

    @PostMapping("/validation")
    public String validerCode(@RequestParam String codeTexte, Model model) {
        try {
            var reservation = reservationService.validerCode(codeTexte.trim().toUpperCase());
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