package fr.uvsq.pglp.project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;


/**
 * Classe composite qui détermine le comportement des dossiers.
 */
public class Repertoire extends Er {

  // Liste pour stocker les éléments (fichiers et sous-répertoires) du répertoire courant.
  private List<Er> tree = new ArrayList<>();

  // Flag pour vérifier si le contenu du répertoire a déjà été chargé.
  private boolean isFetched = false;

  /**
   * Constructeur de la classe. Initialise le chemin et le nom du répertoire.
   */
  public Repertoire(String pathname, String name) {
    super(pathname, name);
  }


  /**
   * Méthode privée pour charger le contenu du répertoire si ce n'est pas déjà fait.
   */
  private void fetch() throws IOException {
    if (isFetched) {
      return;
    }

    Path currentpath = Paths.get(this.getPathnameName());
    try (Stream<Path> stream = Files.list(currentpath)) {
      tree = stream
              .filter(path -> !path.getFileName().toString().startsWith("."))
              .sorted()
              .map(path -> Files.isDirectory(path)
                  ? new Repertoire(path.getParent().toString(), path.getFileName().toString())
                  : new Fichier(path.getParent().toString(), path.getFileName().toString()))
              .collect(Collectors.toList());
      isFetched = true;
    } catch (IOException e) {
      throw new IOException("Impossible de lister le contenu du dossier: " + e.getMessage());
    }
  }


  /**
   * Récupère et retourne une copie de la liste des éléments contenus dans ce répertoire.
   */
  public List<Er> getContent() throws IOException {
    fetch(); // s'il ne l'est pas déjà
    return new ArrayList<>(tree); // une copie de tree
  }

  /**
   * Récupère et retourne le répertoire parent de ce répertoire.
   */
  public Repertoire getParent() {
    Path currentpath = Paths.get(this.getPathnameName());
    Path parent = currentpath.getParent();
    if (parent == null) {
      return null;
    } else if (parent.getParent() == null) {
      return new Repertoire(null, parent.toString());
    } else {
      return new Repertoire(parent.getParent().toString(), parent.getFileName().toString());
    }
  }

  /**
   * Vérifie si un élément spécifique est contenu dans le répertoire.
   */
  public boolean contains(Er element) throws IOException {
    fetch();
    return tree.contains(element);
  }



  /**
   * Ajoute un nouvel élément (fichier ou sous-répertoire) à ce répertoire.
   */
  public void addElement(Er element) throws IOException {
    if (contains(element)) {
      throw new IOException("Element already exists: " + element.getName());
    }
    if (element instanceof Fichier) {
      createFile((Fichier) element);
    } else if (element instanceof Repertoire) {
      createDirectory((Repertoire) element);
    }
    tree.add(element);
    isFetched = false; // Invalidate cache
  }

  /**
   * Crée un fichier physique sur le disque dans ce répertoire.
   */
  private void createFile(Fichier fichier) throws IOException {
    Path filePath = Paths.get(fichier.getPath(), fichier.getName());
    try {
      Files.createFile(filePath);
    } catch (IOException e) {
      throw new IOException("Impossible de créer le fichier: " + e.getMessage());
    }
  }

  /**
   * Crée un sous-répertoire physique sur le disque dans ce répertoire.
   */
  private void createDirectory(Repertoire repertoire) throws IOException {
    Path directoryPath = Paths.get(repertoire.getPath(), repertoire.getName());
    try {
      Files.createDirectories(directoryPath);
    } catch (IOException e) {
      throw new IOException("Impossible de créer le répertoire: " + e.getMessage());
    }
  }


  /**
   * Gère la copie ou le déplacement d'un élément d'un emplacement à un autre.
   */
  public void manageElementCopy(Er source, Er destination, boolean isMove) throws IOException {
    fetch();
    if (contains(destination)) {
      throw new IOException("Cannot create a directory or file with this name: " + destination.getName());
    }
    Path srcPath = Paths.get(source.getPathnameName());
    Path destPath = Paths.get(destination.getPathnameName());
    try {
      if (isMove) {
        if (Files.isDirectory(srcPath)) {
          FileUtils.moveDirectory(srcPath.toFile(), destPath.toFile());
        } else {
          FileUtils.moveFile(srcPath.toFile(), destPath.toFile());
        }
      } else {
        if (Files.isDirectory(srcPath)) {
          FileUtils.copyDirectory(srcPath.toFile(), destPath.toFile());
        } else {
          FileUtils.copyFile(srcPath.toFile(), destPath.toFile());
        }
      }
      tree.add(destination);
      isFetched = false; // Invalidate cache after modification
    } catch (IOException e) {
      throw new IOException("Failed to " + (isMove ? "move" : "copy") + " element: " + e.getMessage());
    }
  }

  /**
   * Supprime un élément de ce répertoire.
   */
  public void deleteElement(Er element) throws IOException {
    fetch();
    if (!contains(element)) {
      throw new IllegalArgumentException("No such file or directory: " + element.getName());
    }
    try {
      Files.delete(Paths.get(element.getPathnameName()));
      tree.remove(element);
      isFetched = false; // Invalidate cache after modification
    } catch (IOException e) {
      throw new IOException("Failed to delete element: " + e.getMessage());
    }
  }

  /**
   * Trouve des éléments dans ce répertoire qui correspondent à un motif regex spécifié.
   */
  public List<Er> findElement(String pattern) throws IOException {
    fetch();
    RegexFileFilter regexFileFilter = new RegexFileFilter("^" + pattern + "[A-Za-z_0-9\\-]*" + "(\\.[^.]+)*$");
    Collection<File> files = FileUtils.listFiles(Paths.get(this.getPathnameName()).toFile(),
        regexFileFilter, TrueFileFilter.INSTANCE);
    // regexFileFilter : on applique un filtre sur les fichiers
    // TrueFileFilter.INSTANCE : on passe tous les répertoires du répertoire courant en revue
    return files.stream()
            .map(file -> file.isDirectory() ? new Repertoire(file.getParent(), file.getName())
                : new Fichier(file.getParent(), file.getName()))
            .collect(Collectors.toList());
  }


  /**
   * Vérifie l'égalité de deux répertoires basée uniquement sur leur nom.
   */
  @Override
  public boolean equals(Object o) {
    // si le répertoire est l'objet à qui on veut le comparer, on retourne vraie
    if (this == o) {
      return true;
    }
    // si l'objet n'existe pas ou que l'objet n'est pas un répertoire, on retourne faux
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Repertoire that = (Repertoire) o;
    return getName().equals(that.getName());
  }
}

