package com.transitmap.config;

import com.transitmap.entity.*;
import com.transitmap.entity.Entreprise.TypeService;
import com.transitmap.entity.DemandeInscription.StatutDemande;
import com.transitmap.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Initialisation des données de démonstration au démarrage.
 * Crée les rôles, utilisateurs, entreprises, lignes, arrêts,
 * véhicules, chauffeurs et trajets de base.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final LigneRepository ligneRepository;
    private final ArretRepository arretRepository;
    private final VehiculeRepository vehiculeRepository;
    private final TrajetRepository trajetRepository;
    private final ChauffeurRepository chauffeurRepository;
    private final EntrepriseRepository entrepriseRepository;
    private final DemandeInscriptionRepository demandeRepository;
    private final LigneInterurbaineRepository ligneInterurbaineRepository;
    private final VilleEtapeRepository villeEtapeRepository;
    private final HoraireInterurbaineRepository horaireRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // ===================== RÔLES =====================
        Role roleAdmin = creerRole("ADMIN");
        Role roleAgent = creerRole("AGENT");
        Role roleVoyageur = creerRole("VOYAGEUR");
        Role roleChauffeur = creerRole("CHAUFFEUR");

        // ===================== UTILISATEURS =====================
        User admin = creerUtilisateur(
                "admin", "admin123", roleAdmin);

        User agent1 = creerUtilisateur(
                "agent1@transitmap.mr", "agent123", roleAgent);

        User agent2 = creerUtilisateur(
                "agent2@transitmap.mr", "agent123", roleAgent);

        User voyageur1 = creerUtilisateur(
                "voyageur1@transitmap.mr", "voyageur123", roleVoyageur);

        User voyageur2 = creerUtilisateur(
                "voyageur2@transitmap.mr", "voyageur123", roleVoyageur);

        // ===================== ENTREPRISES =====================
        if (!entrepriseRepository.findByAgent(agent1).isPresent()) {

            entrepriseRepository.save(Entreprise.builder()
                    .nom("Transport Nord Mauritanie")
                    .numeroCommercial("TRN-2024-001")
                    .adresse("Nouakchott, Mauritanie")
                    .typeService(TypeService.LES_DEUX)
                    .codeBankily("013274")
                    .codeMasrvi("MSR-7821")
                    .codeSedad("SDD-4412")
                    .agent(agent1)
                    .build());
        }

        if (!entrepriseRepository.findByAgent(agent2).isPresent()) {

            entrepriseRepository.save(Entreprise.builder()
                    .nom("Société Sud Transport")
                    .numeroCommercial("SST-2024-002")
                    .adresse("Nouakchott, Mauritanie")
                    .typeService(TypeService.URBAIN)
                    .codeBankily("027431")
                    .codeClick("CLK-9934")
                    .agent(agent2)
                    .build());
        }

        // ===================== LIGNES =====================
        if (ligneRepository.count() > 0) return;

        Ligne ligneNord = ligneRepository.save(Ligne.builder()
                .numero("L01")
                .nom("Ligne Nord")
                .description("Centre-ville → Quartier Nord")
                .build());

        Ligne ligneSud = ligneRepository.save(Ligne.builder()
                .numero("L02")
                .nom("Ligne Sud")
                .description("Gare centrale → Zone Sud")
                .build());

        Ligne ligneCentre = ligneRepository.save(Ligne.builder()
                .numero("L03")
                .nom("Ligne Centre")
                .description("Marché central → Université")
                .build());

        Ligne ligneEst = ligneRepository.save(Ligne.builder()
                .numero("L04")
                .nom("Ligne Est")
                .description("Port → Zone industrielle Est")
                .build());

        // ===================== ARRÊTS =====================

        // Ligne Nord
        Arret arretGareCentrale = arretRepository.save(
                Arret.builder().nom("Gare Centrale")
                .latitude(18.0735).longitude(-15.9582)
                .ordre(1).ligne(ligneNord).build());

        Arret arretPlace = arretRepository.save(
                Arret.builder().nom("Place de l'Indépendance")
                .latitude(18.0790).longitude(-15.9650)
                .ordre(2).ligne(ligneNord).build());

        Arret arretMarche = arretRepository.save(
                Arret.builder().nom("Marché Capitale")
                .latitude(18.0850).longitude(-15.9700)
                .ordre(3).ligne(ligneNord).build());

        Arret arretQuartierNord = arretRepository.save(
                Arret.builder().nom("Quartier Nord")
                .latitude(18.0950).longitude(-15.9750)
                .ordre(4).ligne(ligneNord).build());

        // Ligne Sud
        arretRepository.save(Arret.builder().nom("Gare Centrale")
                .latitude(18.0735).longitude(-15.9582)
                .ordre(1).ligne(ligneSud).build());
        arretRepository.save(Arret.builder().nom("Avenue Gamal Nasser")
                .latitude(18.0680).longitude(-15.9600)
                .ordre(2).ligne(ligneSud).build());
        arretRepository.save(Arret.builder().nom("Carrefour Sud")
                .latitude(18.0620).longitude(-15.9550)
                .ordre(3).ligne(ligneSud).build());
        arretRepository.save(Arret.builder().nom("Zone Sud")
                .latitude(18.0550).longitude(-15.9500)
                .ordre(4).ligne(ligneSud).build());

        // Ligne Centre
        arretRepository.save(Arret.builder().nom("Marché Central")
                .latitude(18.0800).longitude(-15.9620)
                .ordre(1).ligne(ligneCentre).build());
        arretRepository.save(Arret.builder().nom("Hôpital National")
                .latitude(18.0820).longitude(-15.9560)
                .ordre(2).ligne(ligneCentre).build());
        arretRepository.save(Arret.builder().nom("Ministères")
                .latitude(18.0840).longitude(-15.9510)
                .ordre(3).ligne(ligneCentre).build());
        arretRepository.save(Arret.builder().nom("Université")
                .latitude(18.0870).longitude(-15.9460)
                .ordre(4).ligne(ligneCentre).build());

        // Ligne Est
        arretRepository.save(Arret.builder().nom("Port de Nouakchott")
                .latitude(18.0700).longitude(-16.0100)
                .ordre(1).ligne(ligneEst).build());
        arretRepository.save(Arret.builder().nom("Avenue de l'Indépendance")
                .latitude(18.0720).longitude(-15.9900)
                .ordre(2).ligne(ligneEst).build());
        arretRepository.save(Arret.builder().nom("Rond-point Est")
                .latitude(18.0740).longitude(-15.9700)
                .ordre(3).ligne(ligneEst).build());
        arretRepository.save(Arret.builder().nom("Zone Industrielle")
                .latitude(18.0760).longitude(-15.9400)
                .ordre(4).ligne(ligneEst).build());

        // ===================== CHAUFFEURS =====================

        User userChauffeur1 = creerUtilisateur(
                "chauffeur1@transitmap.mr", "chauffeur123", roleChauffeur);

        User userChauffeur2 = creerUtilisateur(
                "chauffeur2@transitmap.mr", "chauffeur123", roleChauffeur);

        Chauffeur chauffeur1 = chauffeurRepository.save(
                Chauffeur.builder()
                .nomComplet("Mohamed Ould Taleb")
                .numeroPermis("MR-2020-001")
                .telephone("22345678")
                .email("chauffeur1@transitmap.mr")
                .user(userChauffeur1)
                .agent(agent1)
                .ligne(ligneNord)
                .build());

        Chauffeur chauffeur2 = chauffeurRepository.save(
                Chauffeur.builder()
                .nomComplet("Ahmed Salem Ould Bah")
                .numeroPermis("MR-2021-002")
                .telephone("33456789")
                .email("chauffeur2@transitmap.mr")
                .user(userChauffeur2)
                .agent(agent1)
                .ligne(ligneSud)
                .build());

        // ===================== VÉHICULES =====================

        Vehicule vehicule1 = vehiculeRepository.save(
                Vehicule.builder()
                .matricule("NKT-001-A")
                .marque("Toyota Coaster")
                .capacite(30)
                .placesDisponibles(28)
                .annee(2020)
                .statut("EN_SERVICE")
                .latitudeActuelle(18.0790)
                .longitudeActuelle(-15.9650)
                .ligne(ligneNord)
                .chauffeur(chauffeur1)
                .build());

        Vehicule vehicule2 = vehiculeRepository.save(
                Vehicule.builder()
                .matricule("NKT-002-A")
                .marque("Mercedes Sprinter")
                .capacite(20)
                .placesDisponibles(15)
                .annee(2019)
                .statut("EN_SERVICE")
                .latitudeActuelle(18.0820)
                .longitudeActuelle(-15.9700)
                .ligne(ligneNord)
                .chauffeur(null)
                .build());

        Vehicule vehicule3 = vehiculeRepository.save(
                Vehicule.builder()
                .matricule("NKT-003-B")
                .marque("Toyota Hiace")
                .capacite(15)
                .placesDisponibles(10)
                .annee(2021)
                .statut("EN_SERVICE")
                .latitudeActuelle(18.0650)
                .longitudeActuelle(-15.9580)
                .ligne(ligneSud)
                .chauffeur(chauffeur2)
                .build());

        Vehicule vehicule4 = vehiculeRepository.save(
                Vehicule.builder()
                .matricule("NKT-004-B")
                .marque("Isuzu NQR")
                .capacite(35)
                .placesDisponibles(35)
                .annee(2018)
                .statut("EN_MAINTENANCE")
                .latitudeActuelle(null)
                .longitudeActuelle(null)
                .ligne(ligneSud)
                .chauffeur(null)
                .build());

        Vehicule vehicule5 = vehiculeRepository.save(
                Vehicule.builder()
                .matricule("NKT-005-C")
                .marque("Toyota Coaster")
                .capacite(30)
                .placesDisponibles(30)
                .annee(2022)
                .statut("EN_SERVICE")
                .latitudeActuelle(18.0830)
                .longitudeActuelle(-15.9530)
                .ligne(ligneCentre)
                .chauffeur(null)
                .build());

        // ===================== TRAJETS =====================

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        // Trajets aujourd'hui
        trajetRepository.save(Trajet.builder()
                .dateTrajet(today)
                .heureDepart(LocalTime.of(7, 0))
                .heureArrivee(LocalTime.of(8, 30))
                .statut("PLANIFIE")
                .ligne(ligneNord)
                .vehicule(vehicule1)
                .chauffeur(chauffeur1)
                .build());

        trajetRepository.save(Trajet.builder()
                .dateTrajet(today)
                .heureDepart(LocalTime.of(9, 0))
                .heureArrivee(LocalTime.of(10, 0))
                .statut("PLANIFIE")
                .ligne(ligneCentre)
                .vehicule(vehicule5)
                .chauffeur(null)
                .build());

        trajetRepository.save(Trajet.builder()
                .dateTrajet(today)
                .heureDepart(LocalTime.of(11, 0))
                .heureArrivee(LocalTime.of(12, 0))
                .statut("PLANIFIE")
                .ligne(ligneSud)
                .vehicule(vehicule3)
                .chauffeur(chauffeur2)
                .build());

        trajetRepository.save(Trajet.builder()
                .dateTrajet(today)
                .heureDepart(LocalTime.of(14, 0))
                .heureArrivee(LocalTime.of(15, 30))
                .statut("PLANIFIE")
                .ligne(ligneNord)
                .vehicule(vehicule2)
                .chauffeur(null)
                .build());

        // Trajets demain
        trajetRepository.save(Trajet.builder()
                .dateTrajet(tomorrow)
                .heureDepart(LocalTime.of(7, 0))
                .heureArrivee(LocalTime.of(8, 30))
                .statut("PLANIFIE")
                .ligne(ligneNord)
                .vehicule(vehicule1)
                .chauffeur(chauffeur1)
                .build());

        trajetRepository.save(Trajet.builder()
                .dateTrajet(tomorrow)
                .heureDepart(LocalTime.of(9, 0))
                .heureArrivee(LocalTime.of(10, 15))
                .statut("PLANIFIE")
                .ligne(ligneCentre)
                .vehicule(vehicule5)
                .chauffeur(null)
                .build());

        // ===================== LIGNES INTERURBAINES =====================

        LigneInterurbaine nktNdb = ligneInterurbaineRepository.save(
                LigneInterurbaine.builder()
                .nom("Nouakchott — Nouadhibou Express")
                .villeDepart("Nouakchott")
                .villeArrivee("Nouadhibou")
                .distanceKm(470.0)
                .prixBase(3500.0)
                .description("Ligne principale côtière")
                .build());

        LigneInterurbaine nktRosso = ligneInterurbaineRepository.save(
                LigneInterurbaine.builder()
                .nom("Nouakchott — Rosso")
                .villeDepart("Nouakchott")
                .villeArrivee("Rosso")
                .distanceKm(200.0)
                .prixBase(1500.0)
                .description("Liaison vers le fleuve Sénégal")
                .build());

        LigneInterurbaine nktAtar = ligneInterurbaineRepository.save(
                LigneInterurbaine.builder()
                .nom("Nouakchott — Atar")
                .villeDepart("Nouakchott")
                .villeArrivee("Atar")
                .distanceKm(450.0)
                .prixBase(3000.0)
                .description("Route de l'Adrar")
                .build());

        // Étapes NKT-NDB
        villeEtapeRepository.save(VilleEtape.builder()
                .ligne(nktNdb).nomVille("Nouakchott")
                .latitude(18.0735).longitude(-15.9582)
                .ordre(1).dureeDepuisDebut(0).build());
        villeEtapeRepository.save(VilleEtape.builder()
                .ligne(nktNdb).nomVille("Akjoujt")
                .latitude(19.7455).longitude(-14.3847)
                .ordre(2).dureeDepuisDebut(180).build());
        villeEtapeRepository.save(VilleEtape.builder()
                .ligne(nktNdb).nomVille("Nouadhibou")
                .latitude(20.9310).longitude(-17.0347)
                .ordre(3).dureeDepuisDebut(420).build());

        // Horaires NKT-NDB
        horaireRepository.save(HoraireInterurbain.builder()
                .ligne(nktNdb)
                .heureDepart(LocalTime.of(6, 0))
                .heureArrivee(LocalTime.of(13, 0))
                .jours("QUOTIDIEN").prix(3500.0).actif(true).build());
        horaireRepository.save(HoraireInterurbain.builder()
                .ligne(nktNdb)
                .heureDepart(LocalTime.of(20, 0))
                .heureArrivee(LocalTime.of(3, 0))
                .jours("QUOTIDIEN").prix(3500.0).actif(true).build());

        // Étapes NKT-Rosso
        villeEtapeRepository.save(VilleEtape.builder()
                .ligne(nktRosso).nomVille("Nouakchott")
                .latitude(18.0735).longitude(-15.9582)
                .ordre(1).dureeDepuisDebut(0).build());
        villeEtapeRepository.save(VilleEtape.builder()
                .ligne(nktRosso).nomVille("Boutilimit")
                .latitude(17.5500).longitude(-14.6833)
                .ordre(2).dureeDepuisDebut(90).build());
        villeEtapeRepository.save(VilleEtape.builder()
                .ligne(nktRosso).nomVille("Rosso")
                .latitude(16.5133).longitude(-15.8050)
                .ordre(3).dureeDepuisDebut(180).build());

        // Horaires NKT-Rosso
        horaireRepository.save(HoraireInterurbain.builder()
                .ligne(nktRosso)
                .heureDepart(LocalTime.of(7, 0))
                .heureArrivee(LocalTime.of(10, 0))
                .jours("QUOTIDIEN").prix(1500.0).actif(true).build());
        horaireRepository.save(HoraireInterurbain.builder()
                .ligne(nktRosso)
                .heureDepart(LocalTime.of(14, 0))
                .heureArrivee(LocalTime.of(17, 0))
                .jours("QUOTIDIEN").prix(1500.0).actif(true).build());

        // Étapes NKT-Atar
        villeEtapeRepository.save(VilleEtape.builder()
                .ligne(nktAtar).nomVille("Nouakchott")
                .latitude(18.0735).longitude(-15.9582)
                .ordre(1).dureeDepuisDebut(0).build());
        villeEtapeRepository.save(VilleEtape.builder()
                .ligne(nktAtar).nomVille("Akjoujt")
                .latitude(19.7455).longitude(-14.3847)
                .ordre(2).dureeDepuisDebut(150).build());
        villeEtapeRepository.save(VilleEtape.builder()
                .ligne(nktAtar).nomVille("Atar")
                .latitude(20.5170).longitude(-13.0490)
                .ordre(3).dureeDepuisDebut(390).build());

        // Horaires NKT-Atar
        horaireRepository.save(HoraireInterurbain.builder()
                .ligne(nktAtar)
                .heureDepart(LocalTime.of(5, 0))
                .heureArrivee(LocalTime.of(11, 30))
                .jours("LUNDI,MERCREDI,VENDREDI")
                .prix(3000.0).actif(true).build());

        // ===================== DEMANDES D'INSCRIPTION (exemples) =====================

        if (!demandeRepository.existsByEmail("demande1@transport.mr")) {
            demandeRepository.save(DemandeInscription.builder()
                    .nomComplet("Fatima Mint Ahmed")
                    .email("demande1@transport.mr")
                    .telephone("44556677")
                    .numeroCIN("MR-12345")
                    .nomEntreprise("Transport Oasis SARL")
                    .numeroCommercial("TO-2024-003")
                    .adresse("Atar, Mauritanie")
                    .typeService(TypeService.INTERURBAIN)
                    .description("Spécialisée dans les trajets Adrar")
                    .motDePasseInitial("pass123")
                    .codeBankily("098765")
                    .statut(StatutDemande.EN_ATTENTE)
                    .dateCreation(LocalDateTime.now().minusDays(2))
                    .build());
        }

        if (!demandeRepository.existsByEmail("demande2@transport.mr")) {
            demandeRepository.save(DemandeInscription.builder()
                    .nomComplet("Sidi Mohamed Ould Vall")
                    .email("demande2@transport.mr")
                    .telephone("55667788")
                    .nomEntreprise("Ligne Express Mauritanie")
                    .typeService(TypeService.URBAIN)
                    .description("Transport urbain Nouakchott")
                    .motDePasseInitial("pass456")
                    .codeMasrvi("MSR-4567")
                    .codeBankily("112233")
                    .statut(StatutDemande.EN_ATTENTE)
                    .dateCreation(LocalDateTime.now().minusHours(5))
                    .build());
        }
    }

    // ===================== MÉTHODES UTILITAIRES =====================

    /** Crée un rôle s'il n'existe pas encore */
    private Role creerRole(String nom) {
        return roleRepository.findByName(nom)
                .orElseGet(() -> roleRepository.save(
                        Role.builder().name(nom).build()));
    }

    /** Crée un utilisateur s'il n'existe pas encore */
    private User creerUtilisateur(String username,
                                   String motDePasse,
                                   Role role) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                        .username(username)
                        .password(passwordEncoder.encode(motDePasse))
                        .enabled(true)
                        .role(role)
                        .build()));
    }
}