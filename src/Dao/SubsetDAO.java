package Dao;

import Model.Subset;
import Tools.DatabaseConnexion;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Permet la gestion des objets Subset dans la base de données
 * Hérite de la classe DAO
 */
public class SubsetDAO extends DAO<Subset> {

    /**
     *récupère un objet Subset de la base de données suivant son identifiant
     * @param id identifiant du Subset à récupérer
     * @return objet Subset correpondant à l'identifiant id
     */
    @Override
    public Subset get(long id) {
        Session session = DatabaseConnexion.getSession();
        Subset s = session.load(Subset.class, id);
        session.close();
        return s;
    }

    /**
     * récupère la liste des objets Subset dans la base de données
     * @return liste des objets Subset
     */
    @Override
    public List<Subset> getAll() {
        Session session = DatabaseConnexion.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Subset> query = cb.createQuery(Subset.class);
        Root<Subset> subset = query.from(Subset.class);
        query.select(subset);
        List<Subset> s = session.createQuery(query).getResultList();
        session.close();
        return s;
    }
}
