package fr.uvsq.pglp.project;

import fr.uvsq.pglp.project.commandes.CommandeAddNote;
import fr.uvsq.pglp.project.commandes.CommandeCd;
import fr.uvsq.pglp.project.commandes.CommandeCopyCut;
import fr.uvsq.pglp.project.commandes.CommandeDel;
import fr.uvsq.pglp.project.commandes.CommandeDeleteNote;
import fr.uvsq.pglp.project.commandes.CommandeFind;
import fr.uvsq.pglp.project.commandes.CommandeGoBack;
import fr.uvsq.pglp.project.commandes.CommandeHelp;
import fr.uvsq.pglp.project.commandes.CommandeListDirectory;
import fr.uvsq.pglp.project.commandes.CommandeMkdir;
import fr.uvsq.pglp.project.commandes.CommandeNotes;
import fr.uvsq.pglp.project.commandes.CommandeNull;
import fr.uvsq.pglp.project.commandes.CommandePaste;
import fr.uvsq.pglp.project.commandes.CommandeVisu;
import fr.uvsq.pglp.project.display.Color;
import fr.uvsq.pglp.project.display.TerminalDisplay;

/**
 * Cette classe permet de gérer les différentes commandes que peut entrer l'utilisateur.
 */
public class AppelCommande {

  private final CommandeContext commandeContext;
  private final TerminalDisplay display = new TerminalDisplay();

  // Constructeur qui initialise le contexte de la commande avec un contexte fourni
  public AppelCommande(CommandeContext commandeContext) {
    this.commandeContext = commandeContext;
  }

  /**
   * Cette méthode exécute la bonne commande en fonction de l'entrée de l'utilisateur.
   *
   * @param command La commande entrée par l'utilisateur.
   * @param er L'objet ER représentant un contexte ou état requis pour certaines commandes.
   * @param args Les arguments supplémentaires nécessaires pour certaines commandes.
   */
  public void executeCommand(String command, Er er, String... args) {
    // Vérifie si l'ER est non défini pour gérer les commandes nécessitant un contexte précis.
    if (er == null) {
      display.printError("Pas d'index stocké. Veuillez entrer un NER.");
      return;
    }

    OperationType type;
    switch (command) {
      case ".":
        // Change le répertoire actuel à celui spécifié dans ER
        CommandeCd commandeCd = new CommandeCd(commandeContext, er);
        commandeCd.execute();
        break;
      case "cut":
        // Coupe le fichier/dossier spécifié dans ER
        type = OperationType.CUT;
        CommandeCopyCut commandeCut = new CommandeCopyCut(commandeContext, er, type);
        commandeCut.execute();
        display.printMessage("Le fichier a été coupé", Color.DEFAULT);
        break;
      case "copy":
        // Copie le fichier/dossier spécifié dans ER
        type = OperationType.COPY;
        CommandeCopyCut commandeCopy = new CommandeCopyCut(commandeContext, er, type);
        commandeCopy.execute();
        display.printMessage("Le fichier a été copié", Color.DEFAULT);
        break;
      case "del":
        // Supprime le fichier/dossier spécifié dans ER
        CommandeDel commandeDel = new CommandeDel(commandeContext, er);
        commandeDel.execute();
        break;
      case "visu":
        // Visualise le contenu ou l'état de ER
        CommandeVisu commandeVisu = new CommandeVisu(er);
        commandeVisu.execute();
        break;
      case "+":
        // Ajoute une note spécifiée dans args à ER
        CommandeAddNote commandeAddNote = new CommandeAddNote(er, args[0]);
        commandeAddNote.execute();
        break;
      case "-":
        // Supprime une note de ER
        CommandeDeleteNote commandeDeleteNote = new CommandeDeleteNote(er);
        commandeDeleteNote.execute();
        break;
      default:
        // Gère l'entrée d'une commande inconnue
        display.printError("Commande inconnue");
        break;
    }
  }

  /**
   * Surcharge de la méthode executeCommand pour gérer les commandes sans besoin d'ER.
   *
   * @param command La commande entrée par l'utilisateur.
   */
  public void executeCommand(String command) {
    switch (command) {
      case "..":
        // Remonte au répertoire parent
        CommandeGoBack commandeGoBack = new CommandeGoBack(commandeContext);
        commandeGoBack.execute();
        break;
      case "past":
        // Colle le contenu précédemment coupé ou copié
        CommandePaste commandePaste = new CommandePaste(commandeContext);
        commandePaste.execute();
        break;
      case "ls":
        // Liste les fichiers et dossiers dans le répertoire courant
        CommandeListDirectory commandeListDirectory = new CommandeListDirectory(commandeContext);
        commandeListDirectory.execute();
        break;
      case "help":
        // Affiche l'aide utilisateur
        CommandeHelp commandHelp = new CommandeHelp();
        commandHelp.execute();
        break;
      case "notes":
        // Affiche les notes associées à l'élément courant
        CommandeNotes commandNotes = new CommandeNotes(commandeContext);
        commandNotes.execute();
        break;
      default:
        display.printError("Commande inconnue");
        break;
    }
  }

  /**
   * Exécute des commandes relatives à la manipulation de répertoires.
   *
   * @param rep Le répertoire cible pour l'opération.
   * @param command La commande à exécuter
   * @param nom Le nom pour la création ou recherche dans le répertoire.
   */
  public void executeCommand(Repertoire rep, String command, String nom) {
    switch (command) {
      case "mkdir":
        // Crée un nouveau répertoire sous le répertoire actuel
        Repertoire newRepertoire = new Repertoire(rep.getPathnameName(), nom);
        CommandeMkdir commandeMkdir = new CommandeMkdir(commandeContext, newRepertoire);
        commandeMkdir.execute();
        break;
      case "find":
        // Recherche un fichier ou répertoire dans le répertoire actuel
        CommandeFind commandeFind = new CommandeFind(commandeContext, nom);
        commandeFind.execute();
        break;
      default:
        CommandeNull commandeNull = new CommandeNull();
        commandeNull.execute();

        break;
    }
  }
}
