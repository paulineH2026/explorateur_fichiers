package fr.uvsq.pglp.project.commandes;

import fr.uvsq.pglp.project.Er;
import fr.uvsq.pglp.project.Notes;
import fr.uvsq.pglp.project.display.Color;
import fr.uvsq.pglp.project.display.TerminalDisplay;


/**
 * Classe de commande pour gérer la suppression de notes d'un élément de repertoire.
 */
public class CommandeDeleteNote implements Commande {

  private final Er er;

  private final TerminalDisplay display = new TerminalDisplay();

  public CommandeDeleteNote(Er er) {
    this.er = er;
  }

  @Override
  public void execute() {
    Notes note = er.notesInitialisation();
    try {
      note.removeNotes();
      display.printMessage("Note supprimée avec succès !",
              Color.GREEN);
    } catch (Exception e) {
      display.printError("Erreur lors de la suppression de la note");
    }
  }
}
