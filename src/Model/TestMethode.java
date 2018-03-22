package Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Classe correpondant à un objet TestMethode
 * Un TestMethode correspond à une seule méthode et un seul test 
 */
@Entity
@Table
public class TestMethode {
	/**
     * identifiant du TestMethode
     * clé primaire auto générée
     */
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;
    
    /**
     * objet Test associé au TestMethode
     */
    @ManyToOne
    private Test test;
    
    /**
     * objet Methode associé au TestMethode
     */
    @ManyToOne
    private Methode methode;
    
    /**
     * Constructeur de la classe TestMethode
     * @param t l'objet Test du TestMethode
     * @param m l'objet Methode du TestMethode
     */
    public TestMethode(Test t, Methode m) {
    	this.test = t;
    	this.methode = m;
    }

    /**
     * Constructeur par défaut de la classe TestMethode
     */
    public TestMethode() {
    }
    
    /**
     * Getter : retourne l'identifiant du TestMethode
     * @return identifiant du TestMethode
     */
    public long getId() {
        return id;
    }
    
    /**
     * Getter : retourne l'objet Test associé au TestMethode
     * @return objet Test associé au TestMethode
     */
    public Test getTest() {
        return test;
    }
    
    /**
     * Setter : modifie le test du TestMethode
     * @param test nouveau test du TestMethode
     */
    public void setTest(Test test) {
        this.test = test;
    }
    
    /**
     * Getter : retourne l'objet Methode associée au TestMethode
     * @return objet Methode associée au TestMethode
     */
    public Methode getMethode() {
        return methode;
    }
    
    /**
     * Setter : modifie la méthode du TestMethode
     * @param methode nouvelle methode du TestMethode
     */
    public void setMethode(Methode methode) {
        this.methode = methode;
    }
}
