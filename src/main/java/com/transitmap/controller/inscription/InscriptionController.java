package com.transitmap.controller.inscription;

import com.transitmap.dto.DemandeInscriptionDto;
import com.transitmap.dto.VoyageurInscriptionDto;
import com.transitmap.entity.DemandeInscription;
import com.transitmap.entity.DemandeInscription.StatutDemande;
import com.transitmap.entity.Entreprise.TypeService;
import com.transitmap.entity.Role;
import com.transitmap.entity.User;
import com.transitmap.repository.DemandeInscriptionRepository;
import com.transitmap.repository.RoleRepository;
import com.transitmap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;

/**
 * Contrôleur pour l'inscription des voyageurs et agents.
 */
@Controller
@RequestMapping("/inscription")
@RequiredArgsConstructor
public class InscriptionController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DemandeInscriptionRepository demandeRepository;
    private final PasswordEncoder passwordEncoder;

    /** Page de choix du type de compte */
    @GetMapping("/choix")
    public String choix() {
        return "inscription/choix";
    }

    /** Formulaire inscription voyageur */
    @GetMapping("/voyageur")
    public String voyageurForm(Model model) {
        model.addAttribute("voyageur",
                new VoyageurInscriptionDto());
        return "inscription/voyageur";
    }

    /** Traitement inscription voyageur */
    @PostMapping("/voyageur")
    public String voyageurSubmit(
            @Valid @ModelAttribute("voyageur")
            VoyageurInscriptionDto dto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "inscription/voyageur";
        }

        // Vérifier si l'email est déjà utilisé
        if (userRepository.findByUsername(dto.getEmail())
                .isPresent()) {
            model.addAttribute("erreur",
                    "Cet email est déjà utilisé.");
            return "inscription/voyageur";
        }

        // Vérifier la confirmation du mot de passe
        if (!dto.getMotDePasse().equals(
                dto.getConfirmerMotDePasse())) {
            model.addAttribute("erreur",
                    "Les mots de passe ne correspondent pas.");
            return "inscription/voyageur";
        }

        // Créer le compte voyageur
        Role roleVoyageur = roleRepository
                .findByName("VOYAGEUR")
                .orElseGet(() -> roleRepository.save(
                        Role.builder().name("VOYAGEUR").build()));

        userRepository.save(User.builder()
                .username(dto.getEmail())
                .password(passwordEncoder.encode(
                        dto.getMotDePasse()))
                .enabled(true)
                .role(roleVoyageur)
                .build());

        return "redirect:/login?inscrit=true";
    }

    /** Formulaire demande agent */
    @GetMapping("/agent")
    public String agentForm(Model model) {
        model.addAttribute("demandeForm",
                new DemandeInscriptionDto());
        return "inscription/agent";
    }

    /** Traitement demande agent */
    @PostMapping("/agent")
    public String agentSubmit(
            @Valid @ModelAttribute("demandeForm")
            DemandeInscriptionDto dto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "inscription/agent";
        }

        // Vérifier si l'email est déjà utilisé
        if (demandeRepository.existsByEmail(dto.getEmail())
                || userRepository.findByUsername(
                        dto.getEmail()).isPresent()) {
            model.addAttribute("erreur",
                    "Cet email est déjà utilisé.");
            return "inscription/agent";
        }

        // Vérifier la confirmation du mot de passe
        if (!dto.getMotDePasseInitial().equals(
                dto.getConfirmerMotDePasse())) {
            model.addAttribute("erreur",
                    "Les mots de passe ne correspondent pas.");
            return "inscription/agent";
        }

        // Créer la demande
        demandeRepository.save(
                DemandeInscription.builder()
                .nomComplet(dto.getNomComplet())
                .email(dto.getEmail())
                .telephone(dto.getTelephone())
                .numeroCIN(dto.getNumeroCIN())
                .nomEntreprise(dto.getNomEntreprise())
                .numeroCommercial(dto.getNumeroCommercial())
                .adresse(dto.getAdresse())
                .typeService(TypeService.valueOf(
                        dto.getTypeService()))
                .description(dto.getDescription())
                .motDePasseInitial(passwordEncoder.encode(
                        dto.getMotDePasseInitial()))
                .codeBankily(dto.getCodeBankily())
                .codeMasrvi(dto.getCodeMasrvi())
                .codeSedad(dto.getCodeSedad())
                .codeClick(dto.getCodeClick())
                .codeBimbank(dto.getCodeBimbank())
                .codeBciPay(dto.getCodeBciPay())
                .statut(StatutDemande.EN_ATTENTE)
                .dateCreation(LocalDateTime.now())
                .build());

        return "redirect:/inscription/en-attente";
    }

    /** Page d'attente après soumission */
    @GetMapping("/en-attente")
    public String enAttente(Model model,
                             Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        var demande = demandeRepository
                .findByEmail(principal.getName())
                .orElse(null);
        if (demande == null) {
            return "redirect:/login";
        }
        model.addAttribute("demande", demande);
        return "inscription/en-attente";
    }
}