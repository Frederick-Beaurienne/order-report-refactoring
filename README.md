# Order Report Refactoring

Refactoring d’un code legacy Java avec garantie de non-régression via tests Golden Master.

## Stack Technique

- Java 21
- Spring Boot
- Maven
- JUnit 5

---

## Structure du Projet

```text
legacy/     -> code legacy fourni (inchangé)
src/        -> implémentation refactorée
tests/      -> tests de régression et tests unitaires
```

---

## Architecture

Le projet est organisé selon une séparation claire des responsabilités afin
de faciliter la maintenabilité, la testabilité et l’évolution du code.

```text
src/main/java/com/fred/orderreport
│
├── domain/
│   ├── model/          -> entités métier
│   ├── calculator/     -> logique de calcul métier
│   └── service/        -> services métier
│
├── infrastructure/
│   ├── csv/            -> parsing et lecture des CSV
│   ├── filesystem/     -> gestion des fichiers
│   └── formatter/      -> génération et formatage du rapport
│
├── orchestration/      -> coordination des traitements applicatifs
│
└── shared/constants/   -> constantes partagées
```