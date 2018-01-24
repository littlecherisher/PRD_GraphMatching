package Servlets.Test;

import Dao.TestDAO;
import Model.Test;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet MasquerTest permettant de masquer un Test pour l'utilisateur
 */
@WebServlet("/MasquerTest")
public class DeleteTestServlet extends HttpServlet {

    /**
     * Masque le Test pour l'utilsateur et affiche la liste des Test visibles
     * renvoie sur listeTests.jsp
     * @param req requete contenant l'identifiant du Test à masquer (id)
     * @param resp reponse
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object object = req.getParameter("id");
        TestDAO testDAO = new TestDAO();
        if (object != null) {
            //on passe l'attribut visible à faux
            Test test = testDAO.get(Long.parseLong(String.valueOf(object)));
            test.setVisible();
            testDAO.update(test);
        }
        //on affiche la liste des tests
        req.setAttribute("tests", testDAO.getAll());
        this.getServletContext().getRequestDispatcher("/listeTests.jsp").forward(req, resp);
    }
}
