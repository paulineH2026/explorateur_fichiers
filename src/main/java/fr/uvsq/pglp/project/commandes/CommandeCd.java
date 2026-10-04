package fr.uvsq.pglp.project.commandes;

import fr.uvsq.pglp.project.CommandeContext;
import fr.uvsq.pglp.project.Er;
import fr.uvsq.pglp.project.Repertoire;
import fr.uvsq.pglp.project.display.TerminalDisplay;
import java.io.IOException;


/**
 * Classe de commande pour gérer le changement de répertoire courant.
 */
public class CommandeCd implements Commande {

  private final TerminalDisplay terminalDisplay = new TerminalDisplay();
  CommandeContext commandContext;

  private final Er er;

  public CommandeCd(CommandeContext commandContext, Er er) {
    this.commandContext = commandContext;
    this.er = er;
  }

  @Override
  public void execute() {
    try {
      if (er instanceof Repertoire && commandContext.getCurrentRepertoire().contains(er)) {
        // le chemin récupéré devient le nouveau répertoire courant
        commandContext.setCurrentRepertoire((Repertoire) er);
  
      } else {
        terminalDisplay.printError(er.getName() + " n'est pas un sous repertoire valide");
      }
    } catch (IOException e) {
      terminalDisplay.printError(e.getMessage());
    }
  }
}