package fr.uvsq.pglp.project;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;


/**
 * Classe abstraite d'éléments de répertoires dont vont hériter Fichier et Repertoire.
 */
public abstract class Er {

  public static final String SLASH = FileSystems.getDefault().getSeparator();

  private String name;
  private final String pathname;
  private Notes notes;

  /**
   * Constructeur de la classe.
   */
  public Er(String pathname, String name) {
    this.name = name;
    this.pathname = pathname;
    this.notes = null;
  }


  /**
   * Fonction qui permet d'initialiser une note.
   *
   * @return une instance de Notes
   */
  public Notes notesInitialisation() {
    this.notes = new Notes(Paths.get(this.pathname), this.name);
    return this.notes;
  }


  /**
   * Fonction qui permet de récupérer le nom de l'élément de répertoire.
   *
   * @return le nom de l'élément de répertoire
   */
  public String getName() {
    return name;
  }


  /**
   * Fonction qui permet de changer le nom de l'élément de répertoire.
   */
  public void setName(String newName) {
    this.name = newName;
  }


  /**
   * Fonction qui permet de récupérer le chemin jusqu'à l'élément de répertoire.
   *
   * @return le chemin jusqu'à l'élément de répertoire
   */
  public String getPath() {
    return pathname;
  }


  /**
   * Méthode qui permet de récupérer la taille de l'élément de répertoire.
   *
   * @return taille en bits de l'élément de répertoire
   */
  public long getSize() {
    File file = new File(this.getPathnameName());
    if (file.exists()) {
      return FileUtils.sizeOf(file);
    } else {
      return 0;
    }
  }


  /**
   * Fonction qui permet de concaténer le chemin jusqu'à l'élément de répertoire
   * avec le nom de ce dernier.
   *
   * @return le chemin complet de l'élément de répertoire
   */
  public String getPathnameName() {
    if (pathname == null) {
      return name;
    } else {
      return pathname + SLASH + name;
    }
  }


  /**
   * Fonction qui permet d'afficher le chemin complet de l'élément de répertoire.
   *
   * @return le chemin complet de l'élément de répertoire
   */
  @Override
  public String toString() {
    return "ER{"
            + "pathname='" + pathname
            + '\''
            + ", name='" + name + '\''
            + '}';
  }
}

