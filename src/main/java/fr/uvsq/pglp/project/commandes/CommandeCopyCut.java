package fr.uvsq.pglp.project.commandes;

import fr.uvsq.pglp.project.CommandeContext;
import fr.uvsq.pglp.project.Er;
import fr.uvsq.pglp.project.OperationType;


/**
 * Classe de commande pour gérer la mise en mémoire de copy/cut.
 */
public class CommandeCopyCut implements Commande {

  private final Er er;
  private final OperationType type;
  CommandeContext commandeContext;


  /**
   * Constructeur de la classe.
   */
  public CommandeCopyCut(CommandeContext commandeContext, Er er, OperationType type) {
    this.commandeContext = commandeContext;
    this.er = er;
    this.type = type;
  }


  @Override
  public void execute() {
    commandeContext.setType(type);
    commandeContext.setSavedElement(er);
  }
}