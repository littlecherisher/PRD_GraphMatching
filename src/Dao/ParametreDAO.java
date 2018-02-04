package Dao;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import Model.Parametre;
import Tools.DatabaseConnexion;

/**
 * Permet la gestion des objets Parametre dans la base de données
 * Hérite de la classe DAO
 */
public class ParametreDAO extends DAO<Parametre>{
	/**
     *récupère un objet Parametre de la base de données suivant son identifiant
     * @param id identifiant du Parametre à récupérer
     * @return objet Parametre correpondant à l'identifiant id
     */
    @Override
    public Parametre get(long id) {
        Session session = DatabaseConnexion.getSession();
        Parametre p = session.load(Parametre.class, id);
        session.close();
        return p;
    }
    
    /**
     * récupère la liste des objets Parametre dans la base de données
     * @return liste des objets Parametre
     */
    @Override
    public List<Parametre> getAll() {
        Session session = DatabaseConnexion.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Parametre> query = cb.createQuery(Parametre.class);
        Root<Parametre> parametre = query.from(Parametre.class);
        query.select(parametre);
        List<Parametre> p = session.createQuery(query).getResultList();
        session.close();
        return p;
    }
}
