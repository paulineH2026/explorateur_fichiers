package fr.uvsq.pglp.project.commandes;

import fr.uvsq.pglp.project.CommandeContext;
import fr.uvsq.pglp.project.Er;
import fr.uvsq.pglp.project.Notes;
import fr.uvsq.pglp.project.Repertoire;
import fr.uvsq.pglp.project.display.TerminalDisplay;
import java.io.IOException;


/**
 * Classe de commande pour gérer la suppression d'élément de repertoire.
 */
public class CommandeDel implements Commande {

  private final Er er;
  CommandeContext commandContext;
  TerminalDisplay display = new TerminalDisplay();

  public CommandeDel(CommandeContext commandContext, Er er) {
    this.commandContext = commandContext;
    this.er = er;
  }

  @Override
  public void execute() {
    try {
      Notes notes = er.notesInitialisation();
      Repertoire repertoireCourant = commandContext.getCurrentRepertoire();
      repertoireCourant.deleteElement(er);
      notes.removeNotes();
      display.printMessage("L'élément a été supprimé avec succès");
    } catch (IOException e) {
      display.printError(e.getMessage());
    }
  }
}
