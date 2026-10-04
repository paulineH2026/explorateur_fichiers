package fr.uvsq.pglp.project.display;

import fr.uvsq.pglp.project.Er;


/**
 * Interface pour gérer l'affichage de l'application.
 */
public interface Display {

  void printError(String error);

  void printMessage(String message, Color color);

  void printMessage(String message);

  String displayPath(Er er);

  void displayFile(Er er);

  void displayNotes(Er er);

  void displayAllNotes(Er er);
}
