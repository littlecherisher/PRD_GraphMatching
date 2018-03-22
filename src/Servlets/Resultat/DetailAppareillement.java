package Servlets.Resultat;

import Dao.ExecutionDAO;
import Model.Execution;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet DetailAppareillement renvoie les informations sur les appareillements des noeuds et des arcs sous forme d'un tableau HTML
 */
@WebServlet("/DetailAppareillement")
public class DetailAppareillement extends HttpServlet{

    /**
     * renvoie le tableau HTML des appareillements
     * @param req requete
     * @param resp reponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF8");
        resp.setCharacterEncoding("UTF8");
        String exec = req.getParameter("execution");
        long idMethode = Long.parseLong(req.getParameter("methode"));
        ExecutionDAO executionDAO = new ExecutionDAO();
        Execution e = executionDAO.get(Long.parseLong(exec));
        String G1 = req.getParameter("G1");
        String G2 = req.getParameter("G2");

        String resultat = e.getDetailAppareillement(idMethode,G1,G2);
        req.setAttribute("responseText",resultat);
        resp.getWriter().write(resultat);
    }
}
