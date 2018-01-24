package Dao;

import Model.Methode;
import Tools.DatabaseConnexion;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
/**
 * Permet la gestion des objets Methode dans la base de données
 * Hérite de la classe DAO
 */
public class MethodeDAO extends DAO<Methode> {

    /**
     *récupère un objet Methode de la base de données suivant son identifiant
     * @param id identifiant du Methode à récupérer
     * @return objet Methode correpondant à l'identifiant id
     */
    @Override
    public Methode get(long id) {
        Session session = DatabaseConnexion.getSession();
        Methode m = session.load(Methode.class, id);
        session.close();
        return m;
    }

    /**
     * récupère la liste des objets Methode visibles dans la base de données
     * @return liste des objets Methode visibles
     */
    @Override
    public List<Methode> getAll() {
        Session session = DatabaseConnexion.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Methode> query = cb.createQuery(Methode.class);
        Root<Methode> methode = query.from(Methode.class);
        query.select(methode);
        query.where(cb.equal(methode.get("visible"), true));
        List<Methode> m = session.createQuery(query).getResultList();
        session.close();
        return m;
    }
}
