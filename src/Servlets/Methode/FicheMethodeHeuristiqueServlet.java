package Servlets.Methode;

import Dao.MethodeDAO;
import Dao.ParametreDAO;
import Dao.TestDAO;
import Dao.TestMethodeDAO;
import Dao.TestMethodeParametreDAO;
import Model.Methode;
import Model.Parametre;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Servlet FicheMethode gérant la création, la modification et l'affichage d'une Methode
 */
@WebServlet("/FicheMethodeHeuristique")
@MultipartConfig
public class FicheMethodeHeuristiqueServlet extends HttpServlet {
	
    TestMethodeDAO testMethodeDao = new TestMethodeDAO();

    /**
     * Retourne le nom du fichier uploadé par l'utilisateur
     * @param part correspond au fichier uploadé
     * @return nom du fichier uploadé
     */
    private static String getNomFichier(Part part) {
        for (String contentDisposition : part.getHeader("content-disposition").split(";")) {
            if (contentDisposition.trim().startsWith("filename")) {
                String nameFile = contentDisposition.substring(contentDisposition.indexOf('=') + 2);
                return nameFile.substring(0, nameFile.length() - 1);
            }
        }
        return null;
    }

    /**
     * Affiche la fiche d'un Methode ou une fiche vide pour une création
     * renvoie sur ficheMethode.jsp
     * @param req requete contenant éventuellement l'identifiant de la Methode à afficher (dataset)
     * @param resp reponse
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF8");
        resp.setCharacterEncoding("UTF8");
        Object object = req.getParameter("methode");
        if (object != null) {
        	MethodeDAO methodeDao = new MethodeDAO();
        	Methode methode = methodeDao.get(Long.parseLong(String.valueOf(object)));
            if (methode != null) {
                req.setAttribute("methode", methode);
            }
        }
        req.setAttribute("testmethodes", testMethodeDao.getAll()); //affichage de la liste des testmethodes
        this.getServletContext().getRequestDispatcher("/ficheMethodeHeuristique.jsp").forward(req, resp);
    }

    /**
     * Créé ou modifie un objet Methode
     * renvoie sur ficheMethode.jsp
     * @param req requete contenant les informations saisies dans le formulaire
     * @param resp reponse
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF8");
        resp.setCharacterEncoding("UTF8");
        //récupération des informations de la méthode 
        String id = req.getParameter("methode");
        String nom = req.getParameter("nom");
        String description = req.getParameter("description");
        Part part = req.getPart("file");
        String fichier = getNomFichier(part);
        InputStream file = part.getInputStream();
        //récupération des paramètres heuristiques
        String[] NomsParamHeu = req.getParameterValues("nomsParamHeu[]");
        String[] TypesParamHeu = req.getParameterValues("typesParamHeu[]");
        
        Methode methode;
        Parametre parametre;
        MethodeDAO methodeDao = new MethodeDAO();
        ParametreDAO parametreDao = new ParametreDAO();
        
        if (!id.isEmpty()) {
            //modification de la méthode
        	methode = methodeDao.get(Long.parseLong(id));
        	methode.clearListsHeuristique();
        	methode.clearListsHeuris();
        	//gestion des paramètres heuristiques
            if (NomsParamHeu != null) for (int i = 0; i < NomsParamHeu.length; i++) {
                methode.addParamHeuristique(NomsParamHeu[i], TypesParamHeu[i]);
            }
            
            if (NomsParamHeu != null) for (int i = 0; i < NomsParamHeu.length; i++) {
                methode.addParamHeuris(NomsParamHeu[i], TypesParamHeu[i]);
            }
            
            if (!nom.equals(methode.getNom())) methode.setNom(nom);
            if (!description.equals(methode.getDescription())) methode.setDescription(description);           
            methode.setType("heuristique");
            //supprimer la liste selon id de méthode et puis ajouter nouveaux paramètres
            List<Parametre> list = parametreDao.getAll();
        	for(int k = 0; k < list.size(); k++){
        		if(list.get(k).getMethode().getId() == methode.getId())
                    parametreDao.remove(list.get(k));
        	}
            //gestion des paramètres heuristiques dans le tableau Parametre           
            if (NomsParamHeu != null) for (int i = 0; i < NomsParamHeu.length; i++) {
            	parametre = new Parametre(NomsParamHeu[i], methode, TypesParamHeu[i]); 
                parametreDao.save(parametre);            
            }
            
            if (!fichier.isEmpty()) {
                try {
                	methode.setExecutable(file, fichier);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            methodeDao.update(methode);
        } else {
            //création de la méthode
        	methode = new Methode(nom, description);
        	methodeDao.save(methode);
        	//gestion des paramètres heuristiques
            if (NomsParamHeu != null) for (int i = 0; i < NomsParamHeu.length; i++) {
                methode.addParamHeuristique(NomsParamHeu[i], TypesParamHeu[i]);
            }
            if (NomsParamHeu != null) for (int i = 0; i < NomsParamHeu.length; i++) {
                methode.addParamHeuris(NomsParamHeu[i], TypesParamHeu[i]);
            }           
            if (NomsParamHeu != null) for (int i = 0; i < NomsParamHeu.length; i++) {
            	parametre = new Parametre(NomsParamHeu[i], methode, TypesParamHeu[i]);
                parametreDao.save(parametre);
            }
            
            methode.setType("heuristique");
            if (!fichier.isEmpty()) {
                try {
                	methode.setExecutable(file, fichier);
                	methodeDao.update(methode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //réaffichage de la fiche de la méthode ajoutée ou modifiée
        req.setAttribute("methode", methode);
        req.setAttribute("testmethodes", testMethodeDao.getAll()); //affichage de la liste des testmethodes
        this.getServletContext().getRequestDispatcher("/ficheMethodeHeuristique.jsp").forward(req, resp);
    }
}
