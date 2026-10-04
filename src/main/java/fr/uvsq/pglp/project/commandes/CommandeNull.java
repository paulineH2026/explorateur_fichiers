package fr.uvsq.pglp.project.commandes;

import fr.uvsq.pglp.project.display.TerminalDisplay;


/**
 * Classe de commande lorsque l'on a des commandes inconnues.
 */
public class CommandeNull implements Commande {

  @Override
  public void execute() {
    TerminalDisplay display = new TerminalDisplay();
    display.printError("Commande inconnue");
  }

}