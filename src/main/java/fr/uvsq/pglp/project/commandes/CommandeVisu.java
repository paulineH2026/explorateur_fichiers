package fr.uvsq.pglp.project.commandes;

import fr.uvsq.pglp.project.Er;
import fr.uvsq.pglp.project.display.TerminalDisplay;

/**
 * Classe de commande pour gérer visualisation des éléments de répertoire.
 */
public class CommandeVisu implements Commande {

  private final TerminalDisplay display = new TerminalDisplay();
  private final Er er;

  public CommandeVisu(Er er) {
    this.er = er;
  }

  @Override
  public void execute() {
    display.displayFile(er);
  }
}