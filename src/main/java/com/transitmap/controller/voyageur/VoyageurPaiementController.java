package com.transitmap.controller.voyageur;

import com.transitmap.dto.ReservationDto;
import com.transitmap.entity.Entreprise;
import com.transitmap.entity.Paiement;
import com.transitmap.entity.Paiement.MethodePaiement;
import com.transitmap.entity.Paiement.StatutPaiement;
import com.transitmap.entity.Reservation;
import com.transitmap.repository.EntrepriseRepository;
import com.transitmap.repository.PaiementRepository;
import com.transitmap.repository.ReservationRepository;
import com.transitmap.service.voyageur.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/voyageur/paiement")
@RequiredArgsConstructor
public class VoyageurPaiementController {

    private final ReservationRepository reservationRepository;
    private final PaiementRepository paiementRepository;
    private final EntrepriseRepository entrepriseRepository;
    private final ReservationService reservationService;

    // 1. Choix de la méthode de paiement
    @GetMapping("/{reservationId}")
    public String choisirMethode(@PathVariable Long reservationId,
                                 Authentication authentication, Model model) {

        // Lève 404 si la réservation n'appartient pas au voyageur connecté
        ReservationDto reservation =
                reservationService.trouverParId(reservationId, authentication.getName());

        model.addAttribute("reservation", reservation);
        return "voyageur/paiement-methode";
    }

    // 2. Page de confirmation (code commerçant selon la méthode)
    @GetMapping("/{reservationId}/confirmer")
    public String pageConfirmation(@PathVariable Long reservationId,
                                   @RequestParam String methode,
                                   Authentication authentication, Model model) {

        ReservationDto reservation =
                reservationService.trouverParId(reservationId, authentication.getName());

        // Entreprise exploitant la ligne (sinon, repli sur la première)
        Entreprise ent = entrepriseRepository.findByLigneId(reservation.getLigneId())
                .orElseGet(() -> entrepriseRepository.findAll()
                        .stream().findFirst().orElse(null));

        String codeCommercant = "";
        if (ent != null) {
            codeCommercant = switch (methode) {
                case "BANKILY" -> nz(ent.getCodeBankily());
                case "MASRVI"  -> nz(ent.getCodeMasrvi());
                case "SEDAD"   -> nz(ent.getCodeSedad());
                case "CLICK"   -> nz(ent.getCodeClick());
                case "BAMIS"   -> nz(ent.getCodeBamis());
                case "BIMBANK" -> nz(ent.getCodeBimbank());
                case "BCIPAY"  -> nz(ent.getCodeBciPay());
                default -> "";
            };
        }

        model.addAttribute("reservation", reservation);
        model.addAttribute("methode", methode);
        model.addAttribute("codeCommerçant", codeCommercant);
        return "voyageur/paiement-confirmation";
    }

    // 3. Validation du paiement
    @PostMapping("/{reservationId}/valider")
    public String validerPaiement(@PathVariable Long reservationId,
                                  @RequestParam String methode,
                                  @RequestParam String codeTransaction,
                                  Authentication authentication) {

        // Vérifie la propriété AVANT toute écriture (404 sinon)
        reservationService.trouverParId(reservationId, authentication.getName());

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow();

        Paiement paiement = Paiement.builder()
                .reservation(reservation)
                .methode(MethodePaiement.valueOf(methode))
                .montant(reservation.getMontant() != null ? reservation.getMontant() : 0.0)
                .codeTransaction(codeTransaction)
                .dateValidation(LocalDateTime.now())
                .statut(StatutPaiement.VALIDE)
                .build();
        paiementRepository.save(paiement);

        reservationService.confirmerApresPaiement(reservationId);
        return "redirect:/voyageur/reservations/" + reservationId;
    }

    private String nz(String s) {
        return s != null ? s : "";
    }
}