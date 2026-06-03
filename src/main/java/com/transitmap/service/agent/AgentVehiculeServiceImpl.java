package com.transitmap.service.agent;

import com.transitmap.dto.VehiculeDto;
import com.transitmap.entity.*;
import com.transitmap.exception.ResourceNotFoundException;
import com.transitmap.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implémentation du service de gestion des véhicules.
 */
@Service
@RequiredArgsConstructor
public class AgentVehiculeServiceImpl implements AgentVehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final LigneRepository ligneRepository;
    private final ChauffeurRepository chauffeurRepository;
    private final UserRepository userRepository;

    /**
     * Crée un nouveau véhicule lié à une ligne.
     * Le nombre de places disponibles est initialisé à la capacité totale.
     */
    @Override
    @Transactional
    public VehiculeDto creerVehicule(VehiculeDto dto, String usernameAgent) {

        Ligne ligne = ligneRepository.findById(dto.getLigneId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ligne introuvable"));

        // Chauffeur optionnel
        Chauffeur chauffeur = null;
        if (dto.getChauffeurId() != null) {
            chauffeur = chauffeurRepository.findById(dto.getChauffeurId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Chauffeur introuvable"));
        }

        Vehicule vehicule = Vehicule.builder()
                .matricule(dto.getMatricule())
                .marque(dto.getMarque())
                .capacite(dto.getCapacite())
                .placesDisponibles(dto.getCapacite()) // initialisé à capacité max
                .annee(dto.getAnnee())
                .statut(dto.getStatut())
                .ligne(ligne)
                .chauffeur(chauffeur)
                .build();

        return mapper(vehiculeRepository.save(vehicule));
    }

    /**
     * Met à jour les informations d'un véhicule.
     */
    @Override
    @Transactional
    public VehiculeDto modifierVehicule(Long id, VehiculeDto dto,
                                         String usernameAgent) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Véhicule introuvable"));

        vehicule.setMatricule(dto.getMatricule());
        vehicule.setMarque(dto.getMarque());
        vehicule.setCapacite(dto.getCapacite());
        vehicule.setAnnee(dto.getAnnee());
        vehicule.setStatut(dto.getStatut());

        return mapper(vehiculeRepository.save(vehicule));
    }

    /**
     * Assigne un chauffeur à un véhicule.
     */
    @Override
    @Transactional
    public VehiculeDto assignerChauffeur(Long vehiculeId, Long chauffeurId,
                                          String usernameAgent) {
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Véhicule introuvable"));
        Chauffeur chauffeur = chauffeurRepository.findById(chauffeurId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Chauffeur introuvable"));

        vehicule.setChauffeur(chauffeur);
        return mapper(vehiculeRepository.save(vehicule));
    }

    /**
     * Retire l'assignation du chauffeur d'un véhicule.
     */
    @Override
    @Transactional
    public VehiculeDto retirerChauffeur(Long vehiculeId, String usernameAgent) {
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Véhicule introuvable"));
        vehicule.setChauffeur(null);
        return mapper(vehiculeRepository.save(vehicule));
    }

    /**
     * Retourne tous les véhicules appartenant aux lignes de l'agent.
     */
    @Override
    @Transactional(readOnly = true)
    public List<VehiculeDto> trouverParAgent(String usernameAgent) {
        return vehiculeRepository.findAll()
                .stream().map(this::mapper).toList();
    }

    /**
     * Retourne un véhicule par son ID.
     */
    @Override
    @Transactional(readOnly = true)
    public VehiculeDto trouverParId(Long id) {
        return mapper(vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Véhicule introuvable")));
    }

    /**
     * Supprime un véhicule.
     */
    @Override
    @Transactional
    public void supprimerVehicule(Long id, String usernameAgent) {
        vehiculeRepository.deleteById(id);
    }

    /** Convertit une entité Vehicule en DTO */
    private VehiculeDto mapper(Vehicule v) {
        return VehiculeDto.builder()
                .id(v.getId())
                .matricule(v.getMatricule())
                .marque(v.getMarque())
                .capacite(v.getCapacite())
                .placesDisponibles(v.getPlacesDisponibles())
                .annee(v.getAnnee())
                .statut(v.getStatut())
                .ligneId(v.getLigne().getId())
                .chauffeurId(v.getChauffeur() != null
                        ? v.getChauffeur().getId() : null)
                .build();
    }
}