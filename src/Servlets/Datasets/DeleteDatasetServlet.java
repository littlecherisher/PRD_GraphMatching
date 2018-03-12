package Servlets.Datasets;

import Dao.DatasetDAO;
import Model.Dataset;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet MasquerDataset gérant le masquage d'un Dataset pour l'utlisateur
 */
@WebServlet("/MasquerDataset")
public class DeleteDatasetServlet extends HttpServlet {
    /**
     * Rend le Dataset non visible et affiche la liste des Datasets visibles
     * Renvoie sur listeDatasets.jsp
     * @param req requete contienant l'identifiant du Dataset à masquer (id)
     * @param resp réponse
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object object = req.getParameter("id");
        DatasetDAO datasetDAO = new DatasetDAO();
        if (object != null) {
            //on passe l'attribut visible à faux
            Dataset dataset = datasetDAO.get(Long.parseLong(String.valueOf(object)));
            dataset.setVisible();
            datasetDAO.update(dataset);
        }
        //on affiche la liste des méthodes
        req.setAttribute("datasets", datasetDAO.getAll());
        this.getServletContext().getRequestDispatcher("/listeDatasets.jsp").forward(req, resp);
    }
}
