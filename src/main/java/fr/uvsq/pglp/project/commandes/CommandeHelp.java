package fr.uvsq.pglp.project.commandes;

import fr.uvsq.pglp.project.display.Color;
import fr.uvsq.pglp.project.display.TerminalDisplay;


/**
 * Classe de commande pour gérer l'aide.
 */
public class CommandeHelp implements Commande {

  private final TerminalDisplay display = new TerminalDisplay();

  @Override
  public void execute() {
    display.printMessage("Commandes disponibles :", Color.GREEN);
    display.printMessage("[NER] : permet de sélectionner un "
        + "élément du répertoire courant(optionnel)", Color.GREEN);
    display.printMessage("[NER] copy : copie le fichier ou le répertoire sélectionné", Color.GREEN);
    display.printMessage("[NER] cut : coupe le fichier ou le répertoire sélectionné", Color.GREEN);
    display.printMessage("[NER] visu : affiche les notes pour le "
        + "fichier ou le répertoire sélectionné", Color.GREEN);
    display.printMessage("[NER] + <message> : ajoute une note au fichier "
        + "ou au répertoire sélectionné", Color.GREEN);
    display.printMessage("[NER] - : supprime le fichier ou le répertoire sélectionné", Color.GREEN);
    display.printMessage("mkdir <nom_dossier> : crée un nouveau dossier", Color.GREEN);
    display.printMessage("find <nom_fichier> : trouve un fichier avec le "
        + "nom spécifié dans le répertoire courant et ses sous-répertoires", Color.GREEN);
    display.printMessage(".. : retourne au répertoire parent", Color.GREEN);
    display.printMessage("past : colle le fichier ou le répertoire "
        + "qui a été précédemment préparé pour la copie ou la coupe", Color.GREEN);
    display.printMessage("help : affiche les commandes disponibles", Color.GREEN);
    display.printMessage("exit : quitte le terminal", Color.GREEN);
  }
}