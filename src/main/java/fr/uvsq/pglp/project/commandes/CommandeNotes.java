package fr.uvsq.pglp.project.commandes;

import fr.uvsq.pglp.project.CommandeContext;
import fr.uvsq.pglp.project.display.TerminalDisplay;


/**
 * Classe de commande pour gérer la récupération des notes d'un élément de répertoire.
 */
public class CommandeNotes implements Commande {

  CommandeContext commandContext;

  public CommandeNotes(CommandeContext commandContext) {
    this.commandContext = commandContext;
  }


  @Override
  public void execute() {
    final TerminalDisplay display = new TerminalDisplay();
    display.displayAllNotes(commandContext.getCurrentRepertoire());
  }
}
