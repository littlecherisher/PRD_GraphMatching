package Tools;

import org.h2.tools.Server;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Classe qui gère le lancement du serveur de base de données
 * et la connexion à la base de données
 */
public class DatabaseConnexion {
    /**
     * SessionFactory contient les paramètres de configuration de la base de données
     * permet d'ouvrir une Session
     */
    private static SessionFactory sessionFactory = null;
    /**
     * Server différent de null si le serveur de base de données est démarré
     */
    private static Server server = null;

    /**
     * Lance le serveur de base de données si besoin et retourne un objet session
     * @return objet Session ouverte
     */
    public static Session getSession(){
        if(server == null){
            try {
                server = Server.createTcpServer("-tcpAllowOthers").start();
                Class.forName("org.h2.Driver");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(sessionFactory == null)
            sessionFactory = new Configuration().configure("./hibernate.cfg.xml").buildSessionFactory();
        return sessionFactory.openSession();
    }
}
