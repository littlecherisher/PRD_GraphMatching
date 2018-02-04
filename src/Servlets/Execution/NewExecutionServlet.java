package Servlets.Execution;

import Dao.ExecutionDAO;
import Dao.TestDAO;
import Model.Execution;
import Model.Test;
import Tools.Executor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe NewExecution qui gère la création et l'exécution d'une Execution
 */
@WebServlet("/NewExecution")
public class NewExecutionServlet extends HttpServlet {
    private final ExecutionDAO executionDAO = new ExecutionDAO();
    private final TestDAO testDAO = new TestDAO();

    /**
     * Créé l'objet Execution, le sauvegarde et l'exécute
     * renvoie sur listeExecutions.jsp
     * @param req requete contenant les informations du formulaire
     * @param resp reponse
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF8");
        resp.setCharacterEncoding("UTF8");
        //récupération des informations de l'exécution
        String id = req.getParameter("id");
        String nom = req.getParameter("nom");
        String description = req.getParameter("description");
        Test t = testDAO.get(Long.parseLong(id));
        Execution e = new Execution(nom, description, t);
        //copie des paramètres du test
        e.setMethodes(new ArrayList<>(t.getMethodes()));
        e.setDatasets(new ArrayList<>(t.getDatasets()));
        e.setSubsets(new ArrayList<>(t.getSubsets()));
        e.setParams(new ArrayList<>(t.getParams()));
        e.setNbInstances(); //calcul du nombre d'instances
        executionDAO.save(e);
        t.fichierParam(e.getId()); //création du fichier de paramètres
        Executor.execute(e); //lancement de l'exécution
        req.setAttribute("executions", executionDAO.getAll());
        req.setAttribute("tests", testDAO.getAll());
        this.getServletContext().getRequestDispatcher("/listeExecutions.jsp").forward(req, resp);
    }
}
