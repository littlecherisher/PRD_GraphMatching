package Servlets.Execution;

import Dao.ExecutionDAO;
import Model.Execution;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet InfosExecution qui gère l'affichage des informations sur l'Execution
 */
@WebServlet("/InfosExecution")
public class InfosExecutionServlet extends HttpServlet {
    private final ExecutionDAO executionDAO = new ExecutionDAO();

    /**
     * renvoie les informations sur l'Execution sélectionnée au format HTML
     * @param req req contenant l'identifiant de l'Execution dont on souhaite obtenir les informations
     * @param resp reponse
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF8");
        resp.setCharacterEncoding("UTF8");
        //affichage de la liste des executions
        String param = req.getParameter("execution");
        long id = Long.parseLong(param);
        Execution e = executionDAO.get(id);
        int nbInstances = e.getNbInstances();
        int nbInstancesExec = e.getNbInstancesExec();
        int nbInstancesEchec = e.getNbInstancesEchec();
        int nbInstancesAExec = e.getNbInstancesAExec();
        String etat = e.getEtat();
        //infos générales
        String response = "<fieldset>" +
                "<legend>Détails de l\'exécution " + e.getNom() + " :</legend>" +
                "<label>Exécutions : "+ nbInstancesExec+" / "+nbInstances+"</label> " +
                "<progress value='"+nbInstancesExec+"' max='"+nbInstances+"'></progress> "+
                (((double)nbInstancesExec/nbInstances)*100) +" %" +
                "</br></br>État : " + etat +
                "</br></br>"+nbInstancesEchec+" echec(s)";
        //accès aux résultats
        response += "<form action='/GraphMatching/FicheResultat?id="+id+"&info=general' method='post'>" +
                "<br/><input type='submit' value='Résultats'/></form>";
        //relancer les exéctions échouées
        if(etat.equals("Terminé") && nbInstancesAExec > 0){
            response += "<form action='/GraphMatching/ExecExecution?id="+id +"' method='post'>" +
                    "<br/><input type='submit' value='Relancer "+nbInstancesAExec+" instance(s)'/></form>";
        }
        //masquer l'execution
        response += "<form action='/GraphMatching/MasquerExecution?id="+id +"' method='post'>" +
                "<br/><input type='submit' value='Masquer cette exécution'/></form>";
        response += "</fieldset></br>";
        req.setAttribute("responseText",response);
        resp.getWriter().write(response);
    }
}
