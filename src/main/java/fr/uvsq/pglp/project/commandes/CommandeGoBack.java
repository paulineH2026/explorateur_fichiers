package fr.uvsq.pglp.project.commandes;

import fr.uvsq.pglp.project.CommandeContext;
import fr.uvsq.pglp.project.Repertoire;
import fr.uvsq.pglp.project.display.TerminalDisplay;

/**
 * Classe de commande pour gérer la navigation arrière dans le système de fichier.
 */
public class CommandeGoBack implements Commande {

  CommandeContext commandContext;
  private final TerminalDisplay display = new TerminalDisplay();

  public CommandeGoBack(CommandeContext commandContext) {
    this.commandContext = commandContext;
  }

  @Override
  public void execute() {
    Repertoire repertoireCourant = commandContext.getCurrentRepertoire();
    Repertoire repertoireParent = repertoireCourant.getParent();
    if (repertoireParent != null) {
      commandContext.setCurrentRepertoire(repertoireParent);
    } else {
      display.printError("At top level, can't go back any further");
    }
  }
}
