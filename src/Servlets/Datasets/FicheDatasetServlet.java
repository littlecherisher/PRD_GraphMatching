package Servlets.Datasets;

import Dao.DatasetDAO;
import Model.Dataset;

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
 * Servlet FicheDataset gérant la création, la modification et l'affichage d'un Dataset
 */
@WebServlet("/FicheDataset")
@MultipartConfig
public class FicheDatasetServlet extends HttpServlet {

    /**
     * Retourne le nom du fichier uploadé par l'utilisateur
     * @param part correspond au fichier uploadé
     * @return nom du fichier uploadé
     */
    private static String getNomFichier(Part part) {
        for (String contentDisposition : part.getHeader("content-disposition").split(";")) {
            if (contentDisposition.trim().startsWith("filename")) {
                //extraction du nom du fichier
                String nameFile = contentDisposition.substring(contentDisposition.indexOf('=') + 2);
                return nameFile.substring(0, nameFile.length() - 1);
            }
        }
        return null;
    }

    /**
     * Affiche la fiche d'un Dataset ou une fiche vide pour une création
     * renvoie sur ficheDataset.jsp
     * @param req requete contenant éventuellement l'identifiant du Dataset à afficher (dataset)
     * @param resp reponse
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF8");
        resp.setCharacterEncoding("UTF8");
        Object object = req.getParameter("dataset");
        if (object != null) {
            DatasetDAO datasetDAO = new DatasetDAO();
            Dataset dataset = datasetDAO.get(Long.parseLong(String.valueOf(object)));
            if (dataset != null) {
                req.setAttribute("dataset", dataset);
            }
        }
        this.getServletContext().getRequestDispatcher("/ficheDataset.jsp").forward(req, resp);
    }

    /**
     * Créé ou modifie un objet Dataset
     * renvoie sur ficheDataset.jsp
     * @param req requete contenant les informations saisies dans le formulaire
     * @param resp reponse
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF8");
        resp.setCharacterEncoding("UTF8");
        //récupération des paramètres
        String id = req.getParameter("dataset");
        String nom = req.getParameter("nom");
        String description = req.getParameter("description");
        //récupération du fichier dataset
        Part partDataset = req.getPart("fileDataset");
        String fichierDataset = getNomFichier(partDataset);
        InputStream fileDataset = partDataset.getInputStream();
        //récupération du fichier subset
        Part partSubset = req.getPart("fileSubset");
        String fichierSubset = getNomFichier(partSubset);
        InputStream fileSubset = partSubset.getInputStream();
        Dataset dataset = null;
        DatasetDAO datasetDAO = new DatasetDAO();
        if (!id.isEmpty()) {
            //modification du dataset
            dataset = datasetDAO.get(Long.parseLong(id));
            if (!nom.equals(dataset.getNom())) dataset.setNom(nom);
            if (!description.equals(dataset.getDescription())) dataset.setDescription(description);
            try {
                if (!fichierDataset.isEmpty()) dataset.setDataset(fileDataset, fichierDataset);
                if (!fichierSubset.isEmpty()) dataset.setSubset(fileSubset, fichierSubset);
            } catch (Exception e) {
                e.printStackTrace();
            }
            datasetDAO.update(dataset);
        } else {
            //création du dataset
            dataset = new Dataset(nom, description);
            datasetDAO.save(dataset);
            try {
                if (!fichierDataset.isEmpty()) dataset.setDataset(fileDataset, fichierDataset);
                if (!fichierSubset.isEmpty()) dataset.setSubset(fileSubset, fichierSubset);
                datasetDAO.update(dataset);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //réaffichage de la fiche du dataset ajouté ou modifié
        req.setAttribute("dataset", dataset);
        this.getServletContext().getRequestDispatcher("/ficheDataset.jsp").forward(req, resp);
    }
}
