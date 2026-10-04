package fr.uvsq.pglp.project.interpreter;

import fr.uvsq.pglp.project.CommandProcessor;
import fr.uvsq.pglp.project.CommandeContext;
import fr.uvsq.pglp.project.display.TerminalDisplay;
import java.io.IOException;
import org.fusesource.jansi.AnsiConsole;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;


/**
 * Classe qui permet de gérer l'interprétation en ligne de commande.
 */
public class TerminalInterpreter implements CommandInterpreter {

  // permet de gérer ce que l'on garde en mémoire pour le bon fonctionnement de l'application
  CommandeContext commandeContext = new CommandeContext();

  // Un booléen indiquant si l'application est en cours d'exécution
  private boolean running = true;

  /**
   * La méthode process est responsable de la boucle principale de l'application.
   * Elle configure le terminal, affiche le contenu du répertoire et les notes,
   * puis entre dans une boucle où elle lit l'entrée de l'utilisateur et traite les commandes.
   * La boucle continue jusqu'à ce que la commande 'exit' soit entrée.
   */
  @Override
  public void process() {
    // Permet de mettre la console Jansi (console.out) sur la sortie standard (System.out)
    // pour avoir de la couleur
    AnsiConsole.systemInstall();

    try {
      // Configuration du terminal
      Terminal terminal = TerminalBuilder.builder().system(true).build();
      LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();


      CommandProcessor commandProcessor = new CommandProcessor(commandeContext);
      TerminalDisplay terminalDisplay = new TerminalDisplay();
      // Boucle principale
      while (running) {
        // Lecture de l'entrée de l'utilisateur
        String line = lineReader.readLine(terminalDisplay.displayPath(commandeContext.getCurrentRepertoire())
            + " >> ").trim();
        // Vérification si la commande 'exit' a été entrée
        if ("exit".equals(line)) {
          running = false;
        } else {
          commandProcessor.startProcessing(line);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
