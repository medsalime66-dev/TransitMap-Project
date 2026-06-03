package com.transitmap.controller.inscription;

import com.transitmap.dto.DemandeInscriptionDto;
import com.transitmap.entity.DemandeInscription;
import com.transitmap.exception.ResourceNotFoundException;
import com.transitmap.repository.DemandeInscriptionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Contrôleur public pour la soumission des demandes d'inscription des agents.
 * Accessible sans authentification.
 */
@Controller
@RequestMapping("/inscription")
@RequiredArgsConstructor
public class InscriptionController {

    private final DemandeInscriptionRepository demandeRepository;

    /** Affiche le formulaire d'inscription */
    @GetMapping
    public String formulaire(Model model) {
        model.addAttribute("demande", new DemandeInscriptionDto());
        return "public/inscription";
    }

    /** Traite la soumission du formulaire d'inscription */
    @PostMapping
    public String soumettre(
            @Valid @ModelAttribute("demande") DemandeInscriptionDto dto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "public/inscription";
        }

        // Vérification qu'au moins un code de paiement est fourni
        if (!aUnCodePaiement(dto)) {
            model.addAttribute("erreurPaiement",
                    "Au moins un code de paiement est obligatoire");
            return "public/inscription";
        }

        // Vérification que l'email n'existe pas déjà
        if (demandeRepository.existsByEmail(dto.getEmail())) {
            model.addAttribute("erreurEmail",
                    "Une demande avec cet email existe déjà");
            return "public/inscription";
        }

        // Création de la demande
        DemandeInscription demande = DemandeInscription.builder()
                .nomComplet(dto.getNomComplet())
                .email(dto.getEmail())
                .telephone(dto.getTelephone())
                .numeroCIN(dto.getNumeroCIN())
                .nomEntreprise(dto.getNomEntreprise())
                .numeroCommercial(dto.getNumeroCommercial())
                .adresse(dto.getAdresse())
                .typeService(dto.getTypeService())
                .description(dto.getDescription())
                .motDePasseInitial(dto.getMotDePasse())
                .codeBankily(dto.getCodeBankily())
                .codeMasrvi(dto.getCodeMasrvi())
                .codeSedad(dto.getCodeSedad())
                .codeClick(dto.getCodeClick())
                .codeBamis(dto.getCodeBamis())
                .codeBimbank(dto.getCodeBimbank())
                .codeBciPay(dto.getCodeBciPay())
                .statut(DemandeInscription.StatutDemande.EN_ATTENTE)
                .dateCreation(LocalDateTime.now())
                .build();

        demandeRepository.save(demande);
        return "redirect:/inscription/attente";
    }

    /** Page d'attente après soumission */
    @GetMapping("/attente")
    public String attente() {
        return "public/inscription-attente";
    }

    /** Vérifie qu'au moins un code de paiement est renseigné */
    private boolean aUnCodePaiement(DemandeInscriptionDto dto) {
        return estRenseigne(dto.getCodeBankily())
                || estRenseigne(dto.getCodeMasrvi())
                || estRenseigne(dto.getCodeSedad())
                || estRenseigne(dto.getCodeClick())
                || estRenseigne(dto.getCodeBamis())
                || estRenseigne(dto.getCodeBimbank())
                || estRenseigne(dto.getCodeBciPay());
    }

    private boolean estRenseigne(String valeur) {
        return valeur != null && !valeur.isBlank();
    }
}