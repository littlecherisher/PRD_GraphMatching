package Servlets.Test;

import Dao.DatasetDAO;
import Dao.MethodeDAO;
import Dao.ParametreDAO;
import Dao.TestDAO;
import Dao.TestMethodeDAO;
import Dao.TestMethodeParametreDAO;
import Model.Test;
import Model.TestMethode;
import Model.TestMethodeParametre;
import Model.Parametre;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet FicheTest permettant l'ajout, la modification et l'affichage d'un Test
 */
@WebServlet("/FicheTest")
public class FicheTestServlet extends HttpServlet {

    private final MethodeDAO methodeDAO = new MethodeDAO();
    private final DatasetDAO datasetDAO = new DatasetDAO();
    private final TestDAO testDAO = new TestDAO();
    private final TestMethodeDAO testmethodeDAO = new TestMethodeDAO();
    private final ParametreDAO parametreDAO = new ParametreDAO();
    private final TestMethodeParametreDAO testmethodeparametreDAO = new TestMethodeParametreDAO();
    
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
        req.setAttribute("methodes", methodeDAO.getAll()); //affichage de la liste des méthodes
        req.setAttribute("datasets", datasetDAO.getAll()); //affichage de la liste des datasets        
        req.setAttribute("testmethodeparametres", testmethodeparametreDAO.getAll()); //affichage de la liste des testmethodeparametres
        req.setAttribute("testmethodes", testmethodeDAO.getAll()); //affichage de la liste des testmethodes
        req.setAttribute("parametres", parametreDAO.getAll()); //affichage de la liste des parametres
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
        //récupération des informations du test
        String id = req.getParameter("test");
        String nom = req.getParameter("nom");
        String description = req.getParameter("description");
        String mode = req.getParameter("mode");
        String[] NomsParam = req.getParameterValues("nomsParam[]");
        String[] ValeursParam = req.getParameterValues("valeursParam[]");
        
        //récupération des paramètres heuristiques
        String[] NomsParamHeu = req.getParameterValues("nomsParamHeu[]");  
        String[] TypesParamHeu = req.getParameterValues("typesParamHeu[]");     
        String[] ValeursParamHeu = req.getParameterValues("valeursParamHeu[]");
        
        //récupération les méthodes, les datasets(subsets) et des paramètres
        String[] methodes = req.getParameterValues("selectedMethode[]");
        String[] data = req.getParameterValues("selectedData[]");
        String memoire = req.getParameter("memoire");
        String temps = req.getParameter("temps");
        String tempsHeur = req.getParameter("tempsHeur");
        String tolerence = req.getParameter("tolerence");
        String thread = req.getParameter("thread");        
        
        Test test = null;
        TestMethode testMethode = null;
        TestMethodeParametre testMethodeParametre = null;
        //Parametre parametre = null;
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
   
            if (NomsParamHeu != null){
            	String[] NomsValeursParamHeu = NomsParamHeu.clone();
            	for (int j = 0; j < NomsParamHeu.length; j++) {
                	test.addParamHeu(NomsParamHeu[j], ValeursParamHeu[j]);
                	StringBuilder sb = new StringBuilder(NomsParamHeu[j]);
                	sb.insert(NomsParamHeu[j].length(), " = ");
                	NomsValeursParamHeu[j] = sb + ValeursParamHeu[j];
                	test.addParamHeuris(NomsValeursParamHeu[j], TypesParamHeu[j]);
                }
            }

            if (memoire != "") test.addParam("memoire", memoire);
            if (temps != "") test.addParam("temps", temps);
            if (tempsHeur != "") test.addParam("tempsHeur", tempsHeur);
            if (tolerence != "") test.addParam("tolerence", tolerence);
            if (thread != "") test.addParam("thread", thread);
            
            //gestion des méthodes
            for (String m : methodes) {
                test.addMethode(methodeDAO.get(Long.parseLong(m)));
            }
            //gestion des data
            for (String d : data) {
                test.addData(d);
            }            
            testDAO.update(test);
            
            //update la base de données quand les utilisateurs modifient un test
            List<TestMethode> listTM = testmethodeDAO.getAll();
            List<TestMethodeParametre> listTMP = testmethodeparametreDAO.getAll();
            for(int n = 0; n < listTM.size(); n++){
        		if(listTM.get(n).getTest().getId() == Long.parseLong(id)){
        			for(int m = 0; m < listTMP.size(); m++){
                		if(listTMP.get(m).getTestMethode().getId() == listTM.get(n).getId())
                			testmethodeparametreDAO.remove(listTMP.get(m));
                	}
        			testmethodeDAO.remove(listTM.get(n));
        		}
        	}            
            //ajouter à nouveau le nouveau test(après modification faite par utilisateur)
            for (String m : methodes) {
                testMethode = new TestMethode(test, methodeDAO.get(Long.parseLong(m)));
                testmethodeDAO.save(testMethode);            
                List<Parametre> listP = parametreDAO.getAll();
				for (int k = 0; k < listP.size(); k++) {
					if (listP.get(k).getMethode().getId() == methodeDAO.get(Long.parseLong(m)).getId()) {
						if (NomsParamHeu != null)
							for (int j = 0; j < NomsParamHeu.length; j++) {
								if (listP.get(k).getNom().equals(NomsParamHeu[j])
										&& listP.get(k).getType().equals(TypesParamHeu[j])) {
									testMethodeParametre = new TestMethodeParametre(testMethode, listP.get(k),
											ValeursParamHeu[j]);
									testmethodeparametreDAO.save(testMethodeParametre);
									break;
								}
							}
					}
				}
            }
        } else {
            //création du test
            test = new Test(nom, description, Integer.parseInt(mode));
            //gestion des paramètres
            if (NomsParam != null) for (int i = 0; i < NomsParam.length; i++) {
                test.addParam(NomsParam[i], ValeursParam[i]);
            }
            
            if (NomsParamHeu != null){
            	String[] NomsValeursParamHeu = NomsParamHeu.clone();
            	for (int j = 0; j < NomsParamHeu.length; j++) {
            		test.addParamHeu(NomsParamHeu[j], ValeursParamHeu[j]);
                	StringBuilder sb = new StringBuilder(NomsParamHeu[j]);
                	sb.insert(NomsParamHeu[j].length(), " = ");
                	NomsValeursParamHeu[j] = sb + ValeursParamHeu[j];
                	test.addParamHeuris(NomsValeursParamHeu[j], TypesParamHeu[j]);
                }
            }
            if (memoire != "") test.addParam("memoire", memoire);
            if (temps != "") test.addParam("temps", temps);
            if (tempsHeur != "") test.addParam("tempsHeur", tempsHeur);
            if (tolerence != "") test.addParam("tolerence", tolerence);
            if (thread != "") test.addParam("thread", thread);
            //gestion des méthodes
            for (String m : methodes) {
                test.addMethode(methodeDAO.get(Long.parseLong(m)));
            }
            //gestion des data
            for (String d : data) {
                test.addData(d);
            }
            testDAO.save(test);
            //ajouter à nouveau le nouveau test
			for (String m : methodes) {
				testMethode = new TestMethode(test, methodeDAO.get(Long.parseLong(m)));
				testmethodeDAO.save(testMethode);
				List<Parametre> listP = parametreDAO.getAll();
				for (int k = 0; k < listP.size(); k++) {
					if (listP.get(k).getMethode().getId() == methodeDAO.get(Long.parseLong(m)).getId()) {
						if (NomsParamHeu != null)
							for (int j = 0; j < NomsParamHeu.length; j++) {
								if (listP.get(k).getNom().equals(NomsParamHeu[j])
										&& listP.get(k).getType().equals(TypesParamHeu[j])) {
									testMethodeParametre = new TestMethodeParametre(testMethode, listP.get(k),
											ValeursParamHeu[j]);
									testmethodeparametreDAO.save(testMethodeParametre);
									break;
								}
							}
					}
				}
			}  
        }
        //réaffichage de la fiche du test ajouté ou modifié
        req.setAttribute("test", test);
        req.setAttribute("methodes", methodeDAO.getAll()); //affichage de la liste des méthodes
        req.setAttribute("datasets", datasetDAO.getAll()); //affichage de la liste des datasets
        req.setAttribute("testmethodeparametres", testmethodeparametreDAO.getAll()); //affichage de la liste des testmethodeparametres
        req.setAttribute("testmethodes", testmethodeDAO.getAll()); //affichage de la liste des testmethodes
        req.setAttribute("parametres", parametreDAO.getAll()); //affichage de la liste des parametres
        this.getServletContext().getRequestDispatcher("/ficheTest.jsp").forward(req, resp);
    }
}
