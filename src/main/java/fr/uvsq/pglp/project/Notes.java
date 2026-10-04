package fr.uvsq.pglp.project;

import fr.uvsq.pglp.project.display.TerminalDisplay;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Classe qui permet de gérer les notes.
 */
public class Notes {

  // pour afficher
  TerminalDisplay display = new TerminalDisplay();
  // là où sont stockées les notes
  private final Path chemin;
  // le titre de la note, le nom de l'élément de répertoire
  private final String label;

  public Notes(Path chemin, String label) {
    this.chemin = chemin.resolve(".notes.json");
    this.label = label;
  }


  /**
   * Méthode pour lire un fichier JSON et retourner un JSONObject.
   *
   * @return un JSONObject représentant le contenu du fichier JSON
   * @throws IOException si une erreur d'E/S se produit
   */
  public JSONObject readJsonFile() throws IOException {

    // si le chemin vers le fichier json existe
    if (this.chemin.toFile().exists()) {
      String jsonContent = Files.readString(this.chemin);
      return new JSONObject(jsonContent);
    }
    return null;
  }


  /**
   * Méthode d'aide pour obtenir un JSONArray de notes.
   *
   * @param note le JSONObject contenant les notes
   * @return un JSONArray de notes
   */
  public JSONArray getNotesArray(JSONObject note) {
    // si le JSONObjet n'est pas nul et qu'une note a déjà été ajouté au fichier, on applique le get
    return note != null && note.has(this.label)
        ? note.getJSONArray(this.label) : new JSONArray();
  }


  /**
   * Méthode écrire dans un fichier une note.
   *
   * @param note ce que l'on veut écrire
   * @throws IOException si une erreur d'E/S se produit
   */
  public void writeJsonFile(JSONObject note) throws IOException {
    Files.writeString(this.chemin, note.toString(4), StandardCharsets.UTF_8);
  }


  /**
   * Ajoute une note associée à un fichier/dossier.
   *
   * @param message la note que l'on veut écrire
   **/
  public void addNotes(String message) {

    JSONObject note;
    try {
      note = readJsonFile();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    if (note == null) {
      note = new JSONObject();
    }
    //liste qui récupère les notes déjà associées au chemin
    JSONArray notesList = getNotesArray(note);
    // on ajoute la note dans la liste
    notesList.put(message);
    // on rajoute dans l'objet la liste
    note.put(this.label, notesList);

    // on ajoute l'objet au fichier
    try {
      writeJsonFile(note);
    } catch (IOException e) {
      throw new RuntimeException("Erreur lors de l'écriture de la note" + e.getMessage());
    }

  }


  /**
   * Supprime les notes d'un fichier/répertoire.
   *
   */
  public void removeNotes()  {
    JSONObject note;
    try {
      note = readJsonFile();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    //si cet élément a bien déjà été annoté
    if (note != null && note.has(this.label)) {
      note.remove(this.label);

      //on supprime le fichier si l'élément était le seul
      if (note.isEmpty()) {
        try {
          Files.delete(this.chemin);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      } else { // on réécrit le fichier sinon
        try {
          writeJsonFile(note);
        } catch (IOException e) {
          throw new RuntimeException("Erreur lors de la suppression de la note" + e.getMessage());
        }

      }
    }
  }


  /**
   * Retourne un JSONArray contenant les notes d'un fichier/répertoire spécifique.
   *
   * @return un JSONArray de notes
   */
  public JSONArray getNotes() {
    JSONObject note;
    try {
      note = readJsonFile();
    } catch (IOException e) {
      throw new RuntimeException("Erreur lors de la récupération des notes" + e.getMessage());
    }
    return getNotesArray(note);
  }


  /**
   * Permet de copier une note associée à un élement de répertoire vers un autre.
   *
   * @param chemin le chemin vers fichier json
   * @param label le titre de la note dans le fichier
   */
  public void moveNotes(String chemin, String label) {
    Path destination = Paths.get(chemin);
    JSONArray notesList = getNotes();
    if (notesList != null) {
      for (int i = 0; i < notesList.length(); i++) {
        // on ajoute les notes dans le répertoire courant de la destination
        Notes newNotes = new Notes(destination, label);
        newNotes.addNotes(notesList.getString(i));
      }
    }
  }

}
