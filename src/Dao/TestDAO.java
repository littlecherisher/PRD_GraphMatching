package Dao;

import Model.Execution;
import Model.Test;
import Tools.DatabaseConnexion;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Permet la gestion des objets Test dans la base de données
 * Hérite de la classe DAO
 */
public class TestDAO extends DAO<Test> {

    /**
     *récupère un objet Test de la base de données suivant son identifiant
     * @param id identifiant du Test à récupérer
     * @return objet Test correpondant à l'identifiant id
     */
    @Override
    public Test get(long id) {
        Session session = DatabaseConnexion.getSession();
        Test t = session.load(Test.class, id);
        session.close();
        return t;
    }

    /**
     * récupère la liste des objets Test visibles dans la base de données
     * @return liste des objets Test visibles
     */
    @Override
    public List<Test> getAll() {
        Session session = DatabaseConnexion.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Test> query = cb.createQuery(Test.class);
        Root<Test> test = query.from(Test.class);
        query.select(test);
        query.where(cb.equal(test.get("visible"), true));
        List<Test> t = session.createQuery(query).getResultList();
        session.close();
        return t;
    }


    /**
     * Retourne le nombre d'objets Execution stockés dans la base de données
     * associés au Test correspondant à l'identifiant fourni en paramètres
     * @param id identifiant du Test
     * @return nombre d'Execution associés au Test correpondant à l'identifiant id
     */
    public Long getNbExecutions(long id) {
        Session session = DatabaseConnexion.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Execution> executionRoot = query.from(Execution.class);
        Join<Test, Execution> exec = executionRoot.join("test");
        query.select(cb.count(exec));
        query.where(cb.equal(exec.get("id"), id));
        Long nb = session.createQuery(query).getSingleResult();
        session.close();
        return nb;
    }
}
