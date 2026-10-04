package fr.uvsq.pglp.project.commandes;

import fr.uvsq.pglp.project.Er;
import fr.uvsq.pglp.project.Notes;
import fr.uvsq.pglp.project.display.Color;
import fr.uvsq.pglp.project.display.TerminalDisplay;


/**
 * Classe de commande pour ajouter une note à un élément de répertoire.
 */
public class CommandeAddNote implements Commande {

  private final Er er;

  private final String message;

  private final TerminalDisplay display = new TerminalDisplay();

  public CommandeAddNote(Er er, String message) {
    this.er = er;
    this.message = message;
  }

  @Override
  public void execute() {
    Notes note = this.er.notesInitialisation();

    try {
      note.addNotes(message);
      display.printMessage("La note a été ajouté avec succès",
              Color.GREEN);
    } catch (Exception e) {
      display.printError("Erreur lors de l'ajout de la note");
    }
  }
}