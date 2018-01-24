package Model;

import Tools.Fichier;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Classe correpondant à un objet Subset
 * Un Subset correspond à un fichier qui contient une liste de graphes
 */
@Entity
@Table
public class Subset {
    /**
     * Identifiant du Subset
     * clé primaire auto générée
     */
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;
    /**
     * Nom du Subset
     */
    private String nom;
    /**
     * Objet Dataset au quel appartient le Subset
     */
    @ManyToOne
    private Dataset dataset;

    /**
     * Chemin du fichier correspondant au Subset
     */
    private String chemin;

    /**
     * Constructeur par défaut de la classe Subset
     */
    public Subset() {
    }

    /**
     * Constructeur de la classe Subset
     * @param n nom du Subset
     * @param d Dataset auquel appartient le Subset
     * @param c chemin du fichier correspondant au Subset
     */
    public Subset(String n, Dataset d, String c) {
        nom = n;
        dataset = d;
        chemin = c;
    }

    /**
     * Getter : retourne l'identifiant du Subset
     * @return identifiant du Subset
     */
    public long getId() {
        return id;
    }

    /**
     * Getter : retourne le nom du Subset
     * @return nom du Subset
     */
    public String getNom() {
        return nom;
    }

    /**
     * Getter : retourne l'objet Dataset auquel est associé le Subset
     * @return objet Dataset auquel est associé le Subset
     */
    public Dataset getDataset() {
        return dataset;
    }

    /**
     * Getter : retourne le chemin du fichier correspondant au Subset
     * @return chemin du fichier correspondant au Subset
     */
    public String getChemin() {
        return chemin;
    }

    /**
     * Redéfinition de la méthode de comparaison de deux objets Subset
     * en se basant sur leur identifiant unique
     * @param obj objet Subset à comparer
     * @return vrai si les deux objets Subset sont identiques, sinon faux
     */
    @Override
    public boolean equals(Object obj) {
        return (id == ((Subset)obj).getId());
    }

    /**
     * retourne le nombre de graphes du Subset
     * appel à la méthode Fichier.nbGraphesSubset
     * @return nombre de graphes du Subset
     */
    public int getNbGraphes() {
        return Fichier.nbGraphesSubset(chemin);
    }
}
