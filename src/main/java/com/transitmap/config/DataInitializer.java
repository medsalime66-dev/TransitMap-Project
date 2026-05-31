package com.transitmap.config;

import com.transitmap.entity.*;
import com.transitmap.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final LigneRepository ligneRepository;
    private final ArretRepository arretRepository;
    private final VehiculeRepository vehiculeRepository;
    private final TrajetRepository trajetRepository;
    private final PasswordEncoder passwordEncoder;
    private final LigneInterurbaineRepository ligneInterurbaineRepository;
    private final VilleEtapeRepository villeEtapeRepository;
    private final HoraireInterurbaineRepository horaireInterurbaineRepository;
    @Override
    public void run(String... args) {

        // ===================== ROLES =====================
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> roleRepository.save(
                        Role.builder().name("ADMIN").build()));

        Role agentRole = roleRepository.findByName("AGENT")
                .orElseGet(() -> roleRepository.save(
                        Role.builder().name("AGENT").build()));

        Role voyageurRole = roleRepository.findByName("VOYAGEUR")
                .orElseGet(() -> roleRepository.save(
                        Role.builder().name("VOYAGEUR").build()));

        // ===================== USERS =====================
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .enabled(true)
                    .role(adminRole)
                    .build());
        }

        if (!userRepository.existsByUsername("agent1")) {
            userRepository.save(User.builder()
                    .username("agent1")
                    .password(passwordEncoder.encode("agent123"))
                    .enabled(true)
                    .role(agentRole)
                    .build());
        }

        if (!userRepository.existsByUsername("agent2")) {
            userRepository.save(User.builder()
                    .username("agent2")
                    .password(passwordEncoder.encode("agent123"))
                    .enabled(true)
                    .role(agentRole)
                    .build());
        }

        if (!userRepository.existsByUsername("voyageur1")) {
            userRepository.save(User.builder()
                    .username("voyageur1")
                    .password(passwordEncoder.encode("voyageur123"))
                    .enabled(true)
                    .role(voyageurRole)
                    .build());
        }

        if (!userRepository.existsByUsername("voyageur2")) {
            userRepository.save(User.builder()
                    .username("voyageur2")
                    .password(passwordEncoder.encode("voyageur123"))
                    .enabled(true)
                    .role(voyageurRole)
                    .build());
        }

        // ===================== LIGNES =====================
        if (ligneRepository.count() > 0) return; // تجنب التكرار
// ========= LIGNES INTERURBAINES =========

        LigneInterurbaine nktNdb = ligneInterurbaineRepository.save(
            LigneInterurbaine.builder()
                .nom("Nouakchott — Nouadhibou")
                .villeDepart("Nouakchott").villeArrivee("Nouadhibou")
                .distanceKm(470.0).prixBase(3500.0)
                .description("Ligne principale côtière")
                .build());

        LigneInterurbaine nktRosso = ligneInterurbaineRepository.save(
            LigneInterurbaine.builder()
                .nom("Nouakchott — Rosso")
                .villeDepart("Nouakchott").villeArrivee("Rosso")
                .distanceKm(200.0).prixBase(1500.0)
                .description("Liaison vers le fleuve Sénégal")
                .build());

        LigneInterurbaine nktAtar = ligneInterurbaineRepository.save(
            LigneInterurbaine.builder()
                .nom("Nouakchott — Atar")
                .villeDepart("Nouakchott").villeArrivee("Atar")
                .distanceKm(450.0).prixBase(3000.0)
                .description("Route de l'Adrar")
                .build());

        // Etapes NKT-NDB
        villeEtapeRepository.save(VilleEtape.builder().ligne(nktNdb)
            .nomVille("Nouakchott").latitude(18.0735).longitude(-15.9582)
            .ordre(1).dureeDepuisDebut(0).build());
        villeEtapeRepository.save(VilleEtape.builder().ligne(nktNdb)
            .nomVille("Akjoujt").latitude(19.7455).longitude(-14.3847)
            .ordre(2).dureeDepuisDebut(180).build());
        villeEtapeRepository.save(VilleEtape.builder().ligne(nktNdb)
            .nomVille("Nouadhibou").latitude(20.9310).longitude(-17.0347)
            .ordre(3).dureeDepuisDebut(420).build());

        // Horaires NKT-NDB
        horaireInterurbaineRepository.save(HoraireInterurbain.builder().ligne(nktNdb)
            .heureDepart(LocalTime.of(6,0)).heureArrivee(LocalTime.of(13,0))
            .jours("QUOTIDIEN").prix(3500.0).actif(true).build());
        horaireInterurbaineRepository.save(HoraireInterurbain.builder().ligne(nktNdb)
            .heureDepart(LocalTime.of(20,0)).heureArrivee(LocalTime.of(3,0))
            .jours("QUOTIDIEN").prix(3500.0).actif(true).build());

        // Etapes NKT-Rosso
        villeEtapeRepository.save(VilleEtape.builder().ligne(nktRosso)
            .nomVille("Nouakchott").latitude(18.0735).longitude(-15.9582)
            .ordre(1).dureeDepuisDebut(0).build());
        villeEtapeRepository.save(VilleEtape.builder().ligne(nktRosso)
            .nomVille("Boutilimit").latitude(17.5500).longitude(-14.6833)
            .ordre(2).dureeDepuisDebut(90).build());
        villeEtapeRepository.save(VilleEtape.builder().ligne(nktRosso)
            .nomVille("Rosso").latitude(16.5133).longitude(-15.8050)
            .ordre(3).dureeDepuisDebut(180).build());

        horaireInterurbaineRepository.save(HoraireInterurbain.builder().ligne(nktRosso)
            .heureDepart(LocalTime.of(7,0)).heureArrivee(LocalTime.of(10,0))
            .jours("QUOTIDIEN").prix(1500.0).actif(true).build());
        horaireInterurbaineRepository.save(HoraireInterurbain.builder().ligne(nktRosso)
            .heureDepart(LocalTime.of(14,0)).heureArrivee(LocalTime.of(17,0))
            .jours("QUOTIDIEN").prix(1500.0).actif(true).build());

        // Etapes NKT-Atar
        villeEtapeRepository.save(VilleEtape.builder().ligne(nktAtar)
            .nomVille("Nouakchott").latitude(18.0735).longitude(-15.9582)
            .ordre(1).dureeDepuisDebut(0).build());
        villeEtapeRepository.save(VilleEtape.builder().ligne(nktAtar)
            .nomVille("Akjoujt").latitude(19.7455).longitude(-14.3847)
            .ordre(2).dureeDepuisDebut(150).build());
        villeEtapeRepository.save(VilleEtape.builder().ligne(nktAtar)
            .nomVille("Atar").latitude(20.5170).longitude(-13.0490)
            .ordre(3).dureeDepuisDebut(390).build());

        horaireInterurbaineRepository.save(HoraireInterurbain.builder().ligne(nktAtar)
    .heureDepart(LocalTime.of(5,0)).heureArrivee(LocalTime.of(11,30))
    .jours("LUNDI,MERCREDI,VENDREDI").prix(3000.0).actif(true).build());
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

        // ===================== ARRETS =====================
        // Ligne Nord — Nouakchott approximatif
        arretRepository.save(Arret.builder().nom("Gare Centrale")
                .latitude(18.0735).longitude(-15.9582).ordre(1).ligne(ligneNord).build());
        arretRepository.save(Arret.builder().nom("Place de l'Indépendance")
                .latitude(18.0790).longitude(-15.9650).ordre(2).ligne(ligneNord).build());
        arretRepository.save(Arret.builder().nom("Marché Capitale")
                .latitude(18.0850).longitude(-15.9700).ordre(3).ligne(ligneNord).build());
        arretRepository.save(Arret.builder().nom("Quartier Nord")
                .latitude(18.0950).longitude(-15.9750).ordre(4).ligne(ligneNord).build());

        // Ligne Sud
        arretRepository.save(Arret.builder().nom("Gare Centrale")
                .latitude(18.0735).longitude(-15.9582).ordre(1).ligne(ligneSud).build());
        arretRepository.save(Arret.builder().nom("Avenue Gamal Nasser")
                .latitude(18.0680).longitude(-15.9600).ordre(2).ligne(ligneSud).build());
        arretRepository.save(Arret.builder().nom("Carrefour Sud")
                .latitude(18.0620).longitude(-15.9550).ordre(3).ligne(ligneSud).build());
        arretRepository.save(Arret.builder().nom("Zone Sud")
                .latitude(18.0550).longitude(-15.9500).ordre(4).ligne(ligneSud).build());

        // Ligne Centre
        arretRepository.save(Arret.builder().nom("Marché Central")
                .latitude(18.0800).longitude(-15.9620).ordre(1).ligne(ligneCentre).build());
        arretRepository.save(Arret.builder().nom("Hôpital National")
                .latitude(18.0820).longitude(-15.9560).ordre(2).ligne(ligneCentre).build());
        arretRepository.save(Arret.builder().nom("Ministères")
                .latitude(18.0840).longitude(-15.9510).ordre(3).ligne(ligneCentre).build());
        arretRepository.save(Arret.builder().nom("Université")
                .latitude(18.0870).longitude(-15.9460).ordre(4).ligne(ligneCentre).build());

        // Ligne Est
        arretRepository.save(Arret.builder().nom("Port de Nouakchott")
                .latitude(18.0700).longitude(-16.0100).ordre(1).ligne(ligneEst).build());
        arretRepository.save(Arret.builder().nom("Avenue de l'Indépendance")
                .latitude(18.0720).longitude(-15.9900).ordre(2).ligne(ligneEst).build());
        arretRepository.save(Arret.builder().nom("Rond-point Est")
                .latitude(18.0740).longitude(-15.9700).ordre(3).ligne(ligneEst).build());
        arretRepository.save(Arret.builder().nom("Zone Industrielle")
                .latitude(18.0760).longitude(-15.9400).ordre(4).ligne(ligneEst).build());

        // ===================== VEHICULES =====================
        vehiculeRepository.save(Vehicule.builder()
                .matricule("NKT-001-A")
                .capacite(50).statut("EN_SERVICE")
                .currentLatitude(18.0790).currentLongitude(-15.9650)
                .ligne(ligneNord).build());

        vehiculeRepository.save(Vehicule.builder()
                .matricule("NKT-002-A")
                .capacite(45).statut("EN_SERVICE")
                .currentLatitude(18.0820).currentLongitude(-15.9700)
                .ligne(ligneNord).build());

        vehiculeRepository.save(Vehicule.builder()
                .matricule("NKT-003-B")
                .capacite(60).statut("EN_SERVICE")
                .currentLatitude(18.0650).currentLongitude(-15.9580)
                .ligne(ligneSud).build());

        vehiculeRepository.save(Vehicule.builder()
                .matricule("NKT-004-B")
                .capacite(50).statut("EN_MAINTENANCE")
                .currentLatitude(null).currentLongitude(null)
                .ligne(ligneSud).build());

        vehiculeRepository.save(Vehicule.builder()
                .matricule("NKT-005-C")
                .capacite(55).statut("EN_SERVICE")
                .currentLatitude(18.0830).currentLongitude(-15.9530)
                .ligne(ligneCentre).build());

        vehiculeRepository.save(Vehicule.builder()
                .matricule("NKT-006-D")
                .capacite(40).statut("EN_SERVICE")
                .currentLatitude(18.0730).currentLongitude(-15.9800)
                .ligne(ligneEst).build());

        // ===================== TRAJETS =====================
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate tomorrow = today.plusDays(1);

        // Aujourd'hui
        trajetRepository.save(Trajet.builder()
                .dateTrajet(today).heureDepart(LocalTime.of(6, 0))
                .heureArrivee(LocalTime.of(7, 30))
                .statut("TERMINE").ligne(ligneNord).build());

        trajetRepository.save(Trajet.builder()
                .dateTrajet(today).heureDepart(LocalTime.of(8, 0))
                .heureArrivee(LocalTime.of(9, 30))
                .statut("TERMINE").ligne(ligneNord).build());

        trajetRepository.save(Trajet.builder()
                .dateTrajet(today).heureDepart(LocalTime.of(10, 0))
                .heureArrivee(LocalTime.of(11, 30))
                .statut("EN_COURS").ligne(ligneNord).build());

        trajetRepository.save(Trajet.builder()
                .dateTrajet(today).heureDepart(LocalTime.of(14, 0))
                .heureArrivee(LocalTime.of(15, 30))
                .statut("PLANIFIE").ligne(ligneNord).build());

        trajetRepository.save(Trajet.builder()
                .dateTrajet(today).heureDepart(LocalTime.of(7, 0))
                .heureArrivee(LocalTime.of(8, 0))
                .statut("TERMINE").ligne(ligneSud).build());

        trajetRepository.save(Trajet.builder()
                .dateTrajet(today).heureDepart(LocalTime.of(11, 0))
                .heureArrivee(LocalTime.of(12, 0))
                .statut("EN_COURS").ligne(ligneSud).build());

        trajetRepository.save(Trajet.builder()
                .dateTrajet(today).heureDepart(LocalTime.of(9, 0))
                .heureArrivee(LocalTime.of(10, 15))
                .statut("TERMINE").ligne(ligneCentre).build());

        trajetRepository.save(Trajet.builder()
                .dateTrajet(today).heureDepart(LocalTime.of(13, 0))
                .heureArrivee(LocalTime.of(14, 15))
                .statut("PLANIFIE").ligne(ligneCentre).build());

        trajetRepository.save(Trajet.builder()
                .dateTrajet(today).heureDepart(LocalTime.of(8, 30))
                .heureArrivee(LocalTime.of(9, 45))
                .statut("TERMINE").ligne(ligneEst).build());

        // Hier
        trajetRepository.save(Trajet.builder()
                .dateTrajet(yesterday).heureDepart(LocalTime.of(8, 0))
                .heureArrivee(LocalTime.of(9, 30))
                .statut("TERMINE").ligne(ligneNord).build());

        trajetRepository.save(Trajet.builder()
                .dateTrajet(yesterday).heureDepart(LocalTime.of(14, 0))
                .heureArrivee(LocalTime.of(15, 0))
                .statut("TERMINE").ligne(ligneSud).build());

        // Demain
        trajetRepository.save(Trajet.builder()
                .dateTrajet(tomorrow).heureDepart(LocalTime.of(7, 0))
                .heureArrivee(LocalTime.of(8, 30))
                .statut("PLANIFIE").ligne(ligneNord).build());

        trajetRepository.save(Trajet.builder()
                .dateTrajet(tomorrow).heureDepart(LocalTime.of(9, 0))
                .heureArrivee(LocalTime.of(10, 0))
                .statut("PLANIFIE").ligne(ligneCentre).build());
    }
}