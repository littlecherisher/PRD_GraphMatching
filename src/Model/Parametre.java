package Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Classe correpondant à un objet Parametre
 * Un Parametre correspond à une méthode heuristique
 */
@Entity
@Table
public class Parametre {
	/**
     * Identifiant du Subset
     * clé primaire auto générée
     */
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;
    
    /**
     * Nom du paramètre
     */
    private String nom;
    /**
     * Objet Methode au quel appartient le paramètre
     */
    @ManyToOne
    private Methode methode;

    /**
     * Le type du paramètre (int, string ou float)
     */
    private String type;
    
    /**
     * Constructeur par défaut de la classe Parametre
     */
    public Parametre() {
    }

    /**
     * Constructeur de la classe Parametre
     * @param n Le nom du Parametre
     * @param m Methode auquel appartient la Methode
     * @param t Le type du paramètre
     */
    public Parametre(String n, Methode m, String t) {
        nom = n;
        methode = m;
        type = t;
    }

    /**
     * Getter : retourne l'identifiant du Parametre
     * @return identifiant du Parametre
     */
    public long getId() {
        return id;
    }

    /**
     * Getter : retourne le nom du Parametre
     * @return nom du Parametre
     */
    public String getNom() {
        return nom;
    }
    
    /**
     * Setter : modifie le nom du paramètre
     * @param nom nouveau nom du paramètre
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Getter : retourne l'objet Methode auquel est associé le Parametre
     * @return objet Methode auquel est associé le Parametre
     */
    public Methode getMethode() {
        return methode;
    }
    
    /**
     * Setter : modifie le nom du paramètre
     * @param nom nouveau nom du paramètre
     */
    public void setMethode(Methode methode) {
        this.methode = methode;
    }

    /**
     * Getter : retourne le type du paramètre
     * @return le type du paramètre
     */
    public String getType() {
        return type;
    }
    
    /**
     * Setter : modifie le type du paramètre
     * @param type nouveau type du paramètre
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Redéfinition du paramètre de comparaison de deux objets Parametre
     * en se basant sur leur identifiant unique, sinon faux
     * @param obj objet Parametre à comparer
     * @return vrai si les Parametre sont identiques
     */
    @Override
    public boolean equals(Object obj) {
        return (id == ((Parametre)obj).getId());
    }
}
