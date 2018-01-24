package Servlets.Test;

import Dao.DatasetDAO;
import Dao.MethodeDAO;
import Dao.TestDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servet ListeTest qui affiche la liste des Test visibles par l'utilisateur
 */
@WebServlet("/ListeTest")
public class ListeTestServlet extends HttpServlet {

    /**
     * Affiche la liste des Test visibles
     * renvoie sur listeTests.jsp
     * @param req requete
     * @param resp reponse
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF8");
        resp.setCharacterEncoding("UTF8");
        TestDAO testDAO = new TestDAO();
        MethodeDAO methodeDAO = new MethodeDAO();
        DatasetDAO datasetDAO = new DatasetDAO();
        //affichage de la liste des tests
        req.setAttribute("tests", testDAO.getAll());
        req.setAttribute("methodes", methodeDAO.getAll());
        req.setAttribute("datasets", datasetDAO.getAll());

        this.getServletContext().getRequestDispatcher("/listeTests.jsp").forward(req, resp);
    }
}
