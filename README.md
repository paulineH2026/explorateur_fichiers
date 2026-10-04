# Explorateur de fichiers - programmation logicielle

Un explorateur de fichiers en ligne de commande, écrit en Java. Il permet de parcourir les répertoires, gérer les fichiers et dossiers, effectuer des recherches par nom et associer des notes aux éléments.

## Fonctionnalités

- Afficher le contenu d’un répertoire et naviguer dans l’arborescence
- Créer, copier, déplacer et supprimer des fichiers ou des dossiers
- Rechercher un élément par nom, y compris dans les sous-répertoires
- Afficher le contenu des fichiers
- Ajouter et consulter des notes associées aux fichiers et dossiers

## Prérequis

- Java 17 ou version ultérieure
- Maven (facultatif, le projet inclut le Maven Wrapper)

## Compilation et lancement

Sous Windows :

```powershell
./mvnw.cmd clean package
java -jar target/builder-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Sous macOS ou Linux :

```sh
./mvnw clean package
java -jar target/builder-1.0-SNAPSHOT-jar-with-dependencies.jar
```

L’application démarre dans le répertoire personnel de l’utilisateur. Saisissez `help` pour afficher les commandes disponibles ou `exit` pour quitter.

## Commandes

La commande `ls` affiche le contenu du répertoire courant. Chaque élément reçoit un numéro (NER) qui permet de le sélectionner pour les commandes suivantes.

| Commande | Description |
| --- | --- |
| `ls` | Afficher le contenu du répertoire courant |
| `<NER> .` | Ouvrir un répertoire |
| `..` | Revenir au répertoire parent |
| `mkdir <nom>` | Créer un répertoire |
| `find <nom>` | Rechercher un fichier ou un répertoire par nom |
| `<NER> visu` | Afficher le contenu d’un fichier |
| `<NER> copy` / `<NER> cut` | Préparer un élément à copier ou déplacer |
| `past` | Coller l’élément préparé dans le répertoire courant |
| `<NER> del` | Supprimer un élément |
| `<NER> + <note>` | Ajouter une note à un élément |
| `<NER> -` | Supprimer une note d’un élément |
| `<NER>` / `notes` | Sélectionner un élément et consulter ses notes / consulter les notes de l’élément courant |
| `help` | Afficher la liste des commandes |
| `exit` | Quitter l’application |
