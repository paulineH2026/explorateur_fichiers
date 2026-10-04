package fr.uvsq.pglp.project;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Classe qui détermine le comportement des objets fichiers.
 */
public class Fichier extends Er {

  public Fichier(String pathname, String name) {
    super(pathname, name);
  }


  /**
   * Lit le contenu du fichier au chemin spécifié.
   *
   * @return le contenu du fichier
   */
  public String readFile() throws IOException {
    StringBuilder sb = new StringBuilder();
    try (// on met dans un buffer le contenu du fichier
    BufferedReader fichier = new BufferedReader(new FileReader(this.getPathnameName()))) {
      String line;
      // tant qu'il y a encore des choses dans le buffer
      while ((line = fichier.readLine()) != null) {
        sb.append(line).append("\n");
      }
    }
    return sb.toString();
  }


  @Override
  public boolean equals(Object o) {
    // si le fichier est l'objet à qui on veut le comparer, on retourne vraie
    if (this == o) {
      return true;
    }
    // si l'objet n'existe pas ou que l'objet n'est pas un fichier, on retourne faux
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    // conversion du type de données : Object en Fichier
    Fichier fichier = (Fichier) o;
    //if (!pathname.equals(fichier.pathname)) return false;
    return this.getName().equals(fichier.getName());
  }

}
