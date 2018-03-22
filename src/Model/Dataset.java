package Model;

import Dao.SubsetDAO;
import Tools.Fichier;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe correspondant à un objet Dataset
 * Un dataset correspond à un ensemble de fichiers graphe
 * et éventuellement à des fichiers définissant des sous-ensembles de graphes (Subset)
 */

@Entity
@Table
public class Dataset {

    /**
     * identifiant du Dataset
     * clé primaire auto générée
     */
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;
    /**
     * nom du Dataset
     */
    private String nom;
    /**
     * description du Dataset
     */
    private String description;
    /**
     * chemin vers le dossier contenant les graphes du Dataset
     */
    private String dataset;
    /**
     * chemin vers le dossier contenant les fichiers subset
     */
    private String subset;
    /**
     * graphes dirigés ou non
     */
    private boolean dirige;
    /**
     * Dataset visible ou non pour l'utilisateur
     * vrai par défaut
     */
    private boolean visible = true;
    /**
     * Nombre de fichiers graphe contenus dans le Dataset
     */
    private int nbGraphes;

    /**
     * Liste des Subsets associés au Dataset
     */
    @OneToMany
    private List<Subset> subsets;

    /**
     * Constructeur par défaut de la classe Dataset
     */
    public Dataset(){
    }

    /**
     * Constructeur de la classe Dataset
     * @param nom nom du Dataset
     * @param description description du Dataset
     */
    public Dataset(String nom, String description) {
        this.nom = nom;
        this.description = description;
        visible = true;
        subsets = new ArrayList<>();
    }

    /**
     * Getter : retourne l'identifiant du Dataset
     * @return identifiant du Dataset
     */
    public long getId() {
        return id;
    }

    /**
     * Getter : retourne le nom du Dataset
     * @return nom du Dataset
     */
    public String getNom() {
        return nom;
    }

    /**
     * Setter : modifie le nom du Dataset
     * @param nom nouveau nom du Dataset
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Getter : retourne la description du dataset
     * @return description du dataset
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter : modifie la description du Dataset
     * @param description nouvelle description du Dataset
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter : retourne le chemin du dossier contenant les fichiers graphe du Dataset
     * @return chemin du dossier contenant les fichers graphe du Dataset
     */
    public String getDataset() {
        return dataset;
    }

    /**
     * Setter : modifie le dossier contenant les graphes du Dataset
     * appel à la méthode copieDossier() pour copier les fichiers graphe sur le serveur
     * @param dataset InputStream correspondant au dossier contenant les graphes
     * @param nom nom du dossier contenant les graphes
     */
    public void setDataset(InputStream dataset, String nom) {
        this.dataset = copieDossier(dataset, nom, "Dataset");
    }

    /**
     * Getter : retourne le chemin du dossier contenant les fichiers Subset
     * @return chemin du dossier contenant les fichiers Subset
     */
    public String getSubset() {
        return subset;
    }

    /**
     * Setter : modifie le dossier contenant les fichiers Subset du Dataset
     * appel à la méthode copieDossier() pour copier les fichiers Subset sur le serveur
     * supprime objets Subset existants du Dataset
     * créé les objets Subset associés au Dataset
     * @param subset InputStream contenant le dosier contenant les fichiers Subset
     * @param nom nom du dosier contenant les fichiers Subset
     */
    public void setSubset(InputStream subset, String nom) {
        //copie du dossier des subsets sur le serveur
        this.subset = copieDossier(subset, nom, "Subset");

        //récupération de la liste des fichiers
        List<String> files = Fichier.listeFichiers(this.subset);

        //suppression des objets Subset existants associés au Datasets
        SubsetDAO subsetDAO = new SubsetDAO();
        subsets.clear();
        for (Subset s : this.subsets) {
            subsetDAO.remove(s);
        }

        //création des objets Susbet
        for (String f : files) {
            Subset s = new Subset(f.substring((f.lastIndexOf("\\") + 1), f.indexOf(".")), this, f);
            subsetDAO.save(s);
            subsets.add(s);
        }
    }

    /**
     * Getter : retourne vrai si le Dataset est visible par l'utilisateur, sinon faux
     * @return vrai si le Dataset est visible par l'utilisateur, sinon faux
     */
    public Boolean getVisible() {
        return visible;
    }

    /**
     * Setter : rend le Dataset non visible pour l'utilisateur
     */
    public void setVisible() {
        this.visible = false;
    }

    /**
     * Getter : retourne le nombre de graphes constituant le Dataset
     * @return nombre de graphes constituant le Dataset
     */
    public int getNbGraphes() {
        return nbGraphes;
    }

    /**
     * Setter : met à jour le nombre de graphes constituant le Dataset
     * @param nbGraphes nombre de graphes constituant le Dataset
     */
    private void setNbGraphes(int nbGraphes) {
        this.nbGraphes = nbGraphes;
    }

    /**
     * Getter : retourne vrai si les graphes du Dataset sont dirigé, sinon faux
     * @return vrai si les graphes du Dataset sont dirigé, sinon faux
     */
    public boolean isDirige() {
        return dirige;
    }

    /**
     * Setter : met à jour la variable indicant si les graphes du Dataset sont dirigés ou non
     * @param dirige vrai si les graphes du Dataset sont dirigé, sinon faux
     */
    private void setDirige(boolean dirige) {
        this.dirige = dirige;
    }

    /**
     * Getter : retourne la liste des Subset associés au Dataset
     * @return liste des Subset associés au Dataset
     */
    public List<Subset> getSubsets() {
        return subsets;
    }

    /**
     * Copie les dossiers contenant les graphes et les subsets du Dataset sur le serveur
     * appel à la fonction Fichier.cleanRep pour vider le contenu du répertoire de destination
     * appel à la fonction Fichier.copie pour copier le fichier .zip sur le serveur
     * appel à la fonction Fichier.unzip pour décompresser le fichier .zip sur le serveur
     * appel à la fonction Fichier.gxlVersDat pour convertir les fichiers graphe .gxl en .dat
     * met à jour le nombre de graphe du Dataset et s'ils sont dirigés ou non
     * @param fichier InputStream correspondant au dossier compressé au format .zip
     * @param name nom du dossier
     * @param type "Dataset" si le dossier contient des graphes, "Subset" s'il contient des fichier Subset
     * @return chemin de destination du dossier
     */
    private String copieDossier(InputStream fichier, String name, String type) {
        Path destination = Paths.get("ProjetPRD\\Datasets", Long.toString(id), type);
        //on vide le répertoire de destination
        Fichier.cleanRep(destination.toString());
        //on récupère le fichier sur le serveur
        Fichier.copie(fichier, destination, name);
        //on décompresse le fichier
        Fichier.unzip(destination, name);
        if (type.equals("Dataset")) {
            //on transforme les fichiers gxl en dat et on récupère le nombre de graphes
            int nb = Fichier.gxlVersDat(destination.toString());
            //on met à jour les propriétés
            setNbGraphes(nb);
            setDirige(Fichier.isDirected(destination.toString()));
        }
        return destination.toString();
    }

    /**
     * Redéfinition de la méthode de comparaison de deux objets Dataset
     * en se basant sur leur identifiant unique
     * @param obj objet Dataset à comparer
     * @return vrai si les Dataset sont identiques, sinon faux
     */
    @Override
    public boolean equals(Object obj) {
        return (id == ((Dataset)obj).getId());
    }
}
