package com.transitmap.config;

import com.transitmap.entity.*;
import com.transitmap.entity.Entreprise.TypeService;
import com.transitmap.entity.DemandeInscription.StatutDemande;
import com.transitmap.entity.Reservation.StatutReservation;
import com.transitmap.entity.Reservation.TypePaiement;
import com.transitmap.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Initialisation complète des données de démonstration.
 * Idempotent : peut être relancé sans créer de doublons.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final EntrepriseRepository entrepriseRepository;
    private final DemandeInscriptionRepository demandeRepository;
    private final LigneInterurbaineRepository ligneInterurbaineRepository;
    private final VilleEtapeRepository villeEtapeRepository;
    private final HoraireInterurbaineRepository horaireRepository;
    private final PrixSegmentRepository prixSegmentRepository;
    private final ChauffeurRepository chauffeurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final ReservationRepository reservationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // ===================== RÔLES =====================
        Role roleAdmin     = creerRole("ADMIN");
        Role roleAgent     = creerRole("AGENT");
        Role roleVoyageur  = creerRole("VOYAGEUR");
        Role roleChauffeur = creerRole("CHAUFFEUR");

        // ===================== UTILISATEURS =====================
        creerUtilisateur("admin", "admin123", roleAdmin);
        User agent1     = creerUtilisateur("agent1@transitmap.mr", "agent123", roleAgent);
        User agent2     = creerUtilisateur("agent2@transitmap.mr", "agent123", roleAgent);
        User voyageur1  = creerUtilisateur("voyageur1@transitmap.mr", "voyageur123", roleVoyageur);
        User voyageur2  = creerUtilisateur("voyageur2@transitmap.mr", "voyageur123", roleVoyageur);

        // ===================== ENTREPRISES =====================
        if (entrepriseRepository.findByAgent(agent1).isEmpty()) {
            entrepriseRepository.save(Entreprise.builder()
                    .nom("Transport Nord Mauritanie")
                    .numeroCommercial("TRN-2024-001")
                    .adresse("Nouakchott, Mauritanie")
                    .typeService(TypeService.INTERURBAIN)
                    .codeBankily("013274")
                    .codeMasrvi("MSR-7821")
                    .codeSedad("SDD-4412")
                    .agent(agent1)
                    .build());
        }
        if (entrepriseRepository.findByAgent(agent2).isEmpty()) {
            entrepriseRepository.save(Entreprise.builder()
                    .nom("Société Sud Transport")
                    .numeroCommercial("SST-2024-002")
                    .adresse("Nouakchott, Mauritanie")
                    .typeService(TypeService.INTERURBAIN)
                    .codeBankily("027431")
                    .codeClick("CLK-9934")
                    .agent(agent2)
                    .build());
        }

        // ===================== DEMANDES D'INSCRIPTION =====================
        if (!demandeRepository.existsByEmail("demande1@transport.mr")) {
            demandeRepository.save(DemandeInscription.builder()
                    .nomComplet("Fatima Mint Ahmed").email("demande1@transport.mr")
                    .telephone("44556677").numeroCIN("MR-12345")
                    .nomEntreprise("Transport Oasis SARL").numeroCommercial("TO-2024-003")
                    .adresse("Atar, Mauritanie").typeService(TypeService.INTERURBAIN)
                    .description("Spécialisée dans les trajets Adrar")
                    .motDePasseInitial("pass123").codeBankily("098765")
                    .statut(StatutDemande.EN_ATTENTE)
                    .dateCreation(LocalDateTime.now().minusDays(2))
                    .build());
        }
        if (!demandeRepository.existsByEmail("demande2@transport.mr")) {
            demandeRepository.save(DemandeInscription.builder()
                    .nomComplet("Cheikh Ould Baba").email("demande2@transport.mr")
                    .telephone("33221100").numeroCIN("MR-67890")
                    .nomEntreprise("Sahel Voyages").numeroCommercial("SV-2024-004")
                    .adresse("Kiffa, Mauritanie").typeService(TypeService.INTERURBAIN)
                    .description("Desserte de l'Assaba")
                    .motDePasseInitial("pass123").codeMasrvi("MSR-5500")
                    .statut(StatutDemande.APPROUVEE)
                    .dateCreation(LocalDateTime.now().minusDays(10))
                    .dateTraitement(LocalDateTime.now().minusDays(9))
                    .commentaireAdmin("Dossier complet, approuvé.")
                    .build());
        }
        if (!demandeRepository.existsByEmail("demande3@transport.mr")) {
            demandeRepository.save(DemandeInscription.builder()
                    .nomComplet("Aminetou Mint Sidi").email("demande3@transport.mr")
                    .telephone("22113344").numeroCIN("MR-24680")
                    .nomEntreprise("Express Trarza").numeroCommercial("ET-2024-005")
                    .adresse("Rosso, Mauritanie").typeService(TypeService.INTERURBAIN)
                    .description("Demande incomplète")
                    .motDePasseInitial("pass123").codeSedad("SDD-3300")
                    .statut(StatutDemande.REJETEE)
                    .dateCreation(LocalDateTime.now().minusDays(6))
                    .dateTraitement(LocalDateTime.now().minusDays(5))
                    .commentaireAdmin("Numéro commercial non vérifiable.")
                    .build());
        }

        // ===================== DONNÉES MÉTIER (une seule fois) =====================
        if (ligneInterurbaineRepository.count() > 0) return;

        // --- Ligne 1 : Nouakchott — Nouadhibou ---
        LigneInterurbaine nktNdb = ligneInterurbaineRepository.save(LigneInterurbaine.builder()
                .nom("Nouakchott — Nouadhibou Express")
                .villeDepart("Nouakchott").villeArrivee("Nouadhibou")
                .description("Ligne principale côtière").build());

        VilleEtape ndb_nkt = etape(nktNdb, "Nouakchott", 18.0735, -15.9582, 1, true,  false);
        VilleEtape ndb_akj = etape(nktNdb, "Akjoujt",    19.7455, -14.3847, 2, false, false);
        VilleEtape ndb_ndb = etape(nktNdb, "Nouadhibou", 20.9310, -17.0347, 3, false, true);

        prix(nktNdb, ndb_nkt, ndb_akj, 1500.0);
        prix(nktNdb, ndb_nkt, ndb_ndb, 3500.0);
        prix(nktNdb, ndb_akj, ndb_ndb, 2200.0);

        HoraireInterurbain h_ndb_matin = horaire(nktNdb, 6, 0, 13, 0, "QUOTIDIEN", 3500.0);
        HoraireInterurbain h_ndb_soir  = horaire(nktNdb, 20, 0, 3, 0, "QUOTIDIEN", 3500.0);

        // --- Ligne 2 : Nouakchott — Atar ---
        LigneInterurbaine nktAtar = ligneInterurbaineRepository.save(LigneInterurbaine.builder()
                .nom("Nouakchott — Atar")
                .villeDepart("Nouakchott").villeArrivee("Atar")
                .description("Route de l'Adrar").build());

        VilleEtape atar_nkt = etape(nktAtar, "Nouakchott", 18.0735, -15.9582, 1, true,  false);
        VilleEtape atar_akj = etape(nktAtar, "Akjoujt",    19.7455, -14.3847, 2, false, false);
        VilleEtape atar_ata = etape(nktAtar, "Atar",       20.5170, -13.0490, 3, false, true);

        prix(nktAtar, atar_nkt, atar_akj, 1200.0);
        prix(nktAtar, atar_nkt, atar_ata, 3000.0);
        prix(nktAtar, atar_akj, atar_ata, 2000.0);

        HoraireInterurbain h_atar_1 = horaire(nktAtar, 5, 0, 11, 30, "LUNDI,MERCREDI,VENDREDI", 3000.0);
        HoraireInterurbain h_atar_2 = horaire(nktAtar, 14, 0, 20, 30, "MARDI,JEUDI,SAMEDI", 3000.0);

        // --- Ligne 3 : Nouakchott — Rosso ---
        LigneInterurbaine nktRosso = ligneInterurbaineRepository.save(LigneInterurbaine.builder()
                .nom("Nouakchott — Rosso")
                .villeDepart("Nouakchott").villeArrivee("Rosso")
                .description("Liaison vers le fleuve Sénégal").build());

        VilleEtape rosso_nkt  = etape(nktRosso, "Nouakchott", 18.0735, -15.9582, 1, true,  false);
        VilleEtape rosso_bout = etape(nktRosso, "Boutilimit", 17.5500, -14.6833, 2, false, false);
        VilleEtape rosso_ros  = etape(nktRosso, "Rosso",      16.5133, -15.8050, 3, false, true);

        prix(nktRosso, rosso_nkt, rosso_bout, 700.0);
        prix(nktRosso, rosso_nkt, rosso_ros, 1500.0);
        prix(nktRosso, rosso_bout, rosso_ros, 900.0);

        HoraireInterurbain h_rosso_matin = horaire(nktRosso, 7, 0, 10, 0, "QUOTIDIEN", 1500.0);
        HoraireInterurbain h_rosso_aprem = horaire(nktRosso, 14, 0, 17, 0, "QUOTIDIEN", 1500.0);
        // Ligne 1 et 2 -> agent1 ;  Ligne 3 -> agent2
        nktNdb.setCreateur(agent1);   ligneInterurbaineRepository.save(nktNdb);
        nktAtar.setCreateur(agent1);  ligneInterurbaineRepository.save(nktAtar);
        nktRosso.setCreateur(agent2); ligneInterurbaineRepository.save(nktRosso);
        // ===================== CHAUFFEURS (+ comptes) =====================
        Chauffeur ch1 = creerChauffeur("Mohamed Lemine Ould Cheikh", "PRM-001", "22001122",
                "chauffeur1@transitmap.mr", "chauffeur123", agent1, roleChauffeur);
        Chauffeur ch2 = creerChauffeur("Sidi Ahmed Ould Brahim", "PRM-002", "22003344",
                "chauffeur2@transitmap.mr", "chauffeur123", agent1, roleChauffeur);
        Chauffeur ch3 = creerChauffeur("Brahim Vall Ould Mohamed", "PRM-003", "22005566",
                "chauffeur3@transitmap.mr", "chauffeur123", agent2, roleChauffeur);

        // ===================== VÉHICULES =====================
        creerVehicule("NKC-1234", "Mercedes Sprinter", 18, 2019, "EN_SERVICE",
                19.7455, -14.3847, ch1);
        creerVehicule("NKC-5678", "Toyota Coaster", 30, 2021, "DISPONIBLE",
                18.0735, -15.9582, ch2);
        creerVehicule("NDB-9012", "Higer Bus", 45, 2020, "MAINTENANCE",
                20.9310, -17.0347, ch3);

        // ===================== RÉSERVATIONS (démonstration) =====================
        // voyageur1
        seedReservation(voyageur1, nktNdb, ndb_nkt, ndb_ndb, h_ndb_matin,
                LocalDate.now().plusDays(3), 3500.0, TypePaiement.BANKILY,
                StatutReservation.CONFIRME, "NDB12345", "QR-RES-NDB-0001", 1);

        seedReservation(voyageur1, nktAtar, atar_nkt, atar_ata, h_atar_1,
                LocalDate.now().plusDays(5), 3000.0, TypePaiement.MASRVI,
                StatutReservation.EN_ATTENTE, null, null, 0);

        seedReservation(voyageur1, nktRosso, rosso_nkt, rosso_ros, h_rosso_matin,
                LocalDate.now().minusDays(2), 1500.0, TypePaiement.BANKILY,
                StatutReservation.UTILISE, "ROS98765", "QR-RES-ROS-0002", 4);

        // voyageur2
        seedReservation(voyageur2, nktNdb, ndb_nkt, ndb_akj, h_ndb_soir,
                LocalDate.now().plusDays(1), 1500.0, TypePaiement.SEDAD,
                StatutReservation.CONFIRME, "AKJ55667", "QR-RES-AKJ-0003", 1);

        seedReservation(voyageur2, nktAtar, atar_nkt, atar_ata, h_atar_2,
                LocalDate.now().plusDays(7), 3000.0, TypePaiement.CLICK,
                StatutReservation.ANNULE, null, null, 3);
    }

    // ============================ HELPERS ============================

    private Role creerRole(String nom) {
        return roleRepository.findByName(nom)
                .orElseGet(() -> roleRepository.save(Role.builder().name(nom).build()));
    }

    private User creerUtilisateur(String username, String motDePasse, Role role) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.save(User.builder()
                        .username(username)
                        .password(passwordEncoder.encode(motDePasse))
                        .enabled(true)
                        .role(role)
                        .build()));
    }

    private VilleEtape etape(LigneInterurbaine ligne, String nom, double lat, double lng,
                             int ordre, boolean depart, boolean arrivee) {
        return villeEtapeRepository.save(VilleEtape.builder()
                .ligne(ligne).nomVille(nom).latitude(lat).longitude(lng)
                .ordre(ordre).estDepart(depart).estArrivee(arrivee).build());
    }

    private void prix(LigneInterurbaine ligne, VilleEtape a, VilleEtape b, double montant) {
        prixSegmentRepository.save(PrixSegment.builder()
                .ligne(ligne).arretA(a).arretB(b).prix(montant).build());
    }

    private HoraireInterurbain horaire(LigneInterurbaine ligne, int hd, int md, int ha, int ma,
                                       String jours, double prix) {
        return horaireRepository.save(HoraireInterurbain.builder()
                .ligne(ligne)
                .heureDepart(LocalTime.of(hd, md))
                .heureArrivee(LocalTime.of(ha, ma))
                .jours(jours).prix(prix).actif(true).build());
    }

    private Chauffeur creerChauffeur(String nom, String permis, String tel, String email,
                                     String motDePasse, User agent, Role roleChauffeur) {
        User user = creerUtilisateur(email, motDePasse, roleChauffeur);
        return chauffeurRepository.findByEmail(email).orElseGet(() ->
                chauffeurRepository.save(Chauffeur.builder()
                        .nomComplet(nom).numeroPermis(permis).telephone(tel).email(email)
                        .user(user).agent(agent).build()));
    }

    private Vehicule creerVehicule(String matricule, String marque, int capacite, int annee,
                                   String statut, Double lat, Double lng, Chauffeur chauffeur) {
        return vehiculeRepository.findByMatricule(matricule).orElseGet(() ->
                vehiculeRepository.save(Vehicule.builder()
                        .matricule(matricule).marque(marque).capacite(capacite)
                        .placesDisponibles(capacite).annee(annee).statut(statut)
                        .latitudeActuelle(lat).longitudeActuelle(lng)
                        .chauffeur(chauffeur).build()));
    }

    private void seedReservation(User voyageur, LigneInterurbaine ligne, VilleEtape dep,
                                 VilleEtape arr, HoraireInterurbain horaire, LocalDate dateTrajet,
                                 double montant, TypePaiement type, StatutReservation statut,
                                 String codeTexte, String codeQR, int joursAvantReservation) {
        reservationRepository.save(Reservation.builder()
                .voyageur(voyageur).ligne(ligne).arretDepart(dep).arretArrivee(arr)
                .horaire(horaire).dateTrajet(dateTrajet).montant(montant)
                .typePaiement(type).statut(statut)
                .codeTexte(codeTexte).codeQR(codeQR)
                .dateReservation(LocalDateTime.now().minusDays(joursAvantReservation))
                .build());
    }
}