package Servlets.Methode;


import Dao.MethodeDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet ListeMethode affiche la liste des méthodes visibles par l'utilisateur
 */
@WebServlet("/ListeMethode")
public class ListeMethodeServlet extends HttpServlet {

    /**
     * Affiche la liste des Methode visibles
     * renvoie sur listeMethodes.jsp
     * @param req requete
     * @param resp reponse
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF8");
        resp.setCharacterEncoding("UTF8");
        //affichage de la liste des méthodes
        MethodeDAO methodeDao = new MethodeDAO();
        req.setAttribute("methodes", methodeDao.getAll());
        this.getServletContext().getRequestDispatcher("/listeMethodes.jsp").forward(req, resp);
    }
}
