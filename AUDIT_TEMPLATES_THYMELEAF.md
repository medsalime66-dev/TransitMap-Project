# Rapport de refonte — Batch 1 : Suppression URBAIN + Restructuration INTERURBAIN
> Projet : TransitMap | Date : 2026-06-13

---

## Objectif
Supprimer entièrement le transport urbain (Ligne, Arret, Trajet) et restructurer le transport interurbain avec un système de prix par matrice entre arrêts (PrixSegment).

---

## Fichiers SUPPRIMÉS (24 fichiers Java + templates)

### Entités
- `entity/Ligne.java`
- `entity/Arret.java`
- `entity/Trajet.java`

### Repositories
- `repository/LigneRepository.java`
- `repository/ArretRepository.java`
- `repository/TrajetRepository.java`

### Services
- `service/LigneService.java` + `impl/LigneServiceImpl.java`
- `service/ArretService.java` + `impl/ArretServiceImpl.java`
- `service/TrajetService.java` + `impl/TrajetServiceImpl.java`
- `service/VehiculeService.java` + `impl/VehiculeServiceImpl.java`

### Controllers
- `controller/LigneController.java`
- `controller/ArretController.java`
- `controller/TrajetController.java`
- `controller/VehiculeController.java`
- `controller/MapApiController.java`

### DTOs
- `dto/LigneDto.java`
- `dto/ArretDto.java`
- `dto/TrajetDto.java`

### Templates
- Répertoires entiers : `lignes/`, `arrets/`, `vehicules/`, `trajets/`
- `voyageur/creer-reservation.html`
- `voyageur/ligne-trajets.html`
- `agent/arret-create.html`
- `agent/trajet-create.html`
- `agent/demandes.html`
- `agent/demande-create.html`

---

## Fichiers CRÉÉS (2)

### `entity/PrixSegment.java`
Entité de matrice de prix symétrique entre deux VilleEtape sur une LigneInterurbaine.
- Champs : `id`, `ligne` (ManyToOne), `arretA` (VilleEtape), `arretB` (VilleEtape), `prix`
- Un seul enregistrement couvre les deux sens A↔B

### `repository/PrixSegmentRepository.java`
- `findByLigneId(Long ligneId)`
- `findPrixEntre(ligneId, a, b)` — requête JPQL avec OR pour trouver le prix dans les deux sens

---

## Fichiers MODIFIÉS (≈28)

### Entités restructurées
| Fichier | Modification |
|---|---|
| `entity/LigneInterurbaine.java` | Supprimé `prixBase`, `distanceKm` ; ajouté `@OneToMany etapes` avec `@OrderBy("ordre ASC")` |
| `entity/VilleEtape.java` | Supprimé `dureeDepuisDebut` ; champs restants : id, ligne, nomVille, latitude, longitude, ordre |
| `entity/Reservation.java` | Remplacé `Trajet`/`Arret` par `LigneInterurbaine`/`VilleEtape` (arretDepart, arretArrivee) ; enum `TypePaiement` → BANKILY, MASRVI, SEDAD, CLICK, BAMIS, BIMBANK, BCIPAY |
| `entity/Vehicule.java` | Supprimé champ `ligne` (ManyToOne vers Ligne) |
| `entity/Chauffeur.java` | Supprimé champ `ligne` (ManyToOne vers Ligne) |
| `entity/Entreprise.java` | Enum `TypeService` réduit à `{INTERURBAIN}` uniquement |

### DTOs nettoyés
| Fichier | Modification |
|---|---|
| `dto/ReservationDto.java` | Remplacé trajetId/dateTrajet/heureDepart par ligneId, arretDepartId, arretArriveeId, ligneNom, arretDepartNom, arretArriveeNom |
| `dto/LigneInterurbaineDto.java` | Supprimé prixBase, distanceKm |
| `dto/VilleEtapeDto.java` | Supprimé dureeDepuisDebut |
| `dto/ChauffeurDto.java` | Supprimé ligneId, ligneNom |
| `dto/VehiculeDto.java` | Supprimé ligneId |
| `dto/DemandeInscriptionDto.java` | Supprimé import inutilisé `TypeService` |

### Repositories nettoyés
| Fichier | Modification |
|---|---|
| `repository/ReservationRepository.java` | Supprimé findByTrajet, findByTrajetAndStatut, countByTrajetAndStatutIn |
| `repository/VehiculeRepository.java` | Supprimé import Ligne et méthode findByLigne() |
| `repository/ChauffeurRepository.java` | Supprimé import Ligne et méthode findByLigne() |

### Services réécrits
| Fichier | Modification |
|---|---|
| `service/voyageur/ReservationServiceImpl.java` | Réécriture complète : utilise LigneInterurbaineRepository, VilleEtapeRepository ; mapper() mappe nomVille → arretDepartNom/arretArriveeNom |
| `service/voyageur/ReservationService.java` | Supprimé méthode reservationsEnAttenteParTrajet() |
| `service/agent/AgentChauffeurServiceImpl.java` | Supprimé LigneRepository ; assignerLigne/retirerLigne sont des no-ops |
| `service/agent/AgentVehiculeServiceImpl.java` | Supprimé LigneRepository ; création véhicule sans ligne |
| `service/impl/InterurbainServiceImpl.java` | Supprimé distanceKm/prixBase/dureeDepuisDebut des builders et mappers |

### Controllers nettoyés
| Fichier | Modification |
|---|---|
| `controller/admin/AdminSurveillanceController.java` | Supprimé repos Ligne/Arret/Vehicule/Trajet ; trajetsAujourdhui = List.of() |
| `controller/agent/AgentDashboardController.java` | Supprimé TrajetRepository/LigneRepository ; trajetsAujourdhui = List.of() |
| `controller/agent/AgentChauffeurController.java` | Supprimé LigneRepository et attribut lignes du modèle |
| `controller/agent/AgentVehiculeController.java` | Supprimé LigneRepository et attribut lignes du modèle |
| `controller/chauffeur/ChauffeurDashboardController.java` | Construit ChauffeurDto manuellement sans getLigne() |
| `controller/voyageur/VoyageurPaiementController.java` | Passe DTO via reservationService.trouverParId() ; supprimé fetches redondants de l'entité brute |

### Config
| Fichier | Modification |
|---|---|
| `config/DataInitializer.java` | Réécriture complète : 3 lignes interurbaines (NKT-NDB, NKT-Atar, NKT-Rosso), chacune avec 3 VilleEtape, 3 PrixSegment (matrice complète), 2 HoraireInterurbain |

### Templates corrigés
| Fichier | Modification |
|---|---|
| `admin/interurbain/create.html` | Supprimé champs distanceKm et prixBase |
| `admin/interurbain/detail.html` | Remplacé méta distanceKm/prixBase par description |
| `admin/interurbain/list.html` | Colonnes : Route, Description, Départs/jour, Actions (supprimé Distance et Prix de base) |
| `admin/interurbain/search.html` | Supprimé affichage prixBase et distanceKm |
| `agent/dashboard.html` | Colonne "Ligne assignée" → affiche `—` statique |
| `agent/chauffeurs/liste.html` | Colonne "Ligne assignée" → affiche `—` statique |
| `agent/vehicules/creer.html` | Supprimé champ ligneId du formulaire |
| `agent/chauffeurs/creer.html` | Supprimé section "Ligne assignée" avec champ ligneId |
| `chauffeur/dashboard.html` | Remplacé ligneNom par affichage "Actif" statique |
| `voyageur/paiement-methode.html` | Utilise champs DTO : reservation.ligneNom, reservation.arretDepartNom, reservation.arretArriveeNom |

---

## Erreurs de compilation restantes
**Aucune.** Le projet compile proprement.

Avertissements mineurs non bloquants :
- `DataInitializer.java` — variable locale `roleChauffeur` non utilisée
- `AgentVehiculeServiceImpl.java` — champ `userRepository` déclaré mais non utilisé
- `User.java` — annotation `@Builder.Default` manquante sur un champ initialisé (avertissement Lombok)

---

## Lacunes fonctionnelles à traiter en Batch 2 et 3

### Batch 2 — Templates Agent
Recréer les vues interurbaines côté agent :
- Gestion des horaires et étapes par ligne
- Liste des réservations en attente à valider

### Batch 3 — Carte
- Recréer `MapApiController` en contexte interurbain
- Recréer `map.html` avec affichage GPS des véhicules sur lignes interurbaines
