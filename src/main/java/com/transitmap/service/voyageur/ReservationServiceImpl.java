package com.transitmap.service.voyageur;

import com.transitmap.dto.ReservationDto;
import com.transitmap.entity.*;
import com.transitmap.entity.Reservation.StatutReservation;
import com.transitmap.entity.Reservation.TypePaiement;
import com.transitmap.exception.ResourceNotFoundException;
import com.transitmap.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implémentation du service de réservation.
 */
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final TrajetRepository trajetRepository;
    private final ArretRepository arretRepository;
    private final UserRepository userRepository;
    private final VehiculeRepository vehiculeRepository;

    /**
     * Crée une réservation en attente de paiement.
     * Vérifie la disponibilité des places avant de créer.
     */
    @Override
    @Transactional
    public ReservationDto creerReservation(ReservationDto dto,
                                            String usernameVoyageur) {
        User voyageur = trouverUtilisateur(usernameVoyageur);
        Trajet trajet = trajetRepository.findById(dto.getTrajetId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trajet introuvable"));
        Arret arretDepart = arretRepository.findById(dto.getArretDepartId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Arrêt de départ introuvable"));
        Arret arretArrivee = arretRepository.findById(dto.getArretArriveeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Arrêt d'arrivée introuvable"));

        // Vérification des places disponibles
        if (trajet.getVehicule() != null &&
                trajet.getVehicule().getPlacesDisponibles() <= 0) {
            throw new RuntimeException(
                    "Aucune place disponible pour ce trajet");
        }

        Reservation reservation = Reservation.builder()
                .voyageur(voyageur)
                .trajet(trajet)
                .arretDepart(arretDepart)
                .arretArrivee(arretArrivee)
                .dateReservation(LocalDateTime.now())
                .typePaiement(TypePaiement.ELECTRONIQUE)
                .statut(StatutReservation.EN_ATTENTE)
                .build();

        return mapper(reservationRepository.save(reservation));
    }

    /**
     * Retourne toutes les réservations du voyageur connecté.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> mesReservations(String usernameVoyageur) {
        User voyageur = trouverUtilisateur(usernameVoyageur);
        return reservationRepository
                .findByVoyageurOrderByDateReservationDesc(voyageur)
                .stream().map(this::mapper).toList();
    }

    /**
     * Retourne une réservation par son ID.
     */
    @Override
    @Transactional(readOnly = true)
    public ReservationDto trouverParId(Long id, String usernameVoyageur) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Réservation introuvable"));
        return mapper(reservation);
    }

    /**
     * Confirme la réservation après validation du paiement.
     * Génère un QR code et un code textuel uniques.
     * Décrémente les places disponibles du véhicule.
     */
    @Override
    @Transactional
    public ReservationDto confirmerApresPaiement(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Réservation introuvable"));

        // Génération des codes uniques
        String codeTexte = genererCodeTexte();
        String codeQR = UUID.randomUUID().toString();

        reservation.setStatut(StatutReservation.CONFIRME);
        reservation.setCodeTexte(codeTexte);
        reservation.setCodeQR(codeQR);

        // Décrémenter les places du véhicule
        Vehicule vehicule = reservation.getTrajet().getVehicule();
        if (vehicule != null && vehicule.getPlacesDisponibles() > 0) {
            vehicule.setPlacesDisponibles(
                    vehicule.getPlacesDisponibles() - 1);
            vehiculeRepository.save(vehicule);
        }

        return mapper(reservationRepository.save(reservation));
    }

    /**
     * Annule une réservation en attente.
     */
    @Override
    @Transactional
    public void annuler(Long id, String usernameVoyageur) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Réservation introuvable"));

        if (reservation.getStatut() == StatutReservation.CONFIRME) {
            // Restituer la place
            Vehicule vehicule = reservation.getTrajet().getVehicule();
            if (vehicule != null) {
                vehicule.setPlacesDisponibles(
                        vehicule.getPlacesDisponibles() + 1);
                vehiculeRepository.save(vehicule);
            }
        }

        reservation.setStatut(StatutReservation.ANNULE);
        reservationRepository.save(reservation);
    }

    /**
     * Valide un code textuel saisi par le chauffeur.
     * Marque la réservation comme UTILISEE.
     */
    @Override
    @Transactional
    public ReservationDto validerCode(String codeTexte) {
        Reservation reservation = reservationRepository
                .findByCodeTexte(codeTexte)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Code invalide ou introuvable"));

        if (reservation.getStatut() != StatutReservation.CONFIRME) {
            throw new RuntimeException(
                    "Ce code a déjà été utilisé ou est invalide");
        }

        reservation.setStatut(StatutReservation.UTILISE);
        return mapper(reservationRepository.save(reservation));
    }

    /**
     * Retourne les réservations confirmées d'un trajet (pour le chauffeur).
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> reservationsEnAttenteParTrajet(Long trajetId) {
        Trajet trajet = trajetRepository.findById(trajetId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trajet introuvable"));
        return reservationRepository
                .findByTrajetAndStatut(trajet, StatutReservation.CONFIRME)
                .stream().map(this::mapper).toList();
    }

    // === Méthodes privées ===

    /** Génère un code textuel unique de 8 caractères */
    private String genererCodeTexte() {
        String code;
        do {
            code = UUID.randomUUID().toString()
                    .replace("-", "")
                    .substring(0, 8)
                    .toUpperCase();
        } while (reservationRepository.findByCodeTexte(code).isPresent());
        return code;
    }

    /** Trouve un utilisateur par son nom d'utilisateur */
    private User trouverUtilisateur(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Utilisateur introuvable"));
    }

    /** Convertit une entité Reservation en DTO */
    private ReservationDto mapper(Reservation r) {
        return ReservationDto.builder()
                .id(r.getId())
                .trajetId(r.getTrajet().getId())
                .arretDepartId(r.getArretDepart().getId())
                .arretArriveeId(r.getArretArrivee().getId())
                .arretDepartNom(r.getArretDepart().getNom())
                .arretArriveeNom(r.getArretArrivee().getNom())
                .ligneNom(r.getTrajet().getLigne().getNom())
                .dateTrajet(r.getTrajet().getDateTrajet().toString())
                .heureDepart(r.getTrajet().getHeureDepart().toString())
                .codeQR(r.getCodeQR())
                .codeTexte(r.getCodeTexte())
                .typePaiement(r.getTypePaiement())
                .statut(r.getStatut())
                .montant(r.getMontant())
                .dateReservation(r.getDateReservation())
                .build();
    }
}