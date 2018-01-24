package Servlets.Execution;

import Dao.ExecutionDAO;
import Dao.TestDAO;
import Model.Execution;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet MasquerExecution qui gère le masquage d'une exécution pour l'utilisateur
 */
@WebServlet("/MasquerExecution")
public class DeleteExecutionServlet extends HttpServlet{

    /**
     * Rend l'Execution non visible pour l'utilisateur et affiche la liste des Execution visibles
     * renvoie sur listeExecutions.jsp
     * @param req requete contenant l'identifiant de l'Execution à masquer
     * @param resp reponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object object = req.getParameter("id");
        ExecutionDAO executionDAO = new ExecutionDAO();
        if (object != null) {

            //on passe l'attribut visible à faux
            Execution execution = executionDAO.get(Long.parseLong(String.valueOf(object)));
            execution.setVisible();
            executionDAO.update(execution);
        }
        //affichage de la liste des executions
        req.setAttribute("executions", executionDAO.getAll());
        TestDAO testDAO = new TestDAO();
        req.setAttribute("tests", testDAO.getAll());
        this.getServletContext().getRequestDispatcher("/listeExecutions.jsp").forward(req, resp);
    }
}
