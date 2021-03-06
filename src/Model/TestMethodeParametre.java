package Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Classe correpondant à un objet TestMethodeParametre
 * Un TestMethodeParametre correspond à un objet TestMethode et un objet Parametre et une liste de "Valeur" 
 */
@Entity
@Table
public class TestMethodeParametre {
	/**
     * identifiant du TestMethodeParametre
     * clé primaire auto générée
     */
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;
    
    /**
     * objet TestMethode associé au TestMethodeParametre
     */
    @ManyToOne
    private TestMethode testMethode;
    
    /**
     * objet Parametre associé au TestMethodeParametre
     */
    @ManyToOne
    private Parametre parametre;
    
    /**
     * valeur de paramètre pour une méthode dans un test
     */
    private String valeur;
    
    /**
     * Constructeur de la classe TestMethodeParametre
     * @param tm l'objet TestMethode du TestMethodeParametre
     * @param p l'objet Parametre du TestMethodeParametre
     * @param v la valeur du TestMethodeParametre
     */
    public TestMethodeParametre(TestMethode tm, Parametre p, String v) {
    	this.testMethode = tm;
    	this.parametre = p;
    	this.valeur = v;
    }

    /**
     * Constructeur par défaut de la classe TestMethodeParametre
     */
    public TestMethodeParametre() {
    }
    
    /**
     * Getter : retourne l'identifiant du TestMethodeParametre
     * @return identifiant du TestMethodeParametre
     */
    public long getId() {
        return id;
    }
    
    /**
     * Getter : retourne la valeur du TestMethodeParametre
     * @return valeur du TestMethodeParametre
     */
    public String getValeur() {
        return valeur;
    }
    
    /**
     * Setter : modifie la valeur du paramètre
     * @param valeur nouvelle valeur du paramètre
     */
    public void setValeur(String valeur) {
        this.valeur = valeur;
    }
    
    /**
     * Getter : retourne l'objet TestMethode associé au TestMethodeParametre
     * @return objet TestTestMethode associé au TestMethodeParametre
     */
    public TestMethode getTestMethode() {
        return testMethode;
    }
    
    /**
     * Setter : modifie le testMethode du TestMethodeParametre
     * @param testMethode nouveau testMethode du TestMethodeParametre
     */
    public void setTestMethode(TestMethode testMethode) {
        this.testMethode = testMethode;
    }
    
    /**
     * Getter : retourne l'objet Parametre associé au TestMethodeParametre
     * @return objet Parametre associé au TestMethodeParametre
     */
    public Parametre getParametre() {
        return parametre;
    }
    
    /**
     * Setter : modifie le paramètre du TestMethodeParametre
     * @param parametre nouveau parametre du TestMethodeParametre
     */
    public void setParametre(Parametre parametre) {
        this.parametre = parametre;
    }
}
