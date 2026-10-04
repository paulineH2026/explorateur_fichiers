package fr.uvsq.pglp.project.commandes;

import fr.uvsq.pglp.project.CommandeContext;
import fr.uvsq.pglp.project.Er;
import fr.uvsq.pglp.project.Repertoire;
import fr.uvsq.pglp.project.display.TerminalDisplay;
import java.io.IOException;

/**
 * Classe de commande pour gérer la création de répertoire.
 */
public class CommandeMkdir implements Commande {

  // pour récupérer le répertoire courant
  CommandeContext commandeContext;
  TerminalDisplay display = new TerminalDisplay();
  private final Er er;

  public CommandeMkdir(CommandeContext commandeContext, Er er) {
    this.commandeContext = commandeContext;
    this.er = er;
  }

  @Override
  public void execute() {
    try {
      Repertoire repertoireCourant = commandeContext.getCurrentRepertoire();
      repertoireCourant.addElement(er);
    } catch (IOException e) {
      display.printError(e.getMessage());
    }
  }
}