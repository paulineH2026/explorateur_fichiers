package fr.uvsq.pglp.project;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Cette classe est responsable de la gestion des répertoires dans l'application.
 * Elle fournit des méthodes pour obtenir et définir le répertoire courant,
 * changer le répertoire courant. Elle permet aussi de garder en mémoire
 * un élément ainsi qu'une opération.
 */
public class CommandeContext {

  // Repertoire courant en mémoire
  // Le répertoire courant lorsqu'on lance le programme est
  // notre répertoire utilisateur
  private Repertoire currentRepertoire = getHomeRepertoire();

  //Element en mémoire
  private Er savedElement;

  //Opération en mémoire
  private OperationType type = null;


  /**
   * Fonction appelée lorsque l'on définit pour la première fois le répertoire
   * courant. Elle renvoie un élément de type Repertoire qui correspond à notre répertoire "home".
   *
   * @return le répertoire "home", notre répertoire utilisateur
   */
  public Repertoire getHomeRepertoire() {
    Path origin = Paths.get(System.getProperty("user.home")).toAbsolutePath();
    String pathname = origin.getParent().toString();
    String name = origin.getFileName().toString();

    return new Repertoire(pathname, name);
  }

  /**
   * Fonction qui permet de récupérer le répertoire courant.
   *
   * @return le répertoire courant
   */
  public Repertoire getCurrentRepertoire() {
    return currentRepertoire;
  }


  /**
   * Fonction qui permet de définir le répertoire courant.
   */
  public void setCurrentRepertoire(Repertoire currentRepertoire) {
    this.currentRepertoire = currentRepertoire;
  }


  /**
   * Fonction qui permet de récupérer l'élément gardé en mémoire.
   *
   * @return le répertoire sauvegardé
   */
  public Er getSavedElement() {
    return savedElement;
  }


  /**
   * Fonction qui permet de définir le répertoire à sauvegarder.
   */
  public void setSavedElement(Er element) {
    this.savedElement = element;
  }


  /**
   * Fonction qui permet de récupérer l'opération gardée en mémoire.
   *
   * @return l'opération sauvegardée
   */
  public OperationType getType() {
    return type;
  }


  /**
   * Fonction qui permet de définir l'opération à sauvegarder.
   */
  public void setType(OperationType type) {
    this.type = type;
  }


}