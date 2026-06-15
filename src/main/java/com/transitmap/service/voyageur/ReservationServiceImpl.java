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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final LigneInterurbaineRepository ligneRepository;
    private final VilleEtapeRepository villeEtapeRepository;
    private final HoraireInterurbaineRepository horaireRepository;
    private final PrixSegmentRepository prixSegmentRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ReservationDto creerReservation(ReservationDto dto, String usernameVoyageur) {
        User voyageur = trouverUtilisateur(usernameVoyageur);

        LigneInterurbaine ligne = ligneRepository.findById(dto.getLigneId())
                .orElseThrow(() -> new ResourceNotFoundException("Ligne introuvable"));

        VilleEtape arretDepart = villeEtapeRepository.findById(dto.getArretDepartId())
                .orElseThrow(() -> new ResourceNotFoundException("Arrêt de départ introuvable"));
        VilleEtape arretArrivee = villeEtapeRepository.findById(dto.getArretArriveeId())
                .orElseThrow(() -> new ResourceNotFoundException("Arrêt d'arrivée introuvable"));

        // Les deux arrêts doivent appartenir à la ligne
        if (!arretDepart.getLigne().getId().equals(ligne.getId())
                || !arretArrivee.getLigne().getId().equals(ligne.getId())) {
            throw new IllegalArgumentException("Les arrêts n'appartiennent pas à cette ligne");
        }
        // L'ordre du départ doit précéder l'arrivée
        if (arretDepart.getOrdre() >= arretArrivee.getOrdre()) {
            throw new IllegalArgumentException(
                    "L'arrêt de départ doit précéder l'arrêt d'arrivée");
        }

        // Horaire choisi
        HoraireInterurbain horaire = horaireRepository.findById(dto.getHoraireId())
                .orElseThrow(() -> new ResourceNotFoundException("Horaire introuvable"));
        if (!horaire.getLigne().getId().equals(ligne.getId())) {
            throw new IllegalArgumentException("Cet horaire n'appartient pas à cette ligne");
        }

        // Date du trajet (>= aujourd'hui)
        LocalDate dateTrajet = dto.getDateTrajet();
        if (dateTrajet == null || dateTrajet.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date de trajet invalide");
        }

        // Prix calculé côté serveur (jamais celui envoyé par le client)
        Double montant = prixSegmentRepository
                .findPrixEntre(ligne.getId(), arretDepart.getId(), arretArrivee.getId())
                .map(PrixSegment::getPrix)
                .orElse(horaire.getPrix());

        Reservation reservation = Reservation.builder()
                .voyageur(voyageur)
                .ligne(ligne)
                .arretDepart(arretDepart)
                .arretArrivee(arretArrivee)
                .horaire(horaire)
                .dateTrajet(dateTrajet)
                .montant(montant)
                .dateReservation(LocalDateTime.now())
                .typePaiement(dto.getTypePaiement() != null
                        ? dto.getTypePaiement() : TypePaiement.BANKILY)
                .statut(StatutReservation.EN_ATTENTE)
                .build();

        return mapper(reservationRepository.save(reservation));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> mesReservations(String usernameVoyageur) {
        User voyageur = trouverUtilisateur(usernameVoyageur);
        return reservationRepository
                .findByVoyageurOrderByDateReservationDesc(voyageur)
                .stream().map(this::mapper).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationDto trouverParId(Long id, String usernameVoyageur) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation introuvable"));
        verifierProprietaire(reservation, usernameVoyageur);
        return mapper(reservation);
    }

    @Override
    @Transactional
    public ReservationDto confirmerApresPaiement(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation introuvable"));

        reservation.setStatut(StatutReservation.CONFIRME);
        reservation.setCodeTexte(genererCodeTexte());
        reservation.setCodeQR(UUID.randomUUID().toString());

        return mapper(reservationRepository.save(reservation));
    }

    @Override
    @Transactional
    public void annuler(Long id, String usernameVoyageur) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation introuvable"));
        verifierProprietaire(reservation, usernameVoyageur);
        reservation.setStatut(StatutReservation.ANNULE);
        reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public ReservationDto validerCode(String codeTexte) {
        Reservation reservation = reservationRepository.findByCodeTexte(codeTexte)
                .orElseThrow(() -> new ResourceNotFoundException("Code invalide"));

        if (reservation.getStatut() != StatutReservation.CONFIRME) {
            throw new RuntimeException("Ce code a déjà été utilisé ou est invalide");
        }
        reservation.setStatut(StatutReservation.UTILISE);
        return mapper(reservationRepository.save(reservation));
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculerPrix(Long ligneId, Long arretDepartId, Long arretArriveeId) {
        if (ligneId == null || arretDepartId == null || arretArriveeId == null) {
            return null;
        }
        return prixSegmentRepository
                .findPrixEntre(ligneId, arretDepartId, arretArriveeId)
                .map(PrixSegment::getPrix)
                .orElse(null);
    }

    // ============================ Helpers ============================

    /** username == null  -> appel interne (ex. paiement), pas de contrôle. */
    private void verifierProprietaire(Reservation reservation, String username) {
        if (username == null) return;
        if (reservation.getVoyageur() == null
                || !username.equals(reservation.getVoyageur().getUsername())) {
            // On masque l'existence : 404 plutôt que 403
            throw new ResourceNotFoundException("Réservation introuvable");
        }
    }

    private String genererCodeTexte() {
        String code;
        do {
            code = UUID.randomUUID().toString()
                    .replace("-", "").substring(0, 8).toUpperCase();
        } while (reservationRepository.findByCodeTexte(code).isPresent());
        return code;
    }

    private User trouverUtilisateur(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
    }

    private ReservationDto mapper(Reservation r) {
        return ReservationDto.builder()
                .id(r.getId())
                .ligneId(r.getLigne().getId())
                .arretDepartId(r.getArretDepart().getId())
                .arretArriveeId(r.getArretArrivee().getId())
                .arretDepartNom(r.getArretDepart().getNomVille())
                .arretArriveeNom(r.getArretArrivee().getNomVille())
                .ligneNom(r.getLigne().getNom())
                .horaireId(r.getHoraire() != null ? r.getHoraire().getId() : null)
                .dateTrajet(r.getDateTrajet())
                .heureDepart(r.getHoraire() != null ? r.getHoraire().getHeureDepart() : null)
                .codeQR(r.getCodeQR())
                .codeTexte(r.getCodeTexte())
                .typePaiement(r.getTypePaiement())
                .statut(r.getStatut())
                .montant(r.getMontant())
                .dateReservation(r.getDateReservation())
                .build();
    }
}