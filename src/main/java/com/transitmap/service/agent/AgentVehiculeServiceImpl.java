package com.transitmap.service.agent;

import com.transitmap.dto.VehiculeDto;
import com.transitmap.entity.*;
import com.transitmap.exception.ResourceNotFoundException;
import com.transitmap.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentVehiculeServiceImpl implements AgentVehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final ChauffeurRepository chauffeurRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public VehiculeDto creerVehicule(VehiculeDto dto, String usernameAgent) {
        Chauffeur chauffeur = null;
        if (dto.getChauffeurId() != null) {
            chauffeur = chauffeurRepository.findById(dto.getChauffeurId())
                    .orElseThrow(() -> new ResourceNotFoundException("Chauffeur introuvable"));
        }

        Vehicule vehicule = Vehicule.builder()
                .matricule(dto.getMatricule())
                .marque(dto.getMarque())
                .capacite(dto.getCapacite())
                .placesDisponibles(dto.getCapacite())
                .annee(dto.getAnnee())
                .statut(dto.getStatut())
                .chauffeur(chauffeur)
                .build();

        return mapper(vehiculeRepository.save(vehicule));
    }

    @Override
    @Transactional
    public VehiculeDto modifierVehicule(Long id, VehiculeDto dto, String usernameAgent) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable"));
        vehicule.setMatricule(dto.getMatricule());
        vehicule.setMarque(dto.getMarque());
        vehicule.setCapacite(dto.getCapacite());
        vehicule.setAnnee(dto.getAnnee());
        vehicule.setStatut(dto.getStatut());
        return mapper(vehiculeRepository.save(vehicule));
    }

    @Override
    @Transactional
    public VehiculeDto assignerChauffeur(Long vehiculeId, Long chauffeurId, String usernameAgent) {
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable"));
        Chauffeur chauffeur = chauffeurRepository.findById(chauffeurId)
                .orElseThrow(() -> new ResourceNotFoundException("Chauffeur introuvable"));
        vehicule.setChauffeur(chauffeur);
        return mapper(vehiculeRepository.save(vehicule));
    }

    @Override
    @Transactional
    public VehiculeDto retirerChauffeur(Long vehiculeId, String usernameAgent) {
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable"));
        vehicule.setChauffeur(null);
        return mapper(vehiculeRepository.save(vehicule));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculeDto> trouverParAgent(String usernameAgent) {
        return vehiculeRepository.findAll().stream().map(this::mapper).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VehiculeDto trouverParId(Long id) {
        return mapper(vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable")));
    }

    @Override
    @Transactional
    public void supprimerVehicule(Long id, String usernameAgent) {
        vehiculeRepository.deleteById(id);
    }

    private VehiculeDto mapper(Vehicule v) {
        return VehiculeDto.builder()
                .id(v.getId())
                .matricule(v.getMatricule())
                .marque(v.getMarque())
                .capacite(v.getCapacite())
                .placesDisponibles(v.getPlacesDisponibles())
                .annee(v.getAnnee())
                .statut(v.getStatut())
                .chauffeurId(v.getChauffeur() != null ? v.getChauffeur().getId() : null)
                .build();
    }
}
