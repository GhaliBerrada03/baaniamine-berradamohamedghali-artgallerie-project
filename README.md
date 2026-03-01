# 🎨 Système de Gestion de Galerie d'Art

Un système de gestion de galerie d'art basé sur une application desktop développée en **Java** et **MySQL**. Le système gère les œuvres, les clients et les ventes avec une architecture en couches et des notifications email intégrées.

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Swing](https://img.shields.io/badge/UI-Java%20Swing-007396?style=for-the-badge&logo=java&logoColor=white)
![SMTP](https://img.shields.io/badge/Email-Jakarta%20Mail-D14836?style=for-the-badge&logo=gmail&logoColor=white)

---

## 📌 1. Aperçu du Projet

Cette application permet :

- Aux clients de consulter et d'acheter des œuvres d'art
- L'enregistrement automatique des ventes
- L'envoi d'un email de confirmation après chaque achat
- La réinitialisation du mot de passe par email
- La gestion des clients et des œuvres par l'administrateur
- Le suivi des statistiques de ventes et des revenus

Le système suit une architecture en couches **(DAO → Service → UI)** et utilise le **hachage SHA-256** pour la sécurité des mots de passe.

---

## 🧩 2. Entités & Modèle de Domaine
<img width="1238" height="793" alt="image" src="https://github.com/user-attachments/assets/64589884-0dfc-4c5d-987a-ca53fbc9cd9c" />


Ce diagramme représente les entités métier principales et leurs relations.

### Entités Principales

**Client**
- `idClient`
- `nom`
- `email`
- `password` *(haché en SHA-256)*

**Oeuvre**
- `idOeuvre`
- `titre`
- `artiste`
- `categorie`
- `prix`
- `statut` *(DISPONIBLE / VENDUE)*

**VenteArt**
- `idVente`
- `dateVente`
- Relation entre `Client` et `Oeuvre`

L'entité `VenteArt` modélise l'action métier "acheter", car la relation contient des données supplémentaires (la date).

---

## 🎭 3. Diagramme des Cas d'Utilisation
<img width="2048" height="2024" alt="image" src="https://github.com/user-attachments/assets/13982826-afff-4749-8033-73116a5671b7" />

Ce diagramme montre comment les utilisateurs interagissent avec le système.

### Acteurs

**Client**
- Connexion
- Réinitialisation du mot de passe
- Consulter les œuvres
- Filtrer les œuvres
- Acheter une œuvre

**Admin**
- Gérer les œuvres
- Gérer les clients

Le système sépare clairement les opérations client de la gestion administrative.

---

## 🗄️ 4. Schéma de la Base de Données (MySQL)
<img width="1135" height="486" alt="image" src="https://github.com/user-attachments/assets/5fed3770-bd9d-4048-a777-7c7f4449ba74" />


Le schéma relationnel est conçu pour refléter le modèle de domaine avec des clés primaires et étrangères appropriées.

### Tables

- `client`
- `oeuvre`
- `vente_art`

**Points clés :**
- L'email est unique
- Le mot de passe est stocké sous forme de hash SHA-256 (`CHAR 64`)
- Les clés étrangères garantissent l'intégrité référentielle
- Le statut de l'œuvre se met à jour automatiquement après la vente

---

## 🏗️ 5. Architecture du Système
<img width="3130" height="1235" alt="architechture" src="https://github.com/user-attachments/assets/0051b541-0bf4-49ea-b200-c6e0f803dd40" />


L'application suit une structure en couches claire.

### Couches

**1. Couche UI (Swing)**
- Connexion
- Consultation et achat d'œuvres
- Historique des ventes
- Tableau de bord des statistiques

**2. Couche Service**
- Logique métier
- Notifications email
- Traitement des ventes
- Logique de réinitialisation du mot de passe

**3. Couche DAO**
- Communication avec la base de données
- Requêtes SQL
- Opérations CRUD

**4. Couche Base de Données**
- Persistance MySQL

Cette séparation améliore la maintenabilité et la scalabilité du système.

---

## 🔄 6. Flux de Sécurité
<img width="952" height="541" alt="image" src="https://github.com/user-attachments/assets/c451d6c0-5e41-4080-993e-355ba1821025" />


Ce diagramme illustre le mécanisme automatique de réinitialisation du mot de passe déclenché lors d'un échec de connexion.

À chaque tentative de connexion échouée, le système effectue automatiquement :

1. Détection de la correspondance email mais non-concordance du hash
2. Génération d'un mot de passe temporaire aléatoire
3. Hachage en SHA-256 et sauvegarde en base de données
4. Envoi du nouveau mot de passe à l'adresse email enregistrée du client

Ce mécanisme prévient les attaques par force brute — chaque tentative incorrecte invalide le mot de passe précédent.

---

## 🔐 7. Points Techniques Forts
<img width="1093" height="602" alt="image" src="https://github.com/user-attachments/assets/b3886ccc-f729-4f32-86df-80aded478eba" />

**1. Robuste**
- Architecture en couches
- Persistance MySQL
- Séparation claire des responsabilités

**2. Sécurisé**
- Hachage des mots de passe en SHA-256
- Gestion de session
- Mécanisme automatique de réinitialisation du mot de passe

**3. Réactif**
- Interface Swing fluide
- Threads d'arrière-plan pour l'envoi des emails

**4. Connecté**
- Intégration SMTP complète
- Confirmation d'achat automatique par email
- Emails de réinitialisation du mot de passe

---

## 📊 8. Statistiques & Rapports

Le système fournit des statistiques en temps réel incluant :

- Nombre total de ventes
- Revenu total
- Top artistes par nombre de ventes
- Top clients par nombre d'achats
- Filtrage des ventes par plage de dates

Ces données offrent des informations utiles pour la gestion de la galerie.

---

## 🧠 9. Conclusion

Le Système de Gestion de Galerie d'Art démontre une architecture desktop complète :

- Modélisation métier
- Conception de la base de données
- Logique backend
- Authentification sécurisée
- Intégration email
- Interface graphique interactive

Il représente un flux de gestion complet, depuis la connexion du client jusqu'à la confirmation de vente et l'analyse statistique.
