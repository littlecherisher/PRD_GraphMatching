package Dao;

import Tools.DatabaseConnexion;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Classe abstraite permetant la gestion des objets dans la base de données (ajout, modification, suppression)
 * @param <T> Type de l'objet
 */
public abstract class DAO<T> {
    /**
     * Sauvegarde l'objet t dans la base de données
     * @param t objet à sauvegarder
     */
    public void save(T t) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = DatabaseConnexion.getSession();
            transaction = session.beginTransaction();
            session.persist(t);
            session.save(t);
            session.flush();
            session.evict(t);
            transaction.commit();
            session.close();
        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    /**
     * Supprime l'objet t de la base de données
     * @param t objet à supprimer
     */
    public void remove(T t) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = DatabaseConnexion.getSession();
            transaction = session.getTransaction();
            transaction.begin();
            session.delete(t);
            session.flush();
            session.evict(t);
            transaction.commit();
            session.close();
        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    /**
     * Met à jour l'objet dans la base de données
     * @param t objet à mettre à jour
     */
    public void update(T t) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = DatabaseConnexion.getSession();
            transaction = session.getTransaction();
            transaction.begin();
            session.merge(t);
            session.flush();
            session.evict(t);
            transaction.commit();
            session.close();
        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    /**
     * Methode abstraite (redéfinie dans les classes filles)
     * permettant de récupérer un objet de la base de données suivant son identifiant
     * @param id identifiant de l'objet à récupérer
     * @return objet de la classe T
     */
    abstract public T get(long id);

    /**
     * Methode abstraite (redéfinie dans les classes filles)
     * permettant de récupérer la liste des objets de la classe T de la base de données
     * @return liste des objets de la classe T
     */
    abstract public List<T> getAll();
}

