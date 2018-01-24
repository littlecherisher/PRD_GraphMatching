package Tools;

import java.io.*;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;


/**
 * Classe rassemblant des méthodes de manipulation de fichiers
 */
public class Fichier {
    /**
     * Copie des fichiers sur le serveur
     * @param fichier     InputStream renvoyé par le formulaire web
     * @param destination Dossier de stockage
     * @param name        nom du fichier
     */
    public static void copie(InputStream fichier, Path destination, String name) {
        try {
            if (!Files.exists(destination)) {
                Files.createDirectories(destination);
            }
            destination = Paths.get(destination.toString(), name);
            Files.copy(fichier, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Extraction des fichiers contenus dans le zip
     * @param destDir Dossier source et destination
     * @param name    nom du fichier zip
     */
    public static void unzip(Path destDir, String name) {
        Path zipFilename = Paths.get(destDir.toString(), name);
        try (FileSystem zipFileSystem = createZipFileSystem(zipFilename)) {
            final Path root = zipFileSystem.getPath("/");
            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    final Path destFile = Paths.get(destDir.toString(), file.getFileName().toString());
                    //System.out.printf("Extracting file %s to %s\n", file, destFile);
                    Files.copy(file, destFile, StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }
                /*@Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    final Path dirToCreate = Paths.get(destDir.toString(), dir.toString());
                    if (Files.notExists(dirToCreate)) {
                        Files.createDirectory(dirToCreate);
                    }
                    return FileVisitResult.CONTINUE;
                }*/
            });
            zipFileSystem.close();
            //suppression du fichier zip après décompression
            Files.delete(zipFilename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Transforme le path du zip en uri pour la décompression
     * @param path chemin du fichier zip
     * @return chemin du fichier zip sous forme d'URI
     * @throws IOException problème de lecture
     */
    private static FileSystem createZipFileSystem(Path path) throws IOException {
        File zip = new File(path.toString());
        URI uri = URI.create("jar:" + zip.toURI());
        //final URI uri = URI.create("jar:file:" + path.toUri().getPath());
        final Map<String, String> env = new HashMap<>();
        return FileSystems.newFileSystem(uri, env);
    }

    /**
     * Retourne la liste du nom des fichiers présents dans le répertoire
     * @param path chemin du répertoire à explorer
     * @return liste du nom des fichiers présents dans le répertoire
     */
    public static List<String> listeFichiers(String path) {
        List<String> files = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    files.add(filePath.toString());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    /**
     * Transforme les fichiers graphes.gxl en .dat et retourne le nombre de graphes dans le dossier
     * @param path chemin du dossier contenant les fichiers à convertir
     * @return nombre de graphes dans le dossier
     */
    public static int gxlVersDat(String path) {
        File rep = new File(path);
        File[] list = rep.listFiles();
        //si on a des fichiers . gxl
        if (list[0].toString().endsWith(".gxl")) {
            Runtime runtime = Runtime.getRuntime();
            try {
                //on appel l'exécutable chargé de transformer les fichiers en .dat
                Process p = runtime.exec(new String[]{"ParseXML.exe", Paths.get(path).toAbsolutePath().toString()});
                p.waitFor();
                //on supprime tout les fichiers .gxl
                for (File f : list) {
                    f.delete();
                }
                rep.delete();
                //on renomme le répertoire dataset_DAT en dataset
                File repDat = new File(path + "_DAT");
                repDat.renameTo(new File(path));

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        //on en profite pour retourner le nombre de graphes du dataset
        return list.length;
    }

    /**
     * Lis un fichier graphe et renvoie vrai si c'est un graphe dirigé, sinon faux
     * @param path chemin du fichier graphe
     * @return vrai si c'est un graphe dirigé, sinon faux
     */
    public static boolean isDirected(String path) {
        File rep = new File(path);
        File[] list = rep.listFiles();
        BufferedReader in = null;
        boolean directed = true;
        try {
            //on lit la première ligne du premier fichier
            in = new BufferedReader(new FileReader(list[0]));
            String line = in.readLine();
            //si la ligne contient undirected --> graphes non dirigés
            if (line.contains("undirected")) directed = false;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //on ferme le flux de lecture
            try {
                if (in != null) in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directed;
    }

    /**
     * Vide le répertoire
     * @param path chemin du répertoire à vider
     */
    public static void cleanRep(String path) {
        if (Files.exists(Paths.get(path))) {
            File rep = new File(path);
            File[] list = rep.listFiles();
            for (File f : list) {
                f.delete();
            }
        }
    }

    /**
     * Lis un fichier Subset et retourne la liste des graphes associés
     * @param path chemin du fichier Subset
     * @return liste des graphes du Subset
     */
    public static List<String> lectureSubset(String path) {

        BufferedReader in = null;
        List<String> graphes = new ArrayList<>();
        String racine = path.substring(0, (path.lastIndexOf("\\") + 1)).replace("\\Subset\\", "\\Dataset\\");
        try {
            File file = new File(path);
            in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("file")) {
                    String p = racine + line.substring((line.indexOf("\"") + 1), (line.indexOf(".") + 1)) + "dat";
                    File f = new File(p);
                    if (f.exists()) graphes.add(p);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //on ferme le flux de lecture
            try {
                if (in != null) in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return graphes;
    }

    /** Lis un fichier Subset et retourne le nombre de graphes associés
     * @param path chemin du fichier Subset
     * @return nombre de graphes du Subset
     */
    public static int nbGraphesSubset(String path) {
        int nb = 0;
        BufferedReader in = null;
        List<String> graphes = new ArrayList<>();
        try {
            File file = new File(path);
            in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("file")) {
                    nb++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //on ferme le flux de lecture
            try {
                if (in != null) in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return nb;
    }

    /**
     * Supprime une ligne dans un fichier
     * @param path chemin du fichier
     * @param line ligne à supprimer
     */
    public static void supprimerLigne(String path, String line){
        try {
            String s = new String(Files.readAllBytes(Paths.get(path)));
            s = s.replace(line+"\r\n","");
            Files.write(Paths.get(path),s.getBytes(),TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parcours un fichier et retourne vrai s'il contient la chaine recherchée, sinon faux
     * @param path chemin du fichier à parcourir
     * @param chaine chaine à chercher dans le fichier
     * @return vrai s'il contient la chaine recherchée, sinon faux
     */
    public static boolean FichierContient(String path, String chaine) {
        try {
            File file = new File(path);
            BufferedReader in = new BufferedReader(new FileReader(file));
            String c;
            while((c = in.readLine()) != null){
                if(c.contains(chaine)) return true;
            }
           return false;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return false;
    }
}
