package fr.uvsq.pglp.project.commandes;

import fr.uvsq.pglp.project.CommandeContext;
import fr.uvsq.pglp.project.Er;
import fr.uvsq.pglp.project.Repertoire;
import fr.uvsq.pglp.project.display.Color;
import fr.uvsq.pglp.project.display.TerminalDisplay;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


/**
 * Classe de commande pour gérer la recherche d'élément de répertoire.
 */
public class CommandeFind implements Commande {

  //private ER er;
  private final String nom;
  CommandeContext commandeContext;
  TerminalDisplay display = new TerminalDisplay();

  public CommandeFind(CommandeContext commandeContext, String nom) {
    this.commandeContext = commandeContext;
    this.nom = nom;
  }

  @Override
  public void execute() {
    Repertoire repertoireCourant = commandeContext.getCurrentRepertoire();
    Path path = Paths.get(repertoireCourant.getPathnameName());
    List<Er> listFiles = null;
    
    try {
      listFiles = repertoireCourant.findElement(nom);
      for (Er er : listFiles) {
        // "relativize" permet d'obtenir le chemin de file moins le chemin de notre répertoire courant
        display.printMessage(path.relativize(Paths.get(er.getPathnameName())).toString(), Color.DEFAULT);
      }
      if (listFiles.isEmpty()) {
        display.printError("Aucun fichier trouvé");
      }
    } catch (IOException e) {
      display.printError(e.getMessage());
    }
  }
}