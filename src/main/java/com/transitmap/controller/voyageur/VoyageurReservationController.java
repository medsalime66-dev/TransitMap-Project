package com.transitmap.controller.voyageur;

import com.transitmap.dto.ReservationDto;
import com.transitmap.repository.ArretRepository;
import com.transitmap.repository.TrajetRepository;
import com.transitmap.service.voyageur.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Contrôleur pour la gestion des réservations du voyageur.
 */
@Controller
@RequestMapping("/voyageur/reservations")
@RequiredArgsConstructor
public class VoyageurReservationController {

    private final ReservationService reservationService;
    private final TrajetRepository trajetRepository;
    private final ArretRepository arretRepository;

    /** Liste toutes les réservations du voyageur connecté */
    @GetMapping
    public String mesReservations(Model model, Principal principal) {
        model.addAttribute("reservations",
                reservationService.mesReservations(principal.getName()));
        return "voyageur/mes-reservations";
    }

    /** Formulaire de réservation pour un trajet donné */
    @GetMapping("/creer/{trajetId}")
    public String formulaireReservation(
            @PathVariable Long trajetId, Model model) {
        var trajet = trajetRepository.findById(trajetId)
                .orElseThrow();
        var arrets = arretRepository.findByLigneIdOrderByOrdreAsc(
                trajet.getLigne().getId());
        model.addAttribute("trajet", trajet);
        model.addAttribute("arrets", arrets);
        model.addAttribute("reservation", new ReservationDto());
        return "voyageur/creer-reservation";
    }

    /** Traite la soumission du formulaire de réservation */
    @PostMapping("/creer")
    public String creer(
            @ModelAttribute ReservationDto dto,
            Principal principal) {
        var reservation = reservationService
                .creerReservation(dto, principal.getName());
        return "redirect:/voyageur/paiement/" + reservation.getId();
    }

    /** Affiche le détail d'une réservation avec QR code */
    @GetMapping("/{id}")
    public String detail(
            @PathVariable Long id,
            Model model,
            Principal principal) {
        model.addAttribute("reservation",
                reservationService.trouverParId(id, principal.getName()));
        return "voyageur/reservation-detail";
    }

    /** Annule une réservation en attente */
    @PostMapping("/annuler/{id}")
    public String annuler(
            @PathVariable Long id,
            Principal principal) {
        reservationService.annuler(id, principal.getName());
        return "redirect:/voyageur/reservations";
    }
}