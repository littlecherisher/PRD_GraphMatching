package Servlets.Execution;

import Dao.ExecutionDAO;
import Dao.TestDAO;
import Model.Execution;
import Tools.Executor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet ExecExecution qui gère l'exécution des instances de l'Execution qui ont échouées
 */
@WebServlet("/ExecExecution")
public class ExecExecution extends HttpServlet {

    /**
     * Execute les instances qui ont échouées
     * renvoie sur listeExecutions.jsp
     * @param req requete contenant l'identifiant de l'Execution que l'on souhaite relancer (id)
     * @param resp reponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object object = req.getParameter("id");
        ExecutionDAO executionDAO = new ExecutionDAO();
        if (object != null) {
            Execution execution = executionDAO.get(Long.parseLong(String.valueOf(object)));
            Executor.execute(execution);
        }
        //affichage de la liste des executions
        req.setAttribute("executions", executionDAO.getAll());
        TestDAO testDAO = new TestDAO();
        req.setAttribute("tests", testDAO.getAll());
        this.getServletContext().getRequestDispatcher("/listeExecutions.jsp").forward(req, resp);
    }
}
