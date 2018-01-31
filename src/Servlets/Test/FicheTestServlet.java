package Servlets.Test;

import Dao.DatasetDAO;
import Dao.MethodeDAO;
import Dao.TestDAO;
import Model.Test;
import Model.Methode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Servlet FicheTest permettant l'ajout, la modification et l'affichage d'un Test
 */
@WebServlet("/FicheTest")
public class FicheTestServlet extends HttpServlet {

    private final MethodeDAO methodeDAO = new MethodeDAO();
    private final DatasetDAO datasetDAO = new DatasetDAO();
    private final TestDAO testDAO = new TestDAO();

    /** Affiche la fiche du Test ou une fiche vierge pour un ajout
     * renvoie sur ficheTest.jsp
     * @param req requete contenant éventuellement l'identifiant du Test à afficher (id)
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF8");
        resp.setCharacterEncoding("UTF8");
        Object object = req.getParameter("test");
        if (object != null) {
            Test test = testDAO.get(Long.parseLong(String.valueOf(object)));
            if (test != null) {
                req.setAttribute("test", test);
            }
        }
        req.setAttribute("methodes", methodeDAO.getAll()); //affichage de la liste des modèles
        req.setAttribute("datasets", datasetDAO.getAll()); //affichage de la liste des datasets
        this.getServletContext().getRequestDispatcher("/ficheTest.jsp").forward(req, resp);
    }

    /**
     * Gère l'ajout ou la modification d'un Test
     * renvoie sur ficheTest.jsp
     * @param req requete contenant les informations du formulaire
     * @param resp reponse
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF8");
        resp.setCharacterEncoding("UTF8");
        //récupération des paramètres
        String id = req.getParameter("test");
        String nom = req.getParameter("nom");
        String description = req.getParameter("description");
        String mode = req.getParameter("mode");
        String[] NomsParam = req.getParameterValues("nomsParam[]");
        String[] ValeursParam = req.getParameterValues("valeursParam[]");
        
        String[] NomsParamHeu = req.getParameterValues("nomsParamHeu[]");  
        String[] TypesParamHeu = req.getParameterValues("typesParamHeu[]");     
        String[] ValeursParamHeu = req.getParameterValues("valeursParamHeu[]");
        
        String[] methodes = req.getParameterValues("selectedMethode[]");
        String[] data = req.getParameterValues("selectedData[]");
        String memoire = req.getParameter("memoire");
        String temps = req.getParameter("temps");
        String tempsHeur = req.getParameter("tempsHeur");
        String tolerence = req.getParameter("tolerence");
        String thread = req.getParameter("thread");        
        
        Test test = null;
        if (!id.isEmpty()) {
            //modification du test
            test = testDAO.get(Long.parseLong(id));
            if (!nom.equals(test.getNom())) test.setNom(nom);
            if (!description.equals(test.getDescription())) test.setDescription(description);
            if (!mode.equals(test.getMode())) test.setMode(Integer.parseInt(mode));
            test.clearLists();
            test.clearListsHeuris();
            //gestion des paramètres
            if (NomsParam != null) for (int i = 0; i < NomsParam.length; i++) {
                test.addParam(NomsParam[i], ValeursParam[i]);
            }  
            
            if (NomsParamHeu != null) for (int j = 0; j < NomsParamHeu.length; j++) {
            	StringBuilder sb = new StringBuilder(NomsParamHeu[j]);
            	sb.insert(NomsParamHeu[j].length(), " = ");
            	NomsParamHeu[j] = sb + ValeursParamHeu[j];
            	test.addParamHeuris(NomsParamHeu[j], TypesParamHeu[j]);
            }
           
            if (memoire != "") test.addParam("memoire", memoire);
            if (temps != "") test.addParam("temps", temps);
            if (tempsHeur != "") test.addParam("tempsHeur", tempsHeur);
            if (tolerence != "") test.addParam("tolerence", tolerence);
            if (thread != "") test.addParam("thread", thread);
            //gestion des modèles
            for (String m : methodes) {
                test.addMethode(methodeDAO.get(Long.parseLong(m)));
            }
            //gestion des data
            for (String d : data) {
                test.addData(d);
            }
            testDAO.update(test);
        } else {
            //création du test
            test = new Test(nom, description, Integer.parseInt(mode));
            //gestion des paramètres
            if (NomsParam != null) for (int i = 0; i < NomsParam.length; i++) {
                test.addParam(NomsParam[i], ValeursParam[i]);
            }
            
            if (NomsParamHeu != null) for (int j = 0; j < NomsParamHeu.length; j++) {
            	StringBuilder sb = new StringBuilder(NomsParamHeu[j]);
            	sb.insert(NomsParamHeu[j].length(), " = ");
            	NomsParamHeu[j] = sb + ValeursParamHeu[j];
            	test.addParamHeuris(NomsParamHeu[j], TypesParamHeu[j]);
            }
            
            if (memoire != "") test.addParam("memoire", memoire);
            if (temps != "") test.addParam("temps", temps);
            if (tempsHeur != "") test.addParam("tempsHeur", tempsHeur);
            if (tolerence != "") test.addParam("tolerence", tolerence);
            if (thread != "") test.addParam("thread", thread);
            //gestion des modèles
            for (String m : methodes) {
                test.addMethode(methodeDAO.get(Long.parseLong(m)));
            }
            //gestion des data
            for (String d : data) {
                test.addData(d);
            }
            testDAO.save(test);
        }
        //réaffichage de la fiche du test ajouté ou modifié
        req.setAttribute("test", test);
        req.setAttribute("methodes", methodeDAO.getAll()); //affichage de la liste des modèles
        req.setAttribute("datasets", datasetDAO.getAll()); //affichage de la liste des datasets
        this.getServletContext().getRequestDispatcher("/ficheTest.jsp").forward(req, resp);
    }
}
