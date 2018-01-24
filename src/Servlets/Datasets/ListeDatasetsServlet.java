package Servlets.Datasets;

import Dao.DatasetDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet ListeDataset permettant l'affichage de la liste des Dataset visibles par l'utilisateur
 */
@WebServlet("/ListeDataset")
public class ListeDatasetsServlet extends HttpServlet {

    /**
     * Affiche la liste des Dataset visibles par l'utilisateur
     * renvoie sur listeDatasets.jsp
     * @param req requete
     * @param resp reponse
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF8");
        resp.setCharacterEncoding("UTF8");
        //affichage de la liste des datasets
        DatasetDAO datasetDAO = new DatasetDAO();
        req.setAttribute("datasets", datasetDAO.getAll());
        this.getServletContext().getRequestDispatcher("/listeDatasets.jsp").forward(req, resp);
    }
}
