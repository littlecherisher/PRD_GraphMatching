package Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Classe correpondant à un objet Test_Methode
 * Un Test_Methode correspond à une seule méthode et un seul test 
 */
@Entity
@Table
public class TestMethode {
	/**
     * identifiant du Test_Methode
     * clé primaire auto générée
     */
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;
    
    /**
     * objet Test associé au Test_Methode
     */
    @ManyToOne
    private Test test;
    /**
     * objet Methode associé au Test_Methode
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
     * Getter : retourne l'objet Test associé au TestMethode
     * @return objet Test associé au TestMethode
     */
    public Methode getMethode() {
        return methode;
    }
}
