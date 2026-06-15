package com.transitmap.controller.voyageur;

import com.transitmap.dto.LigneInterurbaineDto;
import com.transitmap.dto.ReservationDto;
import com.transitmap.service.InterurbainService;
import com.transitmap.service.voyageur.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/voyageur")
@RequiredArgsConstructor
public class VoyageurReservationController {

    private final ReservationService reservationService;
    private final InterurbainService interurbainService;

    // ---- 1. Page de réservation (choix arrêts + horaire + date) ----
    @GetMapping("/interurbain/{ligneId}/reserver")
    public String pageReservation(@PathVariable Long ligneId, Model model) {
        LigneInterurbaineDto ligne = interurbainService.findLigneById(ligneId);
        model.addAttribute("ligne", ligne);
        model.addAttribute("aujourdhui", LocalDate.now());
        return "voyageur/reserver";
    }

    // ---- 2. Aperçu du prix (AJAX / JSON) ----
    @GetMapping("/interurbain/{ligneId}/prix")
    @ResponseBody
    public Map<String, Object> apercuPrix(
            @PathVariable Long ligneId,
            @RequestParam Long arretDepartId,
            @RequestParam Long arretArriveeId) {

        Double prix = reservationService.calculerPrix(
                ligneId, arretDepartId, arretArriveeId);
        return Map.of(
                "prix", prix != null ? prix : 0.0,
                "disponible", prix != null);
    }

    // ---- 3. Création de la réservation -> paiement ----
    @PostMapping("/interurbain/{ligneId}/reserver")
    public String creerReservation(
            @PathVariable Long ligneId,
            @RequestParam Long arretDepartId,
            @RequestParam Long arretArriveeId,
            @RequestParam Long horaireId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTrajet,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        ReservationDto dto = ReservationDto.builder()
                .ligneId(ligneId)
                .arretDepartId(arretDepartId)
                .arretArriveeId(arretArriveeId)
                .horaireId(horaireId)
                .dateTrajet(dateTrajet)
                .build();

        try {
            ReservationDto creee = reservationService.creerReservation(
                    dto, authentication.getName());
            return "redirect:/voyageur/paiement/" + creee.getId();
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("erreur", ex.getMessage());
            return "redirect:/voyageur/interurbain/" + ligneId + "/reserver";
        }
    }

    // ---- 4. Liste de mes réservations ----
    @GetMapping("/reservations")
    public String mesReservations(Authentication authentication, Model model) {
        model.addAttribute("reservations",
                reservationService.mesReservations(authentication.getName()));
        return "voyageur/mes-reservations";
    }

    // ---- 5. Détail d'une réservation ----
    @GetMapping("/reservations/{id}")
    public String detail(@PathVariable Long id,
                         Authentication authentication,
                         Model model) {
        model.addAttribute("reservation",
                reservationService.trouverParId(id, authentication.getName()));
        return "voyageur/reservation-detail";
    }

    // ---- 6. Annuler une réservation ----
    @PostMapping("/reservations/annuler/{id}")
    public String annuler(@PathVariable Long id,
                          Authentication authentication,
                          RedirectAttributes redirectAttributes) {
        reservationService.annuler(id, authentication.getName());
        redirectAttributes.addFlashAttribute("message", "Réservation annulée.");
        return "redirect:/voyageur/reservations";
    }
}