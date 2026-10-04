package fr.uvsq.pglp.project.commandes;

import fr.uvsq.pglp.project.CommandeContext;
import fr.uvsq.pglp.project.Er;
import fr.uvsq.pglp.project.Repertoire;
import fr.uvsq.pglp.project.display.Color;
import fr.uvsq.pglp.project.display.TerminalDisplay;
import java.io.IOException;
import java.util.List;


/**
 * Classe de commande pour gérer les éléments d'un répertoire.
 */
public class CommandeListDirectory implements Commande {

  private final TerminalDisplay display = new TerminalDisplay();

  CommandeContext commandContext;

  public CommandeListDirectory(CommandeContext commandContext) {
    this.commandContext = commandContext;
  }

  @Override
  public void execute() {
    try {
      // initialisation du contenu du répertoire courant
      List<Er> contents = commandContext.getCurrentRepertoire().getContent();
      if (contents.isEmpty()) {
        display.printMessage("Le dossier est vide", Color.RED);
      } else {
        display.printMessage("Contenu du dossier :", Color.GREEN);
        //on affiche les noms des fichiers en couleur par défaut et les noms des répertoires en mauve
        for (int i = 0; i < contents.size(); i++) {
          Er entry = contents.get(i);
          String entryString = entry.getName();
          String entryDisplay = entry instanceof Repertoire ? display.colorText(entryString, Color.PURPLE)
              : display.colorText(entryString, Color.DEFAULT);
  
          // permet d'assigner à chaque élément du répertoire courant un numéro (on commence à 1)
          String numberDisplay = display.colorText((i + 1) + ". ", Color.GREEN);
          display.printMessage(numberDisplay + entryDisplay);
        }
      }
    } catch (IOException e) {
      display.printError(e.getMessage());
    }
  }
}

