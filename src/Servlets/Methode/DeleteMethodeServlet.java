package Servlets.Methode;


import Dao.MethodeDAO;
import Model.Methode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet MasquerMethode qui rend un Methode invisible pour l'utilisateur
 */
@WebServlet("/MasquerMethode")
public class DeleteMethodeServlet extends HttpServlet {

    /**
     * Rend la Methode invisible pour l'utilisateur et affiche la liste des Methode visibles
     * @param req requete contenant l'identifiant de la Methode à masquer (id)
     * renvoie sur listeMethodes.jsp
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object object = req.getParameter("id");
        MethodeDAO methodeDao = new MethodeDAO();
        if (object != null) {
            //on passe l'attribut visible à faux
            Methode methode = methodeDao.get(Long.parseLong(String.valueOf(object)));
            methode.setVisible();
            methodeDao.update(methode);
        }
        //on affiche la liste des modèles
        req.setAttribute("methodes", methodeDao.getAll());
        this.getServletContext().getRequestDispatcher("/listeMethodes.jsp").forward(req, resp);
    }
}
