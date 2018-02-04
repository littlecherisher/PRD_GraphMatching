package Servlets.Methode;

import Dao.MethodeDAO;
import Model.Methode;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;

/**
 * Servlet FicheMethode gérant la création, la modification et l'affichage d'une Methode
 */
@WebServlet("/FicheMethodeExacte")
@MultipartConfig
public class FicheMethodeExacteServlet extends HttpServlet {

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
     * Affiche la fiche d'une Methode ou une fiche vide pour une création
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
        this.getServletContext().getRequestDispatcher("/ficheMethodeExacte.jsp").forward(req, resp);
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
        Methode methode;
        MethodeDAO methodeDao = new MethodeDAO();
        if (!id.isEmpty()) {
            //modification de la méthode
        	methode = methodeDao.get(Long.parseLong(id));
            if (!nom.equals(methode.getNom())) methode.setNom(nom);
            if (!description.equals(methode.getDescription())) methode.setDescription(description);
            methode.setType("exacte");
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
        	methode.setType("exacte");
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
        this.getServletContext().getRequestDispatcher("/ficheMethodeExacte.jsp").forward(req, resp);
    }
}
