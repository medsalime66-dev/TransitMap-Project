package com.transitmap.controller.voyageur;

import com.transitmap.entity.Paiement;
import com.transitmap.entity.Paiement.MethodePaiement;
import com.transitmap.entity.Paiement.StatutPaiement;
import com.transitmap.entity.Reservation;
import com.transitmap.repository.EntrepriseRepository;
import com.transitmap.repository.PaiementRepository;
import com.transitmap.repository.ReservationRepository;
import com.transitmap.service.voyageur.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Contrôleur pour le processus de paiement du voyageur.
 */
@Controller
@RequestMapping("/voyageur/paiement")
@RequiredArgsConstructor
public class VoyageurPaiementController {

    private final ReservationRepository reservationRepository;
    private final PaiementRepository paiementRepository;
    private final EntrepriseRepository entrepriseRepository;
    private final ReservationService reservationService;

    /** Page de sélection de la méthode de paiement */
    @GetMapping("/{reservationId}")
    public String choisirMethode(
            @PathVariable Long reservationId,
            Model model) {

        var reservation = reservationRepository
                .findById(reservationId).orElseThrow();

        // Trouver l'entreprise de l'agent lié à la ligne du trajet
        var entrepriseOpt = entrepriseRepository.findAll()
                .stream().findFirst();

        model.addAttribute("reservation", reservation);
        model.addAttribute("entreprise", entrepriseOpt.orElse(null));
        model.addAttribute("methodes", MethodePaiement.values());

        return "voyageur/paiement-methode";
    }

    /** Page de confirmation avec code commerçant */
    @GetMapping("/{reservationId}/confirmer")
    public String pageConfirmation(
            @PathVariable Long reservationId,
            @RequestParam String methode,
            Model model) {

        var reservation = reservationRepository
                .findById(reservationId).orElseThrow();

        // Récupérer le code commerçant selon la méthode choisie
        var entrepriseOpt = entrepriseRepository.findAll()
                .stream().findFirst();

        String codeCommerçant = "";
        if (entrepriseOpt.isPresent()) {
            var ent = entrepriseOpt.get();
            codeCommerçant = switch (methode) {
                case "BANKILY" -> ent.getCodeBankily() != null
                        ? ent.getCodeBankily() : "";
                case "MASRVI"  -> ent.getCodeMasrvi() != null
                        ? ent.getCodeMasrvi() : "";
                case "SEDAD"   -> ent.getCodeSedad() != null
                        ? ent.getCodeSedad() : "";
                case "CLICK"   -> ent.getCodeClick() != null
                        ? ent.getCodeClick() : "";
                case "BAMIS"   -> ent.getCodeBamis() != null
                        ? ent.getCodeBamis() : "";
                case "BIMBANK" -> ent.getCodeBimbank() != null
                        ? ent.getCodeBimbank() : "";
                case "BCIPAY"  -> ent.getCodeBciPay() != null
                        ? ent.getCodeBciPay() : "";
                default -> "";
            };
        }

        model.addAttribute("reservation", reservation);
        model.addAttribute("methode", methode);
        model.addAttribute("codeCommerçant", codeCommerçant);

        return "voyageur/paiement-confirmation";
    }

    /** Valide le code de transaction et confirme la réservation */
    @PostMapping("/{reservationId}/valider")
    public String validerPaiement(
            @PathVariable Long reservationId,
            @RequestParam String methode,
            @RequestParam String codeTransaction) {

        Reservation reservation = reservationRepository
                .findById(reservationId).orElseThrow();

        // Enregistrement du paiement (validation automatique)
        Paiement paiement = Paiement.builder()
                .reservation(reservation)
                .methode(MethodePaiement.valueOf(methode))
                .montant(reservation.getMontant() != null
                        ? reservation.getMontant() : 0.0)
                .codeTransaction(codeTransaction)
                .dateValidation(LocalDateTime.now())
                .statut(StatutPaiement.VALIDE)
                .build();
        paiementRepository.save(paiement);

        // Confirmation + génération QR
        reservationService.confirmerApresPaiement(reservationId);

        return "redirect:/voyageur/reservations/" + reservationId;
    }
}