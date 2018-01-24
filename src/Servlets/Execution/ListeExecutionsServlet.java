package Servlets.Execution;

import Dao.ExecutionDAO;
import Dao.TestDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet ListeExecution qui affiche la liste des Execution visibles par l'utilisateur
 */
@WebServlet("/ListeExecution")
public class ListeExecutionsServlet extends HttpServlet {

    /**
     * Affiche la liste des Execution visibles par l'utilisateur
     * renvoie sur listeExecutions.jsp
     * @param req requete
     * @param resp reponse
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF8");
        resp.setCharacterEncoding("UTF8");
        //affichage de la liste des executions
        ExecutionDAO executionDAO = new ExecutionDAO();
        req.setAttribute("executions", executionDAO.getAll());
        TestDAO testDAO = new TestDAO();
        req.setAttribute("tests", testDAO.getAll());
        this.getServletContext().getRequestDispatcher("/listeExecutions.jsp").forward(req, resp);
    }
}
