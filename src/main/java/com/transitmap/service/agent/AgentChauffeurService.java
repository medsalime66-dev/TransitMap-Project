package com.transitmap.service.agent;

import com.transitmap.dto.ChauffeurDto;
import java.util.List;

/**
 * Service de gestion des chauffeurs par l'agent.
 */
public interface AgentChauffeurService {

    /** Crée un nouveau chauffeur et son compte utilisateur */
    ChauffeurDto creerChauffeur(ChauffeurDto dto, String usernameAgent);

    /** Met à jour les informations d'un chauffeur */
    ChauffeurDto modifierChauffeur(Long id, ChauffeurDto dto, String usernameAgent);

    /** Assigne un chauffeur à une ligne */
    ChauffeurDto assignerLigne(Long chauffeurId, Long ligneId, String usernameAgent);

    /** Supprime l'assignation de ligne d'un chauffeur */
    ChauffeurDto retirerLigne(Long chauffeurId, String usernameAgent);

    /** Trouve tous les chauffeurs de l'agent */
    List<ChauffeurDto> trouverParAgent(String usernameAgent);

    /** Trouve un chauffeur par son ID */
    ChauffeurDto trouverParId(Long id, String usernameAgent);

    /** Supprime un chauffeur */
    void supprimerChauffeur(Long id, String usernameAgent);
}