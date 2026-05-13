# Order Report Refactoring

Refactoring d’un code legacy Java avec garantie de non-régression via tests Golden Master.

---

# Sommaire

* [Contexte](#contexte)
* [Objectifs du Refactoring](#objectifs-du-refactoring)
* [Stack Technique](#stack-technique)
* [Installation](#installation)
* [Exécution](#exécution)
* [Exécution des Tests](#exécution-des-tests)
* [Architecture du Projet](#architecture-du-projet)
* [Choix de Refactoring](#choix-de-refactoring)
* [Problèmes Identifiés dans le Legacy](#problèmes-identifiés-dans-le-legacy)
* [Solutions Apportées](#solutions-apportées)
* [Approche de Refactoring](#approche-de-refactoring)
* [Gestion des Magic Numbers](#gestion-des-magic-numbers)
* [Isolation des Effets de Bord](#isolation-des-effets-de-bord)
* [Compromis et Limites](#compromis-et-limites)
* [Améliorations Futures](#améliorations-futures)

---

# Contexte

Ce projet consiste à refactorer une application legacy Java générant des rapports clients à partir de plusieurs fichiers CSV.

L’objectif principal était de :

* améliorer la lisibilité,
* améliorer la maintenabilité,
* isoler les responsabilités,
* renforcer le typage,
* conserver strictement le comportement existant.

Le comportement legacy a été préservé via une stratégie de tests Golden Master.

---

# Objectifs du Refactoring

Le refactoring a été réalisé en conservant les contraintes suivantes :

* conservation stricte du comportement observable,
* absence de modification des données sources,
* conservation des comportements legacy existants, y compris certains comportements considérés comme discutables ou potentiellement buggués,
* amélioration progressive du design sans réécriture complète de l’application.

---

# Stack Technique

* Java 21
* Spring Boot
* Maven
* JUnit 5

---

# Installation

## Prérequis

* Java 21
* Maven 3.9+

## Installation des dépendances

```bash
mvn clean install
```

---

# Exécution

## Lancer l’application refactorée

```bash
mvn spring-boot:run
```

---

# Exécution des Tests

## Lancer tous les tests

```bash
mvn test
```

## Validation Golden Master

Le test Golden Master compare :

* la sortie du code legacy,
* la sortie du code refactoré,

afin de garantir une stricte non-régression fonctionnelle.

---

# Architecture du Projet

Le projet a été réorganisé autour d’une séparation claire des responsabilités.

```text
src/main/java/com/fred/orderreport
│
├── domain/
│   ├── model/              -> entités métier
│   ├── result/             -> objets consolidés de résultat
│   └── service/
│       └── calculator/     -> logique métier et calculateurs
│
├── infrastructure/
│   ├── csv/                -> parsing des fichiers CSV
│   ├── export/             -> export JSON
│   └── formatter/          -> formatage du rapport texte
│
├── orchestration/          -> orchestration applicative
│
└── shared/
    └── constants/          -> constantes métier partagées
```

---

# Choix de Refactoring

## Refactoring incrémental

Le projet a été refactoré progressivement afin de :

* limiter les risques de régression,
* conserver un historique Git lisible,
* garantir un état fonctionnel stable après chaque étape.

Chaque extraction importante a été validée par le test Golden Master.

---

## Choix de limiter la reformulation du code

Le refactoring s’est volontairement concentré sur :

* l’organisation,
* la séparation des responsabilités,
* l’extraction de logique métier,
* la lisibilité,
* le typage.

Le comportement legacy et certaines structures de calcul ont été volontairement conservés afin de respecter l’objectif de stricte refactorisation demandé par l’énoncé.

---

## Conservation des comportements legacy

Certains comportements potentiellement discutables ont été conservés volontairement :

* écrasement de certaines remises par des paliers supérieurs,
* try/catch silencieux sur certaines lectures CSV,
* ignorances des lignes invalides,
* conservation des comportements de fallback,
* règles métier implicites existantes.

Ces comportements ont été maintenus afin de préserver une correspondance stricte avec l’application originale.

---

# Problèmes Identifiés dans le Legacy

## Méthode monolithique

Le code legacy reposait sur une unique méthode centralisant :

* parsing,
* calculs métier,
* formatage,
* export,
* I/O.

Impact :

* très faible lisibilité,
* forte complexité cognitive,
* faible testabilité.

---

## Absence de typage métier

Le legacy utilisait principalement :

* `Map<String, Object>`
* `Map<String, String>`

Impact :

* absence de sécurité de typage,
* logique difficile à suivre,
* maintenance complexe.

---

## Mélange des responsabilités

Les responsabilités étaient fortement couplées :

* calcul métier,
* parsing,
* formatage,
* export JSON,
* écriture fichier.

Impact :

* difficulté d’évolution,
* risque élevé d’effets de bord.

---

## Présence de magic numbers

Les règles métier étaient représentées par des valeurs numériques dispersées dans plusieurs méthodes.

Impact :

* compréhension difficile,
* duplication des règles métier,
* maintenance plus complexe.

---

# Solutions Apportées

## Extraction des calculateurs métier

Les principales règles métier ont été extraites dans des calculateurs dédiés :

* DiscountCalculator
* TaxCalculator
* ShippingCalculator
* LoyaltyCalculator
* HandlingCalculator
* PromotionCalculator
* OrderPricingCalculator

---

## Introduction de modèles typés

Des objets métier dédiés ont été introduits afin de remplacer les structures dynamiques :

* Customer
* Product
* Order
* Promotion
* ShippingZone
* CustomerReportData
* DiscountResult

---

## Séparation infrastructure / métier

Les responsabilités techniques ont été déplacées dans des modules dédiés :

* parsing CSV,
* export JSON,
* formatage,
* orchestration.

---

## Isolation des effets de bord

Les opérations d’I/O ont été isolées :

* export JSON séparé,
* formatage séparé,
* construction des données métier avant export.

---

## Centralisation des constantes métier

Les principales règles métier ont été regroupées dans :

```text
BusinessConstants
```

Objectifs :

* suppression des principaux magic numbers,
* amélioration de la lisibilité,
* préparation vers un futur système de configuration plus avancé.

---

# Approche de Refactoring

Le refactoring a été réalisé selon une approche proche du pattern :

```text
Golden Master + petits refactorings incrémentaux
```

Chaque étape suivait le cycle :

1. extraction limitée,
2. exécution des tests,
3. validation Golden Master,
4. commit atomique.

Cette approche a permis de limiter fortement les risques de régression.

---

# Gestion des Magic Numbers

Les principales constantes métier ont été nommées et centralisées :

* règles de remises,
* seuils de livraison,
* taxation,
* bonus matin,
* fidélité,
* frais de gestion.

Le choix a été fait de ne pas sur-abstraire certaines valeurs immédiatement lisibles afin de préserver la lisibilité globale du code.

---

# Isolation des Effets de Bord

La génération du rapport repose désormais sur plusieurs étapes distinctes :

```text
Chargement des données
    ↓
Agrégation métier
    ↓
Construction des données consolidées
    ↓
Formatage
    ↓
Export
```

Cette séparation réduit fortement les effets de bord et améliore la testabilité.

---

# Compromis et Limites

## Ce qui n’a pas été réalisé

* externalisation complète des règles métier dans une configuration runtime,
* remplacement des dernières structures `Map<String, Object>` d’agrégation,
* couverture exhaustive en tests unitaires,
* refonte complète des comportements legacy incohérents.

---

## Compromis assumés

### Conservation stricte du comportement legacy

Certains comportements ont été volontairement conservés afin de garantir :

* la non-régression,
* la conformité avec le Golden Master,
* le respect du périmètre de refactorisation.

### Limitation de certaines abstractions

Certaines valeurs numériques simples ont été conservées localement afin d’éviter :

* une sur-ingénierie,
* une perte de lisibilité métier.

---

# Améliorations Futures

* remplacement complet des `Map<String, Object>` résiduels,
* externalisation des règles métier dans un système de configuration,
* ajout de tests unitaires plus complets,
* ajout d’une couche DTO dédiée,
* amélioration de la gestion d’erreurs,
* suppression progressive des comportements legacy implicites,
* ajout d’interfaces métier pour certains services,
* amélioration de la couverture de tests d’intégration.
