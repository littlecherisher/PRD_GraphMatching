package Dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import Model.TestMethode;
import Tools.DatabaseConnexion;

/**
 * Permet la gestion des objets TestMethode dans la base de données
 * Hérite de la classe DAO
 */
public class TestMethodeDAO extends DAO<TestMethode>{
	 /**
     * récupère un objet TestMethode de la base de données suivant son identifiant
     * @param id identifiant du TestMethode à récupérer
     * @return objet TestMethode correpondant à l'identifiant id
     */
    @Override
    public TestMethode get(long id) {
        Session session = DatabaseConnexion.getSession();
        TestMethode tm = session.load(TestMethode.class, id);
        session.close();
        return tm;
    }

    /**
     * récupère la liste des objets TestMethode visibles dans la base de données
     * @return liste des objets TestMethode
     */
    @Override
    public List<TestMethode> getAll() {
        Session session = DatabaseConnexion.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<TestMethode> query = cb.createQuery(TestMethode.class);
        Root<TestMethode> testmethode = query.from(TestMethode.class);
        query.select(testmethode);
        List<TestMethode> tm = session.createQuery(query).getResultList();
        session.close();
        return tm;
    }

}
