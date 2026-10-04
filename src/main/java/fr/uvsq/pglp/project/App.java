package fr.uvsq.pglp.project;

import fr.uvsq.pglp.project.interpreter.TerminalInterpreter;


/**
 * La classe principale de l'application.
 */
public class App {



  /**
   * La méthode principale qui démarre l'application.
   *
   * @param args arguments de la ligne de commande
   */
  public static void main(String[] args) {
    App app = new App();
    app.run();
  }


  public void run() {
    TerminalInterpreter terminalInterpreter = new TerminalInterpreter();
    terminalInterpreter.process();
  }
}