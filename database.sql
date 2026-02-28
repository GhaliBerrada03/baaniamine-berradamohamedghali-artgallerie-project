-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : ven. 20 fév. 2026 à 22:19
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `artgallerie`
--

-- --------------------------------------------------------

--
-- Structure de la table `client`
--

CREATE TABLE `client` (
  `idClient` int(11) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `client`
--

INSERT INTO `client` (`idClient`, `nom`, `email`) VALUES
(16, 'Maroanin', 'maroanin@gmail.com');

-- --------------------------------------------------------

--
-- Structure de la table `oeuvre`
--

CREATE TABLE `oeuvre` (
  `idOeuvre` int(11) NOT NULL,
  `titre` varchar(150) NOT NULL,
  `artiste` varchar(120) NOT NULL,
  `categorie` varchar(80) NOT NULL,
  `prix` decimal(10,2) NOT NULL,
  `statut` enum('DISPONIBLE','VENDUE') NOT NULL DEFAULT 'DISPONIBLE'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `oeuvre`
--

INSERT INTO `oeuvre` (`idOeuvre`, `titre`, `artiste`, `categorie`, `prix`, `statut`) VALUES
(2, 'La Joconde Updated', 'Picasso', 'Sculpture', 9000.00, 'VENDUE'),
(3, 'La Joconde', 'Picasso', 'Peinture', 5000.00, 'DISPONIBLE');

-- --------------------------------------------------------

--
-- Structure de la table `vente_art`
--

CREATE TABLE `vente_art` (
  `idVente` int(11) NOT NULL,
  `idClient` int(11) NOT NULL,
  `idOeuvre` int(11) NOT NULL,
  `date_vente` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `vente_art`
--

INSERT INTO `vente_art` (`idVente`, `idClient`, `idOeuvre`, `date_vente`) VALUES
(2, 16, 2, '2026-02-20 15:27:56');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `client`
--
ALTER TABLE `client`
  ADD PRIMARY KEY (`idClient`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Index pour la table `oeuvre`
--
ALTER TABLE `oeuvre`
  ADD PRIMARY KEY (`idOeuvre`);

--
-- Index pour la table `vente_art`
--
ALTER TABLE `vente_art`
  ADD PRIMARY KEY (`idVente`),
  ADD UNIQUE KEY `uq_vente_oeuvre` (`idOeuvre`),
  ADD KEY `fk_vente_client` (`idClient`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `client`
--
ALTER TABLE `client`
  MODIFY `idClient` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT pour la table `oeuvre`
--
ALTER TABLE `oeuvre`
  MODIFY `idOeuvre` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `vente_art`
--
ALTER TABLE `vente_art`
  MODIFY `idVente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `vente_art`
--
ALTER TABLE `vente_art`
  ADD CONSTRAINT `fk_vente_client` FOREIGN KEY (`idClient`) REFERENCES `client` (`idClient`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_vente_oeuvre` FOREIGN KEY (`idOeuvre`) REFERENCES `oeuvre` (`idOeuvre`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
