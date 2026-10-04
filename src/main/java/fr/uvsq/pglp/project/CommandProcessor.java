package fr.uvsq.pglp.project;

import fr.uvsq.pglp.project.display.TerminalDisplay;
import java.io.IOException;


/**
 * Cette classe est utilisée pour analyser les
 * données rentrées par l'utilisateur en ligne de commande.
 */
public class CommandProcessor {

  CommandeContext commandeContext;

  public CommandProcessor(CommandeContext commandeContext) {
    this.commandeContext = commandeContext;
  }

  TerminalDisplay display = new TerminalDisplay();
  // Un index stocké pour référencer un élément spécifique, par défault cet index est nul
  Integer storedIndex = null;

  Er storedEr = null;


  /**
   * Cette méthode est utilisée pour commencer à traiter les commandes entrées par l'utilisateur.
   * Elle utilise des expressions régulières pour identifier les commandes dans la ligne entrée.
   * Elle gère également les cas où une commande peut nécessiter un NER (Numéro d'Enregistrement)
   * ou une commande seule précédée ou non par un NER.
   *
   * @param line La ligne de commande entrée par l'utilisateur.
   * @throws IOException Si une erreur d'entrée/sortie se produit.
   */
  public void startProcessing(String line) throws IOException {

    Repertoire repertoireCourant = commandeContext.getCurrentRepertoire();
    AppelCommande appelCommande = new AppelCommande(commandeContext);
    TerminalDisplay display = new TerminalDisplay();

    // nombre de groupes maximum que l'on peut faire avec une ligne de commande
    String[] parts = getParts(line);
    // Si l'utilisateur a renseigné un NER (^\\d+$ expression régulière pour un entier)
    // la variable prend la valeur true
    // False sinon
    boolean isNumber = parts.length > 0 && parts[0].matches("^\\d+$");

    // Si la ligne est vide, on sort
    if (line.isEmpty()) {
      return;
    }

    // Dans le cas où une commande peut avoir besoin d'un NER
    // (NER seul) ou (commande seule précédée ou non par un NER)
    // + expression régulière au moins une fois
    // ? expression régulière au plus une fois
    // * expression régulière pour autant de fois que l'on veut, zéro étant accepté
    if (line.matches("^(\\d+|(\\d+\\s+)?(copy|cut|visu|del|[.\\-]|(\\+(\\s+\\S+)*)))$")) {
      // Cas où on a qu'un seul token
      if (parts.length == 1) {
        // Si on a renseigné qu'un NER
        if (isNumber) {
          // parseInt transforme le String en Integer
          storedIndex = Integer.parseInt(parts[0]) - 1;
          //on affiche les notes associées à l'élément courant si elles existent
          storedEr = checkNer(storedIndex);
          if (storedEr == null) {
            return;
          }
          display.printMessage("L'élément courant est maintenant " + parts[0]);
          display.displayNotes(storedEr);
        } else {
          // si l'utilisateur n'a rentré qu'un + sans indiquer le message à ajouter
          if (parts[0].equals("+")) {
            display.printError("Veuillez renseigner un nom de note");
          } else { // si l'utilisateur a entré par exemple copy, -, cut ...
            try {
              storedEr = repertoireCourant.getContent().get(storedIndex);
            } catch (IOException e) {
              display.printError(e.getMessage());
            }
            if (storedEr == null) {
              return;
            }
            appelCommande.executeCommand(parts[0], storedEr);
          }
        }
      } else if (parts.length == 2) { // Cas où on a deux tokens
        // si le premier token est un NER
        if (isNumber) {
          storedIndex = Integer.parseInt(parts[0]) - 1;
          storedEr = checkNer(storedIndex);
          if (storedEr == null) {
            return;
          }

          display.printMessage("L'élément courant est maintenant " + parts[0]);
          appelCommande.executeCommand(parts[1], storedEr);
        } else if (parts[0].equals("+")) { // si le premier token est un +
          storedEr = checkNer(storedIndex);
          if (storedEr == null) {
            return;
          }
          appelCommande.executeCommand(parts[0], storedEr, parts[1]);
        }
      } else if (parts.length == 3) { // Cas où on a trois tokens
        // entrée valide que si le premier token est un NER
        if (isNumber) {
          storedIndex = Integer.parseInt(parts[0]) - 1;
          storedEr = checkNer(storedIndex);
          if (storedEr == null) {
            return;
          }
          display.printMessage("L'élément courant est maintenant " + parts[0]);
          appelCommande.executeCommand(parts[1], storedEr, parts[2]);
        }
      }
    } else if (line.matches("^(mkdir|find)(\\s+\\S+)?$")) { // Cas où la commande n'a pas
      appelCommande.executeCommand(repertoireCourant, parts[0], parts[1]);
    } else {
      appelCommande.executeCommand(parts[0]);
    }
  }

  private static String[] getParts(String line) {
    int limit = 3;
    // dans le cas où l'utilisateur veut ajouter une note sans renseigner
    // de NER, il ne faut séparer la commande
    // qu'en deux au maximum (le + et le message)
    if (line.startsWith("+")) {
      limit = 2;
    }
    // Utilisation d'expressions régulières pour identifier les commandes dans la ligne
    // parts est un tableau contenant au maximum trois/deux tokens,
    // premiers éléments de la ligne séparés par des espaces
    return line.trim().split("\\s+", limit);
  }

  private Er checkNer(int ner) {
    Repertoire repertoireCourant = commandeContext.getCurrentRepertoire();
    Er element;
    try {
      element = repertoireCourant.getContent().get(ner);
    } catch (IOException e) {
      display.printError(e.getMessage());
      return null;
    } catch (IndexOutOfBoundsException e) {
      display.printError("veuillez entrer un numéro valide");
      return null;
    }
    return element;
  }
}
