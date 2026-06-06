package com.transitmap.service.agent;

import com.transitmap.dto.VehiculeDto;
import java.util.List;

/**
 * Service de gestion des véhicules par l'agent.
 */
public interface AgentVehiculeService {

    /** Crée un nouveau véhicule */
    VehiculeDto creerVehicule(VehiculeDto dto, String usernameAgent);

    /** Met à jour un véhicule */
    VehiculeDto modifierVehicule(Long id, VehiculeDto dto, String usernameAgent);

    /** Assigne un chauffeur à un véhicule */
    VehiculeDto assignerChauffeur(Long vehiculeId, Long chauffeurId,
                                   String usernameAgent);

    /** Retire l'assignation du chauffeur */
    VehiculeDto retirerChauffeur(Long vehiculeId, String usernameAgent);

    /** Trouve tous les véhicules de l'agent */
    List<VehiculeDto> trouverParAgent(String usernameAgent);

    /** Trouve un véhicule par son ID */
    VehiculeDto trouverParId(Long id);

    /** Supprime un véhicule */
    void supprimerVehicule(Long id, String usernameAgent);
}