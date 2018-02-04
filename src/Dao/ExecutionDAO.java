package Dao;

import Model.Execution;
import Tools.DatabaseConnexion;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Permet la gestion des objets Execution dans la base de données
 * Hérite de la classe DAO
 */
public class ExecutionDAO extends DAO<Execution> {

    /**
     * récupère un objet Execution de la base de données suivant son identifiant
     * @param id identifiant du Execution à récupérer
     * @return objet Execution correpondant à l'identifiant id
     */
    @Override
    public Execution get(long id) {
        Session session = DatabaseConnexion.getSession();
        Execution e = session.load(Execution.class, id);
        session.close();
        return e;
    }

    /**
     * récupère la liste des objets Execution visibles dans la base de données
     * @return liste des objets Execution visibles
     */
    @Override
    public List<Execution> getAll() {
        Session session = DatabaseConnexion.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Execution> query = cb.createQuery(Execution.class);
        Root<Execution> execution = query.from(Execution.class);
        query.select(execution);
        query.where(cb.equal(execution.get("visible"), true));
        List<Execution> e = session.createQuery(query).getResultList();
        session.close();
        return e;
    }
}
