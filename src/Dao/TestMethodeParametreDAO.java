package Dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import Model.TestMethodeParametre;
import Tools.DatabaseConnexion;

/**
 * Permet la gestion des objets TestMethodeParametre dans la base de données
 * Hérite de la classe DAO
 */
public class TestMethodeParametreDAO extends DAO<TestMethodeParametre>{
	/**
     * récupère un objet TestMethodeParametre de la base de données suivant son identifiant
     * @param id identifiant du TestMethodeParametre à récupérer
     * @return objet TestMethodeParametre correpondant à l'identifiant id
     */
    @Override
    public TestMethodeParametre get(long id) {
        Session session = DatabaseConnexion.getSession();
        TestMethodeParametre tmp = session.load(TestMethodeParametre.class, id);
        session.close();
        return tmp;
    }

    /**
     * récupère la liste des objets TestMethodeParametre visibles dans la base de données
     * @return liste des objets TestMethodeParametre
     */
    @Override
    public List<TestMethodeParametre> getAll() {
        Session session = DatabaseConnexion.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<TestMethodeParametre> query = cb.createQuery(TestMethodeParametre.class);
        Root<TestMethodeParametre> testmethodeparametre = query.from(TestMethodeParametre.class);
        query.select(testmethodeparametre);
        //query.where(cb.equal(testmethode.get("visible"), true));
        List<TestMethodeParametre> tmp = session.createQuery(query).getResultList();
        session.close();
        return tmp;
    }

}
