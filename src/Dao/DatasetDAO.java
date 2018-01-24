package Dao;

import Model.Dataset;
import Tools.DatabaseConnexion;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;


/**
 * Permet la gestion des objets Dataset dans la base de données
 * Hérite de la classe DAO
 */
public class DatasetDAO extends DAO<Dataset> {

    /**
     *récupère un objet Dataset de la base de données suivant son identifiant
     * @param id identifiant du Dataset à récupérer
     * @return objet Dataset correpondant à l'identifiant id
     */
    @Override
    public Dataset get(long id) {
        Session session = DatabaseConnexion.getSession();
        Dataset dataset = session.load(Dataset.class, id);
        session.close();
        return dataset;
    }

    /**
     * récupère la liste des objets Dataset visibles dans la base de données
     * @return liste des objets Dataset visibles
     */
    @Override
    public List<Dataset> getAll() {
        Session session = DatabaseConnexion.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Dataset> query = cb.createQuery(Dataset.class);
        Root<Dataset> dataset = query.from(Dataset.class);
        query.select(dataset);
        query.where(cb.equal(dataset.get("visible"), true));
        List<Dataset> datasets = session.createQuery(query).getResultList();
        session.close();
        return datasets;
    }
}
