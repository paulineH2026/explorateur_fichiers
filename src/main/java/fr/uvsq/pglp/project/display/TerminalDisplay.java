package fr.uvsq.pglp.project.display;

import fr.uvsq.pglp.project.Er;
import fr.uvsq.pglp.project.Fichier;
import fr.uvsq.pglp.project.Notes;
import fr.uvsq.pglp.project.Repertoire;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.apache.commons.io.FilenameUtils;
import org.fusesource.jansi.Ansi;
import org.json.JSONArray;


/**
 * Classe d'affichage en ligne de commande.
 */
public class TerminalDisplay implements Display {

  /**
   * Cette méthode colore un texte donné avec la couleur spécifiée.
   *
   * @param text  Le texte à colorer
   * @param color La couleur à utiliser
   * @return Le texte coloré.
   */
  public String colorText(String text, Color color) {
    Ansi ansi = Ansi.ansi();

    switch (color) {
      case BLUE -> ansi.fg(Ansi.Color.BLUE);
      case GREEN -> ansi.fg(Ansi.Color.GREEN);
      case RED -> ansi.fg(Ansi.Color.RED);
      case YELLOW -> ansi.fg(Ansi.Color.YELLOW);
      case PURPLE -> ansi.fg(Ansi.Color.MAGENTA);
      case CYAN -> ansi.fg(Ansi.Color.CYAN);
      case DEFAULT -> { /* Do nothing for default */ }
      default -> { }
    }
    // si la couleur indiquée est DEFAULT alors ne change pas la couleur
    // du message, sinon on applique la coloration
    return (color == Color.DEFAULT) ? text : ansi.a(text).reset().toString();
  }

  @Override
  public void printError(String error) {
    System.out.println(colorText(error, Color.RED));
  }

  @Override
  public void printMessage(String message, Color c) {
    System.out.println(colorText(message, c));
  }

  @Override
  public void printMessage(String message) {
    System.out.println(colorText(message, Color.DEFAULT));
  }


  /**
   * Cette méthode affiche le chemin d'un répertoire.
   *
   */
  @Override
  public String displayPath(Er er) {
    Path currentpath;
    if (er.getPath() == null) {
      currentpath = Paths.get(er.getName());
    } else {
      currentpath = Paths.get(er.getPath() + Repertoire.SLASH + er.getName());
    }

    String fullPath = currentpath.toString();
    //récupère le chemin vers le répertoire utilisateur
    String homeDir = System.getProperty("user.home");
    //pour faire comme dans un vrai terminal, si on se trouve dans le
    // répertoire utilisateur, on affiche un ~ avant
    return fullPath.startsWith(homeDir) ? "~" + fullPath.substring(homeDir.length()) : fullPath;
  }


  @Override
  public void displayFile(Er er) {
    try {
      String path = er.getPathnameName();
      // on récupère l'extension du fichier
      String extension = FilenameUtils.getExtension(path);

      // le nom de la fenêtre est le nom du fichier et on crée la fenêtre
      JFrame frame = new JFrame(er.getName());
      // pour organiser les éléments dans la fenêtre
      frame.setLayout(new BorderLayout());
      //System.out.println(path);

      // dans le cas des images
      if (extension.equalsIgnoreCase("png")
          | extension.equalsIgnoreCase("jpeg")
          | extension.equalsIgnoreCase("jpg")) {
        // création d'un ImageIcon depuis un chemin spécifié
        ImageIcon image = new ImageIcon(path);
        // pour afficher notre ImageIcon dans la fenêtre
        JLabel imageLabel = new JLabel(image);
        frame.add(imageLabel, BorderLayout.CENTER);
      } else if (extension.equalsIgnoreCase("txt")) {
        // dans text, on met le contenu du fichier
        String text = ((Fichier) er).readFile();
        // pour afficher des lignes de text de façon simple
        JTextArea textArea = new JTextArea(text);
        // on ne peut pas modifier l'espace de texte
        textArea.setEditable(false);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        // si le contenu du fichier est trop long, on peut scroller
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
      } else { // si on a autre chose qu'un fichier texte ou image ou renvoie la taille
        long size = er.getSize();

        if (size <= 0) {
          System.out.println("0 octets");
        } else {
          // unités que peut prendre la taille
          final String[] units = new String[]{"octets", "Ko", "Mo", "Go", "To"};
          int res = (int) (Math.log10(size) / Math.log10(1024));
          printMessage(new DecimalFormat("# ##0.#").format(size / Math.pow(1024, res)) + units[res], Color.PURPLE);
        }
        return;
      }

      // définit la taille de la fenêtre pour contenir tout ce que l'on veut afficher
      frame.pack();
      // fenêtre au milieu de l'écran
      frame.setLocationRelativeTo(null);
      // pour voir la fenêtre
      frame.setVisible(true);

      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    } catch (IOException e) {
      printError(e.getMessage());
    }
  }

  @Override
  public void displayNotes(Er er) {

    Notes notes = er.notesInitialisation();

    JSONArray notesList = notes.getNotes();
    if (!notesList.isEmpty()) {
      printMessage("Notes: ", Color.GREEN);
      for (int i = 0; i < notesList.length(); i++) {
        printMessage(notesList.getString(i), Color.DEFAULT);
      }
    }
  }

  @Override
  public void displayAllNotes(Er er) {
    try {
      Repertoire repertoire = (Repertoire) er;
      List<Er> elementsNotes = repertoire.getContent();
      printMessage("Notes: ", Color.GREEN);
      for (Er element : elementsNotes) {
        Notes notesElement = element.notesInitialisation();
        JSONArray notesList = notesElement.getNotes();
        if (!notesList.isEmpty()) {
          printMessage("   " + element.getName() + ":", Color.GREEN);
          for (int i = 0; i < notesList.length(); i++) {
            printMessage("      - " + notesList.getString(i), Color.DEFAULT);
          }
        }
      }
    } catch (IOException e) {
      printError("Impossible de lister les notes.");
    }
  }
}

