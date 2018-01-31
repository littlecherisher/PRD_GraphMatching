package Model;

import Tools.Fichier;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *Classe correspondant à un objet Methode
 *Un Methode correspond à un exécutable que l'on souhaite tester
 */
@Entity
@Table
public class Methode{

    /**
     * identifiant de la Methode
     * clé primaire auto générée
     */
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;
    /**
     * nom de la méthode
     */
    private String nom;

    /**
     * description de la méthode
     */
    private String description;

    /**
     * chemin de l'exécutable de la méthode
     */
    private String executable;
    
    /**
     * méthode visible ou non
     */
    private Boolean visible = true;
    
    /**
     * Liste des paramètres de la Méthode sous forme nom_type
     */
    @ElementCollection
    private List<String> paramsHeuristique;
    
    /*
    private List<String> paramsHeuristiqueType;*/
    
    /**
     * Liste des paramètres de la Méthode
     */
    @ElementCollection
    private Map<String, String> paramsHeuris;

    /**
     * Constructeur par défaut de la classe Methode
     */
    public Methode() {
    }

    /**
     * Constructeur de la classe Methode
     *
     * @param nom nom de la méthode
     * @param description description de la méthode
     */
    public Methode(String nom, String description) {
        this.nom = nom;
        this.description = description;
        paramsHeuristique = new ArrayList<>();
        paramsHeuris = new HashMap<>();
    }

    /**
     * Getter : retourne l'identifiant de la méthode
     * @return identifiant de la méthode
     */
    public long getId() {
        return id;
    }

    /**
     * Getter : retourne le nom de la méthode
     * @return nom de la méthode
     */
    public String getNom() {
        return nom;
    }

    /**
     * Setter : modifie le nom de la méthode
     * @param nom nouveau nom de la méthode
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Getter : retourne la description de la méthode
     * @return description de la méthode
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter : modifie la description de la méthode
     * @param description nouvelle description de la méthode
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter : retourne le chemin de l'exécutable de la méthode
     * @return chemin de l'exécutable de la méthode
     */
    public String getExecutable() {
        return executable;
    }

    /**
     * Setter : modifie l'exécutable de la méthode
     * appel à la méthode copieExecutable pour la copie du fichier
     * @param executable InputStream correspondant à l'exécutable
     * @param name nom de l'exécutable
     */
    public void setExecutable(InputStream executable, String name) {
        this.executable = copieExecutable(executable, name);

    }

    /**
     * Getter : retourne vrai si la méthola méthodede est visible par l'utilisateur, sinon faux
     * @return vrai si de la méthode est visible par l'utilisateur, sinon faux
     */
    public Boolean getVisible() {
        return visible;
    }

    /**
     * Setter : rend la méthode non visible pour l'utilisateur
     */
    public void setVisible() {
        this.visible = false;
    }
    
    /**
     * Vide les listes des paramètres heuristiques
     */
    public void clearListsHeuristique() {
    	paramsHeuristique.clear();
    }
    
    /**
     * Vide les listes HashMap des paramètres heuristiques
     */
    public void clearListsHeuris() {
    	paramsHeuris.clear();
    }
    
    /**
     * Getter : retourne la liste des paramètres heuristiques associés à la méthode
     * @return liste des paramètres heuristiques associés à la méthode
     */
    public List<String> getParamsHeuristique () {
        return paramsHeuristique;
    }
    
    
    /**
     * Ajoute un paramètre heuristique à la liste sous forme nom_type
     * @param nom nom du paramètre heuristique
     * @param type type du paramètre heuristique
     */
    public void addParamHeuristique(String nom, String type) {
        if (!nom.isEmpty() && !type.isEmpty()) 
        	paramsHeuristique.add(nom + "--type--" + type);
    }
    
    /**
     * Ajoute un paramètre heuristique à la liste
     * @param nom nom du paramètre heuristique
     * @param type type du paramètre heuristique
     */
    /*public void addParamHeuristiqueType(String type) {
        if (!type.isEmpty()) 
        	paramsHeuristiqueType.add(type);
    }*/
    
    /**
     * Ajoute un paramètre heuristique à la liste sous forme nom(type)
     * @param nom nom du paramètre heuristique
     * @param type type du paramètre heuristique
     */
    public void addParamHeuris(String nom, String type) {
        if (!nom.isEmpty() && !type.isEmpty()) 
        	paramsHeuris.put(nom, type);
    }
    
    /**
     * Renvoie les paramètres heuristiques sous la forme d'un HashMap
     * @return paramètres heuristiques sous la forme d'un HashMap
     */
    public HashMap<String, String> getParametresHeuristique() {
        HashMap<String, String> parametresHeuristique = new HashMap<>();
        for (String s : paramsHeuristique) {
            String[] split = s.split("--type--");
            if(split.length > 1) {
            	//split[1].split(")");
            	parametresHeuristique.put(split[0], split[1]);
            }
        }
        return parametresHeuristique;
    }

    /**
     * copie l'exécutable et cplex1260.dll dans le répertoire de l'application
     * appel à la méthode Fichier.copie() pour copier l'exécutable
     * @param executable chemin de l'exe avant la copie
     * @return chemin de l'exe après la copie
     */
    private String copieExecutable(InputStream executable, String name) {
        Path destination = Paths.get("ProjetPRD\\Methodes", Long.toString(id));
        //Suppression de l'exécutable existant lors d'une modification
        if(this.executable != null){
            new File(this.executable).delete();
        }
        Fichier.copie(executable, destination, name);
        try {
            //copie de la dll cplex1260.dll dans le répertoire si elle n'est pas présente
        	if(!new File(destination.toString()+"\\cplex1270.dll").exists())
                //System.out.println(Paths.get(destination.toString()+"\\..\\..\\lib","cplex1270.dll"));
                Files.copy(Paths.get(destination.toString()+"\\..\\..\\lib","cplex1270.dll"),Paths.get(destination.toString(),"cplex1270.dll"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destination + "\\" + name;
    }

    /**
     * Redéfinition de la méthode de comparaison de deux objets Methode
     * en se basant sur leur identifiant unique, sinon faux
     * @param obj objet Methode à comparer
     * @return vrai si les Methode sont identiques
     */
    @Override
    public boolean equals(Object obj) {
        return (id == ((Methode)obj).getId());
    }
}