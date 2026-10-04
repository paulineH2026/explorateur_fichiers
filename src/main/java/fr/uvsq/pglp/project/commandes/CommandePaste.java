package fr.uvsq.pglp.project.commandes;

import fr.uvsq.pglp.project.CommandeContext;
import fr.uvsq.pglp.project.Er;
import fr.uvsq.pglp.project.Fichier;
import fr.uvsq.pglp.project.Notes;
import fr.uvsq.pglp.project.OperationType;
import fr.uvsq.pglp.project.Repertoire;
import fr.uvsq.pglp.project.display.Color;
import fr.uvsq.pglp.project.display.TerminalDisplay;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * Classe de commande pour gérer la copie/move des éléments mis en mémoire.
 */
public class CommandePaste implements Commande {

  private final CommandeContext commandeContext;
  private final TerminalDisplay display = new TerminalDisplay();

  public CommandePaste(CommandeContext commandeContext) {
    this.commandeContext = commandeContext;
  }

  @Override
  public void execute() {
    try {
      OperationType action = commandeContext.getType();
  
      if (action == null) {
        display.printError("Il n'y a rien à coller");
        return;
      }
  
      Er savedElement = commandeContext.getSavedElement();
      Er newElement = createNewElement(savedElement);
  
      Er newElement2 = resolveNameCollisions(newElement);
  
      performOperation(savedElement, newElement2, action);
      handleNotes(savedElement, newElement2, action);
    } catch (IOException e) {
      display.printError(e.getMessage());
    }
  }

  private Er createNewElement(Er savedElement) {
    Repertoire currentRepertoire = commandeContext.getCurrentRepertoire();
    if (Files.isDirectory(Path.of(savedElement.getPathnameName()))) {
      return new Repertoire(currentRepertoire.getPathnameName(), savedElement.getName());
    } else {
      return new Fichier(currentRepertoire.getPathnameName(), savedElement.getName());
    }
  }

  private Er resolveNameCollisions(Er newElement) throws IOException {
    while (commandeContext.getCurrentRepertoire().contains(newElement)) {
      newElement.setName(getNextName(newElement));
    }
    return newElement;
  }

  private String getNextName(Er element) {
    String name = element.getName();
    int lastDotIndex = name.lastIndexOf('.');
    if (element instanceof Repertoire || lastDotIndex == -1) {
      name += "-copy";
    } else {
      name = name.substring(0, lastDotIndex) + "-copy" + name.substring(lastDotIndex);
    }
    return name;
  }

  private void performOperation(Er savedElement, Er newElement, OperationType action) {
    try {
      Repertoire currentRepertoire = commandeContext.getCurrentRepertoire();
      currentRepertoire.manageElementCopy(savedElement, newElement, action != OperationType.COPY);
      display.printMessage("L'opération a réussi", Color.GREEN);
    } catch (IOException e) {
      display.printMessage("Une erreur s'est produite lors de l'opération : " + e.getMessage(), Color.RED);
    }
  }

  private void handleNotes(Er savedElement, Er newElement, OperationType action) {
    Notes notes = savedElement.notesInitialisation();
    notes.moveNotes(newElement.getPath(), newElement.getName());
    if (action == OperationType.CUT) {
      notes.removeNotes();
    }
  }

}
