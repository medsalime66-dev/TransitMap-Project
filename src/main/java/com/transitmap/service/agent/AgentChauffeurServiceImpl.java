package com.transitmap.service.agent;

import com.transitmap.dto.ChauffeurDto;
import com.transitmap.entity.*;
import com.transitmap.exception.ResourceNotFoundException;
import com.transitmap.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implémentation du service de gestion des chauffeurs.
 */
@Service
@RequiredArgsConstructor
public class AgentChauffeurServiceImpl implements AgentChauffeurService {

    private final ChauffeurRepository chauffeurRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LigneRepository ligneRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Crée un chauffeur et son compte utilisateur associé.
     * Le compte aura le rôle CHAUFFEUR.
     */
    @Override
    @Transactional
    public ChauffeurDto creerChauffeur(ChauffeurDto dto, String usernameAgent) {

        User agent = trouverAgent(usernameAgent);

        // Vérification unicité email et permis
        if (chauffeurRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }
        if (chauffeurRepository.existsByNumeroPermis(dto.getNumeroPermis())) {
            throw new RuntimeException("Ce numéro de permis existe déjà");
        }

        // Création du compte utilisateur CHAUFFEUR
        Role roleChauffeur = roleRepository.findByName("CHAUFFEUR")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rôle CHAUFFEUR introuvable"));

        User userChauffeur = User.builder()
                .username(dto.getEmail())
                .password(passwordEncoder.encode(dto.getMotDePasse()))
                .enabled(true)
                .role(roleChauffeur)
                .build();
        userRepository.save(userChauffeur);

        // Récupération de la ligne si fournie
        Ligne ligne = null;
        if (dto.getLigneId() != null) {
            ligne = ligneRepository.findById(dto.getLigneId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Ligne introuvable"));
        }

        // Création du chauffeur
        Chauffeur chauffeur = Chauffeur.builder()
                .nomComplet(dto.getNomComplet())
                .numeroPermis(dto.getNumeroPermis())
                .telephone(dto.getTelephone())
                .email(dto.getEmail())
                .user(userChauffeur)
                .agent(agent)
                .ligne(ligne)
                .build();

        return mapper(chauffeurRepository.save(chauffeur));
    }

    /**
     * Met à jour les informations d'un chauffeur existant.
     */
    @Override
    @Transactional
    public ChauffeurDto modifierChauffeur(Long id, ChauffeurDto dto,
                                           String usernameAgent) {
        Chauffeur chauffeur = trouverChauffeurDeLAgent(id, usernameAgent);

        chauffeur.setNomComplet(dto.getNomComplet());
        chauffeur.setTelephone(dto.getTelephone());

        return mapper(chauffeurRepository.save(chauffeur));
    }

    /**
     * Assigne une ligne à un chauffeur.
     */
    @Override
    @Transactional
    public ChauffeurDto assignerLigne(Long chauffeurId, Long ligneId,
                                       String usernameAgent) {
        Chauffeur chauffeur = trouverChauffeurDeLAgent(chauffeurId, usernameAgent);
        Ligne ligne = ligneRepository.findById(ligneId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ligne introuvable"));

        chauffeur.setLigne(ligne);
        return mapper(chauffeurRepository.save(chauffeur));
    }

    /**
     * Retire l'assignation de ligne d'un chauffeur.
     */
    @Override
    @Transactional
    public ChauffeurDto retirerLigne(Long chauffeurId, String usernameAgent) {
        Chauffeur chauffeur = trouverChauffeurDeLAgent(chauffeurId, usernameAgent);
        chauffeur.setLigne(null);
        return mapper(chauffeurRepository.save(chauffeur));
    }

    /**
     * Retourne tous les chauffeurs créés par l'agent connecté.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ChauffeurDto> trouverParAgent(String usernameAgent) {
        User agent = trouverAgent(usernameAgent);
        return chauffeurRepository.findByAgent(agent)
                .stream().map(this::mapper).toList();
    }

    /**
     * Retourne un chauffeur par son ID (vérifie qu'il appartient à l'agent).
     */
    @Override
    @Transactional(readOnly = true)
    public ChauffeurDto trouverParId(Long id, String usernameAgent) {
        return mapper(trouverChauffeurDeLAgent(id, usernameAgent));
    }

    /**
     * Supprime un chauffeur et son compte utilisateur.
     */
    @Override
    @Transactional
    public void supprimerChauffeur(Long id, String usernameAgent) {
        Chauffeur chauffeur = trouverChauffeurDeLAgent(id, usernameAgent);
        if (chauffeur.getUser() != null) {
            userRepository.delete(chauffeur.getUser());
        }
        chauffeurRepository.delete(chauffeur);
    }

    // === Méthodes privées utilitaires ===

    /** Trouve l'agent par son nom d'utilisateur */
    private User trouverAgent(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Agent introuvable : " + username));
    }

    /** Trouve un chauffeur et vérifie qu'il appartient à l'agent */
    private Chauffeur trouverChauffeurDeLAgent(Long id, String usernameAgent) {
        Chauffeur chauffeur = chauffeurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Chauffeur introuvable"));
        if (!chauffeur.getAgent().getUsername().equals(usernameAgent)) {
            throw new RuntimeException(
                    "Accès non autorisé à ce chauffeur");
        }
        return chauffeur;
    }

    /** Convertit une entité Chauffeur en DTO */
    private ChauffeurDto mapper(Chauffeur c) {
        return ChauffeurDto.builder()
                .id(c.getId())
                .nomComplet(c.getNomComplet())
                .numeroPermis(c.getNumeroPermis())
                .telephone(c.getTelephone())
                .email(c.getEmail())
                .ligneId(c.getLigne() != null ? c.getLigne().getId() : null)
                .ligneNom(c.getLigne() != null ? c.getLigne().getNom() : null)
                .build();
    }
}