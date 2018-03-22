package Model;

import Dao.DatasetDAO;
import Dao.ParametreDAO;
import Dao.SubsetDAO;
import Dao.TestDAO;
import Dao.TestMethodeDAO;
import Dao.TestMethodeParametreDAO;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * Classe correspondant à un objet Test
 * Un Test correspond à l'association entre des Methode et un ou plusieurs Dataset et / ou Subset
 * Le lancement d'un Test produit une Execution
 */
@Entity
@Table
public class Test {
    /**
     * Identifiant du Test
     * Clé primaire auto générée
     */
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;
    
    /**
     * Nom du Test
     */
    private String nom;
    
    /**
     * Description du Test
     */
    private String description;
    
    /**
     * Mode d'execution du Test
     */
    private int mode;
    
    /**
     * Liste des Methode associés au Test
     */
    @ManyToMany
    private List<Methode> methodes;

    /**
     * Liste des Dataset associés au Test
     */
    @ManyToMany
    private List<Dataset> datasets;

    /**
     * Liste des Subset associés au Test
     */
    @ManyToMany
    private List<Subset> subsets;
    
    /**
     * Test visible ou non
     */
    private boolean visible;

    /**
     * Liste des paramètres du Test sous forme nom=valeur (Pour les méthodes exactes)
     */
    @ElementCollection
    private List<String> params;
    
    /**
     * Liste des paramètres du Test sous forme nom=valeur (Pour les méthodes heuristiques)
     */
    @ElementCollection
    private List<String> paramsH;
    
    /**
     * Liste des paramètres heuristiques du Test sous forme deux colones:type, nom=valeur (Pour les méthodes heuristiques)
     */
    @ElementCollection
    private Map<String, String> paramsHeu;
    
    /**
     * Constructeur par défaut de la classe Test
     */
    public Test() {
    }

    /**
     * Constructeur de la classe Test
     * @param nom nom du Test
     * @param description description du Test
     * @param mode entier représentant le mode d'exécution du Test
     */
    public Test(String nom, String description, int mode) {
        this.nom = nom;
        this.description = description;
        this.mode = mode;
        datasets = new ArrayList<>();
        methodes = new ArrayList<>();
        params = new ArrayList<>();
        paramsH = new ArrayList<>();
        paramsHeu = new HashMap<>();
        //paramsHeuTotal = new HashMap<String, Map<String, String>>();
        subsets = new ArrayList<>();
        visible = true;
    }

    /**
     * Getter : retourne l'identifiant du Test
     * @return identifiant du Test
     */
    public long getId() {

        return id;
    }

    /**
     * Getter : retourne le nom du Test
     * @return nom du Test
     */
    public String getNom() {
        return nom;
    }

    /**
     * Setter : modifie le nom du Test
     * @param nom nom du Test
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Getter : retourne la description du Test
     * @return description du Test
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter : modifie la description du Test
     * @param description nouvelle description du Test
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter : retourne un entier représentant le mode d'exécution du Test
     * @return entier représentant le mode d'exécution du Test
     */
    public int getMode() {
        return mode;
    }

    /**
     * Setter : Modifie le mode d'exécution du Test
     * @param mode entier représentant le mode d'exécution du Test
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * Getter : retourne vrai si le Test est visible par l'utilisateur, sinon faux
     * @return vrai si le Test est visible par l'utilisateur, sinon faux
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     *Setter : rend le Test non visible pour l'utilisateur
     */
    public void setVisible() {
        this.visible = false;
    }

    /**
     * Getter : retourne la liste des Methode associés au Test
     * @return liste des Methode associés au Test
     */
    public List<Methode> getMethodes() {
        return methodes;
    }

    /**
     * Getter : retourne la liste des Dataset associés au Test
     * @return liste des Dataset associés au Test
     */
    public List<Dataset> getDatasets() {
        return datasets;
    }

    /**
     * Getter : retourne la liste des Subset associés au Test
     * @return liste des Subset associés au Test
     */
    public List<Subset> getSubsets() {
        return subsets;
    }

    /**
     * Getter : retourne la liste des paramètres associés au Test
     * @return liste des paramètres associés au Test
     */
    public List<String> getParams() {
        return params;
    }
    
    /**
     * Getter : retourne la liste des paramètres heuristiques associés au Test
     * @return liste des paramètres heuristiques associés au Test
     */
    public List<String> getParamsH() {
        return paramsH;
    }

    /**
     * Ajoute un objet Methode à la liste
     * @param methode objet Methode à ajouter
     */
    public void addMethode(Methode methode) {
    	methodes.add(methode);
    	
    }

    /**
     * Ajoute un Dataset ou un Subset à la liste
     * @param data identifiant du Dataset (1) ou du Subset(1-1) à ajouter à la liste
     */
    public void addData(String data) {
        DatasetDAO datasetDAO = new DatasetDAO();
        //gestion d'un dataset entier
        if (data.indexOf('-') == -1) {
            datasets.add(datasetDAO.get(Long.parseLong(data)));
        } else {
            //gestion des subsets
            String[] dataSplit = data.split("-");
            //ajout du subset
            SubsetDAO subsetDAO = new SubsetDAO();
            subsets.add(subsetDAO.get(Long.parseLong(dataSplit[1])));
        }
    }

    /**
     * Ajoute un paramètre à la liste pour les méthodes exactes
     * @param nom nom du paramètre
     * @param valeur valeur du paramètre
     */
    public void addParam(String nom, String valeur) {
        if (!nom.isEmpty() && !valeur.isEmpty()) params.add(nom + " = " + valeur);
    }
    
    /**
     * Ajoute un paramètre à la liste pour les méthodes heuristiques
     * @param nom nom du paramètre
     * @param valeur valeur du paramètre
     */
    public void addParamHeu(String nom, String valeur) {
        if (!nom.isEmpty() && !valeur.isEmpty()) paramsH.add(nom + " = " + valeur);
    }
    
    /**
     * Ajoute un paramètre heuristique à la liste (contient nom et type)
     * @param nom nom du paramètre heuristique
     * @param valeur valeur du paramètre heuristique
     */
    public void addParamHeuris(String nomType, String valeur) {
        if (!nomType.isEmpty() && !valeur.isEmpty()) 
        	paramsHeu.put(nomType, valeur);
    }
    
    /**
     * Vide les listes des Methode, Dataset, Subset et paramètres
     */
    public void clearLists() {
        methodes.clear();
        datasets.clear();
        subsets.clear();
        params.clear();
    }
    
    /**
     * Vide les listes des Methode, Dataset, Subset et paramètres heuristiques
     */
    public void clearListsHeuris() {
    	paramsH.clear();
    	paramsHeu.clear();
    }

    /**
     * Renvoie les paramètres exactes sous la forme d'un HashMap
     * @return paramètres sous la forme d'un HashMap
     */
    public HashMap<String, String> getParametres() {
        HashMap<String, String> parametres = new HashMap<>();
        for (String s : params) {
            String[] split = s.split(" = ");
            if(split.length > 1) parametres.put(split[0], split[1]);
        }
        return parametres;
    }
    
    /**
     * Renvoie les paramètres heuristiques sous la forme d'un HashMap
     * @return paramètres sous la forme d'un HashMap
     */
    public HashMap<String, String> getParametresHeuristique() {
        HashMap<String, String> parametres = new HashMap<>();
        for (String s : paramsH) {
            String[] split = s.split(" = ");
            if(split.length > 1) parametres.put(split[0], split[1]);
        }
        return parametres;
    }

    /**
     * Créé le fichier (éventuellement le dossier) contenant les paramètres
     * @param id identifiant de l'Execution
     */
    public void fichierParam(long id){
        try {
        	Path path1 = Paths.get("ProjetPRD\\Executions",String.valueOf(id));
            if (!Files.exists(path1)) {
                Files.createDirectories(path1);
            }
            if(params.size() > 0) {
                path1 = Paths.get(path1.toString(), "param.txt");
                Files.write(path1, params, Charset.forName("UTF-8"), CREATE, TRUNCATE_EXISTING);
            }
            /*Path path3 = Paths.get("ProjetPRD\\Executions",String.valueOf(id));
            if(paramsH.size() > 0) {
                path3 = Paths.get(path3.toString(), "paramH.txt");
                Files.write(path3, paramsH, Charset.forName("UTF-8"), CREATE, TRUNCATE_EXISTING);
            }*/
            for(Methode m : methodes){
            	Path path2 = Paths.get("ProjetPRD\\Executions",String.valueOf(id));
                if (!Files.exists(path2)) {
                    Files.createDirectories(path2);
                }
                if(m.getParamsHeuristique().size() > 0) {
                    path2 = Paths.get(path2.toString(), "paramHeuristique-" + m.getId() + ".txt");
                    Files.write(path2, getEachParams(m.getId()), Charset.forName("UTF-8"), CREATE, TRUNCATE_EXISTING);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retourne la liste des Methode sous forme d'une chaine de caractères pour l'affichage
     * @return liste des Methode sous forme d'une chaine de caractères
     */
    public String printMethodes() {
        String chaine = "";
        for (Methode m : methodes) {
            chaine += m.getNom() + ", ";
        }
        return chaine.substring(0, chaine.length() - 2);
    }

    /**
     * Retourne la liste des Dataset et Subset sous forme d'une chaine de caractères pour l'affichage
     * @return liste des Dataset et Subset sous forme d'une chaine de caractères
     */
    public String printData() {
        String chaine = "";
        for(Dataset d : datasets){
            chaine += d.getNom()+", ";
        }
        for(Subset s : subsets){
            chaine += s.getNom()+ " ("+s.getDataset().getNom()+") ,";
        }
        int taille = chaine.length();
        if (taille > 0) chaine = chaine.substring(0, taille - 2);
        return chaine;
    }

    /**
     * Retourne le libellé du mode d'exécution du Test
     * @return libellé du mode d'exécution du Test
     */
    public String printMode(){
        switch (mode){
            case 1:
                return "IP";
            case 2 :
                return "LP";
            case 3 :
                return "IP pré-processing";
            case 4 :
                return "Heuristique";
        }
        return null;
    }

    /**
     * Retourne le nombre d'Execution associées au Test
     * @return nombre d'Execution associées au Test
     */
    public long getNbExecutions() {
        TestDAO testDAO = new TestDAO();
        return testDAO.getNbExecutions(id);
    }
    
    /**
     * Retourne la liste des paramètres heuristiques sous forme nom = valeur pour chaque méthode 
     * quand on va exécute un test qui contient deux méthodes heuristiques
     * @param id identifiant de la méthode heuristique 
     * @return la liste des paramètres heuristiques sous forme nom = valeur
     */
	public ArrayList<String> getEachParams(long id) {		
		ParametreDAO parametreDAO = new ParametreDAO();
		TestMethodeDAO testMethodeDAO = new TestMethodeDAO();
		TestMethodeParametreDAO testMethodeParametreDAO = new TestMethodeParametreDAO();
		ArrayList<String> ParamsNomValeur = new ArrayList<String>();
		List<Parametre> params = parametreDAO.getAll();
		List<TestMethode> testMethodes = testMethodeDAO.getAll();
		List<TestMethodeParametre> testMethodeParametres = testMethodeParametreDAO.getAll();
		for (int i = 0; i < params.size(); i++) {
			if (params.get(i).getMethode().getId() == id)
				for(int k = 0; k < testMethodes.size(); k++){
					if(testMethodes.get(k).getMethode().getId() == id && testMethodes.get(k).getTest().id == getId())
						for (int j = 0; j < testMethodeParametres.size(); j++) {
							if (testMethodeParametres.get(j).getParametre().getId() == params.get(i).getId() 
									&& testMethodeParametres.get(j).getTestMethode().getId() == testMethodes.get(k).getId()) {
								ParamsNomValeur.add(params.get(i).getNom() + " = " + testMethodeParametres.get(j).getValeur());
						}
					}
				}				
		}
		return ParamsNomValeur;
	}
}
