package com.transitmap.service.admin;

import com.transitmap.dto.DemandeInscriptionDto;
import com.transitmap.entity.*;
import com.transitmap.entity.DemandeInscription.StatutDemande;
import com.transitmap.exception.ResourceNotFoundException;
import com.transitmap.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implémentation du service de traitement des demandes d'inscription.
 */
@Service
@RequiredArgsConstructor
public class AdminDemandeServiceImpl implements AdminDemandeService {

    private final DemandeInscriptionRepository demandeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EntrepriseRepository entrepriseRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Retourne toutes les demandes triées par date décroissante.
     */
    @Override
    @Transactional(readOnly = true)
    public List<DemandeInscriptionDto> trouverToutes() {
        return demandeRepository.findAllByOrderByDateCreationDesc()
                .stream().map(this::mapper).toList();
    }

    /**
     * Retourne uniquement les demandes en attente de traitement.
     */
    @Override
    @Transactional(readOnly = true)
    public List<DemandeInscriptionDto> trouverEnAttente() {
        return demandeRepository
                .findByStatutOrderByDateCreationDesc(StatutDemande.EN_ATTENTE)
                .stream().map(this::mapper).toList();
    }

    /**
     * Approuve une demande et crée automatiquement le compte agent
     * ainsi que son entreprise avec les codes de paiement.
     */
    @Override
    @Transactional
    public DemandeInscriptionDto approuver(Long id, String commentaire) {

        DemandeInscription demande = trouverDemande(id);

        // Vérification que la demande est en attente
        if (demande.getStatut() != StatutDemande.EN_ATTENTE) {
            throw new RuntimeException(
                    "Cette demande a déjà été traitée");
        }

        // Création du compte utilisateur AGENT
        Role roleAgent = roleRepository.findByName("AGENT")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rôle AGENT introuvable"));

        User agent = User.builder()
                .username(demande.getEmail())
                .password(passwordEncoder.encode(
                        demande.getMotDePasseInitial()))
                .enabled(true)
                .role(roleAgent)
                .build();
        userRepository.save(agent);

        // Création de l'entreprise avec les codes de paiement
        Entreprise entreprise = Entreprise.builder()
                .nom(demande.getNomEntreprise())
                .numeroCommercial(demande.getNumeroCommercial())
                .adresse(demande.getAdresse())
                .typeService(demande.getTypeService())
                .codeBankily(demande.getCodeBankily())
                .codeMasrvi(demande.getCodeMasrvi())
                .codeSedad(demande.getCodeSedad())
                .codeClick(demande.getCodeClick())
                .codeBamis(demande.getCodeBamis())
                .codeBimbank(demande.getCodeBimbank())
                .codeBciPay(demande.getCodeBciPay())
                .agent(agent)
                .build();
        entrepriseRepository.save(entreprise);

        // Mise à jour du statut de la demande
        demande.setStatut(StatutDemande.APPROUVEE);
        demande.setCommentaireAdmin(commentaire);
        demande.setDateTraitement(LocalDateTime.now());

        return mapper(demandeRepository.save(demande));
    }

    /**
     * Rejette une demande d'inscription.
     */
    @Override
    @Transactional
    public DemandeInscriptionDto rejeter(Long id, String commentaire) {

        DemandeInscription demande = trouverDemande(id);

        if (demande.getStatut() != StatutDemande.EN_ATTENTE) {
            throw new RuntimeException(
                    "Cette demande a déjà été traitée");
        }

        demande.setStatut(StatutDemande.REJETEE);
        demande.setCommentaireAdmin(commentaire);
        demande.setDateTraitement(LocalDateTime.now());

        return mapper(demandeRepository.save(demande));
    }

    /**
     * Retourne une demande par son ID.
     */
    @Override
    @Transactional(readOnly = true)
    public DemandeInscriptionDto trouverParId(Long id) {
        return mapper(trouverDemande(id));
    }

    // === Méthodes privées ===

    /** Trouve une demande ou lève une exception */
    private DemandeInscription trouverDemande(Long id) {
        return demandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Demande introuvable"));
    }

    /** Convertit une entité DemandeInscription en DTO */
    private DemandeInscriptionDto mapper(DemandeInscription d) {
        return DemandeInscriptionDto.builder()
                .id(d.getId())
                .nomComplet(d.getNomComplet())
                .email(d.getEmail())
                .telephone(d.getTelephone())
                .numeroCIN(d.getNumeroCIN())
                .nomEntreprise(d.getNomEntreprise())
                .numeroCommercial(d.getNumeroCommercial())
                .adresse(d.getAdresse())
                .typeService(d.getTypeService())
                .description(d.getDescription())
                .codeBankily(d.getCodeBankily())
                .codeMasrvi(d.getCodeMasrvi())
                .codeSedad(d.getCodeSedad())
                .codeClick(d.getCodeClick())
                .codeBamis(d.getCodeBamis())
                .codeBimbank(d.getCodeBimbank())
                .codeBciPay(d.getCodeBciPay())
                .statut(d.getStatut())
                .dateCreation(d.getDateCreation())
                .dateTraitement(d.getDateTraitement())
                .commentaireAdmin(d.getCommentaireAdmin())
                .build();
    }
}