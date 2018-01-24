package Servlets.Resultat;

import Dao.ExecutionDAO;
import Model.Execution;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Servlet FicheResultat récupère et retourne des informations sur le résultat d'une Execution selon le choix de l'utilisateur
 */
@WebServlet("/FicheResultat")
public class FicheResultatServlet extends HttpServlet {

    /**
     * FicheResultat récupère et retourne des informations sur le résultat d'une Execution selon le choix de l'utilisateur
     * renvoie sur /ficheResultat.jsp
     * @param req requete contenant l'identifiant de l'execution (id) et le type d'information (info)
     * @param resp reponse
     * @throws ServletException
     * @throws IOException
     */
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF8");
        resp.setCharacterEncoding("UTF8");

        String param = req.getParameter("id");
        ExecutionDAO executionDAO = new ExecutionDAO();
        Execution e = executionDAO.get(Long.parseLong(param));

        String infos = req.getParameter("info");
        switch(infos){
            case "general":
                req.setAttribute("description",e.getDescription());
                req.setAttribute("nbInstances",e.getNbInstances());
                req.setAttribute("nbInstancesExec",e.getNbInstancesExec());
                req.setAttribute("mode",e.printMode());
                req.setAttribute("methodes",e.printMethodes());
                req.setAttribute("datasets",e.printData());
                req.setAttribute("parametres",e.getParametres());
                break;
            case "appareillement" :
                req.setAttribute("listePaires",e.getListeAppareillement());
                req.setAttribute("methodes",e.getMethodes());
                break;
            case "count" :
            case"SolutionState":
                req.setAttribute("resultats",e.getCount(infos));
                break;
            default:
                req.setAttribute("resultats",e.getResultats(infos));
                break;
        }
        req.setAttribute("infos",infos);
        req.setAttribute("execution", e);
        this.getServletContext().getRequestDispatcher("/ficheResultat.jsp").forward(req, resp);
    }
}
