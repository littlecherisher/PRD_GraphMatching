package Model;

import Dao.DatasetDAO;
import Dao.ExecutionDAO;
import Dao.SubsetDAO;
import Tools.Fichier;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Classe correpondant à un objet Execution
 * Une execution correspond à l'exécution d'un test
 * implémente Runnable pour les exécutions parralèles
 */
@Entity
@Table
public class Execution implements Runnable {
    /**
     * identifiant de l'Execution
     * clé primaire auto générée
     */
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;
    /**
     * nom de l'Execution
     */
    private String nom;
    /**
     * description de l'Execution
     */
    private String description;
    /**
     * etat de l'Execution : En attente, En cours, Terminé
     */
    private String etat;
    /**
     * nombre d'instance de l'execution
     * Une instance correspond à execution d'un modèle sur une paire de graphes
     */
    private int nbInstances;
    /**
     * mode de l'Execution 1 = IP, 2 = LP, 3 = IP pré-processing, 4 = Heuristique
     */
    private int mode;
    /**
     * vrai si l'Execution est visible par l'utilisateur, sinon faux
     */
    private boolean visible = true;
    /**
     * objet Test associé à l'Execution
     */
    @ManyToOne
    private Test test;
    /**
     * liste des méthodes associés à l'Execution
     */
    @ManyToMany
    private List<Methode> methodes;
    /**
     * Liste des Dataset associés à l'Execution
     */
    @ManyToMany
    private List<Dataset> datasets;
    /**
     * Liste des Subset associés à l'Execution
     */
    @ManyToMany
    private List<Subset> subsets;
    /**
     * Liste des paramètres associés à l'Execution
     */
    @ElementCollection
    private List<String> params;

    /**
     * Constructeur de la classe Execution
     * @param nom nom de l'Execution
     * @param description description de l'Execution
     * @param test objet Test associé à l'Execution
     */
    public Execution(String nom, String description, Test test) {
        this.nom = nom;
        this.description = description;
        this.test = test;
        this.etat = "En attente";
        this.mode = test.getMode();
    }

    /**
     * Constructeur par défaut de la classe Execution
     */
    public Execution() {
    }

    /**
     * Getter : retourne l'identifiant de l'Execution
     * @return identifiant de l'Execution
     */
    public long getId() {
        return id;
    }

    /**
     * Getter : retourne le nom de l'Execution
     * @return nom de l'Execution
     */
    public String getNom() {
        return nom;
    }

    /**
     * Getter : retourne l'état de l'Execution
     * @return état de l'Execution
     */
    public String getEtat() {
        return etat;
    }

    /**
     * Getter : retourne la description de l'Execution
     * @return description de l'Execution
     */
    public String getDescription() {

        return description;
    }

    /**
     * Getter : retourne vrai si l'Execution est visible par l'utilisateur, sinon faux
     * @return vrai si l'Execution est visible par l'utilisateur, sinon faux
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Setter : rend l'Execution invisible par l'utilisateur
     */
    public void setVisible() {
        this.visible = false;
    }

    /**
     * Getter : retourne le mode de l'Execution
     * @return mode de l'Execution
     */
    public int getMode() {
        return mode;
    }

    /**
     * Getter : retourne l'objet Test associé à l'Execution
     * @return objet Test associé à l'Execution
     */
    public Test getTest() {
        return test;
    }

    /**
     * Getter : retourne la liste des objets Methode associés à l'Executuion
     * @return liste des objets Methode associés à l'Executuion
     */
    public List<Methode> getMethodes() {
        return methodes;
    }

    /**
     * Setter : modifie la liste des methodes associés à l'Execution
     * @param methodes nouvelle liste des methodes associés à l'Execution
     */
    public void setMethodes(List<Methode> methodes) {
        this.methodes = methodes;
    }


    /**
     * Getter : retourne la liste des objets Dataset associés à l'Execution
     * @return liste des objets Dataset associés à l'Execution
     */
    public List<Dataset> getDatasets() {
        return datasets;
    }

    /**
     * Setter : modifie la liste des objets Dataset associés à l'Execution
     * @param datasets nouvelle liste des objets Dataset associés à l'Execution
     */
    public void setDatasets(List<Dataset> datasets) {
        this.datasets = datasets;
    }

    /**
     * Getter : retourne la liste des objets Subset associés à l'Execution
     * @return liste des objets Subset associés à l'Execution
     */
    public List<Subset> getSubsets() {
        return subsets;
    }

    /**
     * Setter : modifie la liste des objets Subset associés à l'Execution
     * @param subsets nouvelle liste des objets Subset associés à l'Execution
     */
    public void setSubsets(List<Subset> subsets) {
        this.subsets = subsets;
    }

    /**
     * Getter : retourne la liste des paramètres associés à l'Execution
     * @return liste des paramètres associés à l'Execution
     */
    public List<String> getParams() {
        return params;
    }

    /**
     * Setter : modifie la liste des paramètres associés à l'Execution
     * @param params nouvelle liste des paramètres associés à l'Execution
     */
    public void setParams(List<String> params) {
        this.params = params;
    }

    /**
     * Getter : retourne le nombre d'instances de l'Execution
     * @return nombre d'instances de l'Execution
     */
    public int getNbInstances() {
        return nbInstances;
    }

    /**
     * Setter : met à jour le nombre d'instances de l'Exection
     * appel de la méthode factorielle() pour les calculs factoriels
     * appel des méthodes getNbGraphes() des classes Dataset et Subset
     * pour récupérer le nombre de graphes des datasets et des substets
     */
    public void setNbInstances() {
        nbInstances = 0;
        for (Dataset d : datasets) {
            nbInstances += Math.pow((factorielle(d.getNbGraphes()) / (factorielle(d.getNbGraphes() - 1))), 2);
        }
        for (Subset s : subsets) {
            nbInstances += Math.pow((factorielle(s.getNbGraphes()) / (factorielle(s.getNbGraphes() - 1))), 2);
        }
        nbInstances *= methodes.size();
    }

    /**
     * Parcours les fichiers de sortie pour déterminer le nombre d'instances (nombre de lignes) exécutées
     * @return nombre d'instances exécutées
     */
    public int getNbInstancesExec() {
        BufferedReader in = null;
        int nb = 0;
        try {
            for (Methode m : methodes) {
                String path = "ProjetPRD\\Executions\\" + id + "\\output-" + m.getId() + ".txt";
                if (Files.exists(Paths.get(path))) {
                    File f = new File(path);
                    in = new BufferedReader(new FileReader(f));
                    while (in.readLine() != null) {
                        nb++;
                    }
                }
            }
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return nb;
        }
    }

    /**
     * Parcours les fichiers de sortie pour déterminer le nombre d'instances qui ont échouées
     * @return nombre d'instances échouées
     */
    public int getNbInstancesEchec() {
        BufferedReader in = null;
        int nb = 0;
        try {
            for (Methode m : methodes) {
                String path = "ProjetPRD\\Executions\\" + id + "\\output-" + m.getId() + ".txt";
                if (Files.exists(Paths.get(path))) {
                    File f = new File(path);
                    in = new BufferedReader(new FileReader(f));
                    String line = "";
                    while ((line = in.readLine()) != null) {
                        String[] infos = line.split(";");
                        for(String s : infos) {
                            String[] info = s.split(":");
                            if (info.length > 1) {
                                if (info[0].equals("SolutionState")) {
                                    if (!info[1].equals("1") && !info[1].equals("101") && !info[1].equals("102")) {
                                        nb++;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return nb;
        }
    }

    /**
     * Calcule le nombre d'instances restantes à exécuter
     * @return nombre d'instances restantes à exécuter
     */
    public int getNbInstancesAExec(){
        return getNbInstances() - (getNbInstancesExec() - getNbInstancesEchec());
    }
    /**
     * redefinition de la méthode run de Runnable
     * met à jour l'état de l'Execution
     * préparation des paramètres passés aux Methode
     * appel de la méthode Fichier.listeFichiers pour récupérer la liste des fichiers graphe du ou des Dataset
     * appel de la méthode Fichier.lectureSubset pour récupérer a liste des fichiers graphe du ou des Subset
     * appel de la méthode lancerExecution pour executer le Methode
     * pour chaque couple (Methode, graphe, graphe)
     */
    @Override
    public void run() {
        ExecutionDAO executionDAO = new ExecutionDAO();
        boolean aExecuter = !etat.equals("Terminé");
        etat = "En cours";
        executionDAO.update(this);
        Runtime runtime = Runtime.getRuntime();
        String[] args = new String[7];
        args[2] = String.valueOf(mode); //mode execution
        if (params.size() > 0)
            args[6] = System.getProperty("user.dir") + "\\ProjetPRD\\Executions\\" + id + "\\param.txt"; // chemin params
        //pour chaque modèle
        for (Methode m : test.getMethodes()) {
            System.out.println("methode : " + m.getId());
            args[5] = System.getProperty("user.dir") + "\\ProjetPRD\\Executions\\" + id + "\\output-" + m.getId() + ".txt"; // chemin sortie --> un fichier par modèle
            args[0] = m.getExecutable();
            if(!aExecuter) aExecuter = !Files.exists(Paths.get(args[5]));
            for (Dataset d : datasets) {
                args[1] = String.valueOf(d.getId());
                //si le fichier résultat existe on cherche le dataset dedans
                if(!aExecuter) aExecuter = !Fichier.FichierContient(args[5],args[1]);
                lancerExecution(Fichier.listeFichiers(d.getDataset()), runtime, args, aExecuter);
                aExecuter = false;
            }
            for (Subset s : subsets) {
                args[1] = s.getId() + "-" + s.getDataset().getId();
                //si le fichier résultat existe on cherche le subset dedans
                if(!aExecuter) aExecuter = !Fichier.FichierContient(args[5],args[1]);
                lancerExecution(Fichier.lectureSubset(s.getChemin()), runtime, args, aExecuter);
                aExecuter = false;
            }
        }
        etat = "Terminé";
        executionDAO.update(this);
        System.out.println("terminé");
    }

    /**
     * Appel à l'executable du modèle pour chaque paire de graphes
     * @param graphes liste des graphes
     * @param runtime objet Runtime permettant l'appel à l'exécutable
     * @param args paramètres passés à l'exécutable
     * @param aExecuter vrai si on doit executer toutes les instances, sinon faux si on doit tester l'existance de l'instance
     */
    private void lancerExecution(List<String> graphes, Runtime runtime, String[] args, boolean aExecuter) {
        for (int i = 0; i < graphes.size(); i++) {
            for (int j = 0 ; j < graphes.size(); j++) {
                args[3] = System.getProperty("user.dir") + "\\" + graphes.get(i);
                args[4] = System.getProperty("user.dir") + "\\" + graphes.get(j);
                //final Process p = null;
                if(aExecuter || aExecuter(args[5], args[1], graphes.get(i), graphes.get(j))){
                    try {
                        System.out.println(graphes.get(i) + " --> " + graphes.get(j));
                        final Process p = runtime.exec(args);
                        new Thread(() -> {
                            try {
                                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                                String line = "";
                                try {
                                    while ((line = reader.readLine()) != null) System.out.println("sortie : " + line);
                                } finally {
                                    reader.close();
                                }
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }
                        }).start();
                        new Thread(() -> {
                            try {
                                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                                String line = "";
                                try {
                                    while ((line = reader.readLine()) != null) System.err.println("erreur : " + line);
                                } finally {
                                    reader.close();
                                }
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }
                        }).start();
                        p.waitFor();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Lecture des fichiers de sortie pour extraire les minimums, moyennes et maximums correspondant au critère info
     * appel à la méthode calculDeviation si info = deviation
     * @param info nom du critère à récupérer
     * @return structure contenant les min, sum, max et count du critère info par dataset ou subset et par modèle
     */
    public HashMap<String, HashMap<String, List<Double>>> getResultats(String info) {
        final int MIN = 0;
        final int MAX = 1;
        final int SUM = 2;
        final int NB = 3;
        //Initialisation
        HashMap<String, HashMap<String, List<Double>>> result = initHashMap(!info.equals("ObjVal"));
        BufferedReader in;
        Double BestOpt = Double.MAX_VALUE;
        String dataname;
        for (Methode m : methodes) {
            String line;
            try {
                File file = new File("ProjetPRD\\Executions\\" + id + "\\output-" + m.getId() + ".txt");
                in = new BufferedReader(new FileReader(file));
                HashMap<String, List<Double>> resultSub = new HashMap<>();
                List<Double> resultMethode = new ArrayList<>(4);
                while ((line = in.readLine()) != null) {
                    double UB = 0, LB;
                    dataname = "";
                    
                    String[] split = line.split(";");
                    for (int i = 0; i < split.length; i++) {
                        String[] subSplit = split[i].split(":");
                        if(subSplit.length > 1){
                            String nom = subSplit[0];
                            String val = subSplit[1];
                            switch (nom) {
                                case "Dataset": //nom dataset
                                    dataname = val;
                                    if (result.containsKey(val)) {
                                        resultSub = result.get(val);
                                        if (resultSub.containsKey(String.valueOf(m.getId()))) {
                                            resultMethode = resultSub.get(String.valueOf(m.getId()));
                                        }
                                    }
                                    break;
                                case "UB": //écart bornes
                                    if (info.equals("UB/LB")) UB = Double.valueOf(val);
                                    break;
                                case "LB": //écart bornes
                                    if (info.equals("UB/LB")) {
                                        LB = Double.valueOf(val);
                                        Double valeur = UB - LB;
                                        if (valeur < resultMethode.get(MIN)) {
                                            resultMethode.set(MIN, valeur);
                                        }
                                        if (valeur > resultMethode.get(MAX)) {
                                            resultMethode.set(MAX, valeur);
                                        }
                                        resultMethode.set(SUM, resultMethode.get(SUM) + valeur);
                                        resultMethode.set(NB, resultMethode.get(NB) + 1);
                                        resultSub.replace(String.valueOf(m.getId()), resultMethode);
                                        result.replace(dataname, resultSub);
                                        i = split.length; //sortie de la boucle
                                    }
                                    break;
                                case "ObjVal":
                                    if (info.equals("ObjVal")) {
                                        Double valeur = Double.valueOf(val);
                                        //if (valeur < BestOpt) BestOpt = valeur;
                                        if (valeur < BestOpt && valeur != 0) BestOpt = valeur;
                                        resultMethode.add(valeur);
                                    }
                                    break;
                                default: //temps cpu, noeuds explorés, % pré-pro
                                    if (nom.equals(info)) {
                                        Double valeur = Double.valueOf(val);

                                        if (valeur < resultMethode.get(MIN)) {
                                            resultMethode.set(MIN, valeur);
                                        }
                                        if (valeur > resultMethode.get(MAX)) {
                                            resultMethode.set(MAX, valeur);
                                        }
                                        resultMethode.set(SUM, resultMethode.get(SUM) + valeur);
                                        resultMethode.set(NB, resultMethode.get(NB) + 1);
                                        resultSub.replace(String.valueOf(m.getId()), resultMethode);
                                        result.replace(dataname, resultSub);
                                        i = split.length; //sortie de la boucle
                                    }
                                    break;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (info.equals("ObjVal")) {
            return calculDeviation(BestOpt, result);
        }
        return result;
    }

    /**
     * Formate le résultat de la méthode getResultats pour calculer la déviation de la fonction objectif
     * @param min plus petite valeur de la fonction objectif
     * @param data structure contenant les min, sum, max et count des fonctions objectif par dataset ou subset et par modèle
     * @return structure contenant les min, sum, max et count de la déviation de la fonction objectif par dataset ou subset et par modèle
     */
    private HashMap<String, HashMap<String, List<Double>>> calculDeviation(double min, HashMap<String, HashMap<String, List<Double>>> data) {
        final int MIN = 0;
        final int MAX = 1;
        final int SUM = 2;
        final int NB = 3;
        HashMap<String, HashMap<String, List<Double>>> result = initHashMap(true);
        for (Map.Entry<String, HashMap<String, List<Double>>> entry : data.entrySet()) {
            for (Map.Entry<String, List<Double>> entryMethode : entry.getValue().entrySet()) {
                for (double d : entryMethode.getValue()) {
                	double dev = Math.abs(min - d / min);
                    List<Double> retour = result.get(entry.getKey()).get(entryMethode.getKey());
                    if (dev < retour.get(MIN)) {
                        retour.set(MIN, dev);
                    }
                    if (dev > retour.get(MAX)) {
                        retour.set(MAX, dev);
                    }
                    retour.set(SUM, retour.get(SUM) + dev);
                    retour.set(NB, retour.get(NB) + 1);
                    result.get(entry.getKey()).replace(entryMethode.getKey(), retour);
                }
            }
        }
        return result;
    }

    /**
     * Lecture des fichiers de sortie pour extraire le nombre de linges correspondantes au critère info
     * @param info nom du critère à récupérer
     * @return structure contenant le nombre de lignes correspondantes au critère par dataset ou subset et par modèle
     */
    public HashMap<String, HashMap<String, Double>> getCount(String info) {
        //Initialisation
        HashMap<String, HashMap<String, Double>> result = initHashMap();
        BufferedReader in;
        for (Methode m : methodes) {
            String line;
            try {
                File file = new File("ProjetPRD\\Executions\\" + id + "\\output-" + m.getId() + ".txt");
                in = new BufferedReader(new FileReader(file));
                HashMap<String, Double> resultSub = new HashMap<>();
                while ((line = in.readLine()) != null) {
                    String dataname = "";
                    String[] split = line.split(";");
                    for (int i = 0; i < split.length; i++) {
                        String[] subSplit = split[i].split(":");
                        String nom = subSplit[0];
                        String val = subSplit[1];
                        switch (nom) {
                            case "Dataset": //nom dataset
                                dataname = val;
                                if (result.containsKey(val)) {
                                    resultSub = result.get(val);
                                }
                                break;
                            case "SolutionState": //Solutions optimales
                                if (info.equals("SolutionState")){
                                    if (val.equals("1") || val.equals("101") || val.equals("102")) {
                                        resultSub.replace(String.valueOf(m.getId()), resultSub.get(String.valueOf(m.getId())) + 1.0);
                                        result.replace(dataname, resultSub);
                                        i = split.length; //sortie de la boucle
                                    }
                                }

                                break;
                            default: //instances traitées
                                if (info.equals("count")) {
                                    resultSub.replace(String.valueOf(m.getId()), resultSub.get(String.valueOf(m.getId())) + 1.0);
                                    result.replace(dataname, resultSub);
                                    i = split.length; //sortie de la boucle
                                }
                                break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Lecture des fichier de sortie pour extraire les paires de graphes sur les quelles on a exécuté les Methode
     * @return liste des paires de graphes sur les quelles on a exécuté les Methode
     */
    public List<List<String>> getListeAppareillement() {
        List<String> G1 = new ArrayList<>();
        List<String> G2 = new ArrayList<>();
        BufferedReader in;
        for (Methode m : methodes) {
            String line;
            try {
                File file = new File("ProjetPRD\\Executions\\" + id + "\\output-" + m.getId() + ".txt");
                in = new BufferedReader(new FileReader(file));
                while ((line = in.readLine()) != null) {
                    String[] split = line.split(";");
                    for (int i = 0; i < split.length; i++) {
                        String[] subSplit = split[i].split(":");
                        String nom = subSplit[0];
                        String val = subSplit[1];
                        if (nom.equals("G1Name")) {
                            if (!G1.contains(val)) G1.add(val);
                        } else if (nom.equals("G2Name")) {
                            if (!G2.contains(val)) G2.add(val);
                            i = split.length; //sortie de la boucle
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        List<List<String>> resultat = new ArrayList<>();
        resultat.add(G1);
        resultat.add(G2);
        return resultat;
    }

    /**
     * parours un fichier de sortie pour extraire l'appareillement des sommets et des arcs entre deux graphes pour une Methode
     * @param idMethode identifiant de la Methode
     * @param g1 nom du graphe 1
     * @param g2 nom du Graphe 2
     * @return tableau HTML contenant les résutlats de l'appareillement
     */
    public String getDetailAppareillement(long idMethode, String g1, String g2) {
        String noeuds = "";
        String arcs = "";
        String resultat = "";
        String lastDataset = "";
        try {
            File file = new File("ProjetPRD\\Executions\\" + id + "\\output-" + idMethode + ".txt");
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;

            while ((line = in.readLine()) != null) {
                String[] split = line.split(";");
                for (int i = 0; i < split.length; i++) {
                    String[] subSplit = split[i].split(":");
                    String nom = subSplit[0];
                    String val = subSplit[1];
                    switch (nom) {
                        case "Dataset":
                            if(lastDataset != "" && !lastDataset.equals(val)){
                                resultat += construitTableau(lastDataset,noeuds,arcs,g1,g2);
                                noeuds = "";
                                arcs = "";
                            }
                            lastDataset = val;
                        break;
                        case "G1Name":
                            if (!val.equals(g1)) {
                                i = split.length; //sortie de la boucle
                            }
                        break;
                        case "G2Name":
                            if (!val.equals(g2)) {
                                i = split.length; //sortie de la boucle
                            }
                        break;
                        case "MatchingData":
                            String[] appareillements = val.split("\\|");
                            if (appareillements.length > 1) {
                                //MatchingData:[1-->1]=1.000000|[2-->12]=1.000000
                                for (String s : appareillements) {
                                    s = s.substring(s.indexOf("[") + 1, s.indexOf("]"));
                                    if (!s.contains("N")) { //si pas d'appareillement avec un noeud / arc "null"
                                        String[] app = s.split("-->");
                                        if (app[0].contains(",")) {
                                            arcs += "<tr><td>" + app[0] + "</td><td>" + app[1] + "</td></tr>";
                                        } else {
                                            noeuds += "<tr><td>" + app[0] + "</td><td>" + app[1] + "</td></tr>";
                                        }
                                    }

                                }
                            } else {
                                //MatchingData:Node:1->2=0.000000/Node:2->4=5.500000
                                String ligne = line.substring(line.indexOf("MatchingData:") + 13);
                                appareillements = ligne.split("/");
                                for (String s : appareillements) {
                                    s = s.substring(s.indexOf(":") + 1, s.indexOf("="));
                                    if (!s.contains("eps_id")) { //si pas d'appareillement avec un noeud / arc "null"
                                        String[] app = s.split("->");
                                        if (app[0].contains(",")) {
                                            arcs += "<tr><td>" + app[0] + "</td><td>" + app[1] + "</td></tr>";
                                        } else {
                                            noeuds += "<tr><td>" + app[0] + "</td><td>" + app[1] + "</td></tr>";
                                        }
                                    }
                                }
                            }
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultat += construitTableau(lastDataset,noeuds,arcs,g1,g2);
        if(resultat.equals("")){
            return "Pas de comparaison effectuée entre " + g1 + " et " + g2;
        }else{
            return resultat;
        }
    }

    /**
     * Retourne le tableau HTML permettant l'affichage des appareillements
     * @param data identifiant du Dataset ou Subset concerné
     * @param noeuds appareillement des noeuds
     * @param arcs appareillement des arcs
     * @param g1 graphe 1
     * @param g2 graphe 2
     * @return tableau HTML permettant l'affichage des appareillements
     */
    private String construitTableau(String data, String noeuds, String arcs, String g1, String g2){
        String resultat = "";
        if(noeuds != "" || arcs != ""){
            DatasetDAO datasetDAO = new DatasetDAO();
            if(data.contains("-")){
                SubsetDAO subsetDAO = new SubsetDAO();
                String[] split = data.split("-");
                resultat = "<fieldset><legend>"+subsetDAO.get(Long.parseLong(split[0])).getNom()+" ("+datasetDAO.get(Long.parseLong(split[1])).getNom()+")</legend>";
            }else{
                resultat = "<fieldset><legend>"+datasetDAO.get(Long.parseLong(data)).getNom()+"</legend>";
            }
            if(!noeuds.equals("")){
                resultat += "<div class='tableau'>" +
                        "<h4>Noeuds</h4>" +
                        "<table id='noeuds' class='display noeuds' cellspacing='0'>" +
                        "<thead>" +
                        "<tr>" +
                        "<th>"+g1+"</th>" +
                        "<th>"+g2+"</th>" +
                        "</thead>" +
                        "<tbody>" +
                        noeuds +
                        "</tbody>" +
                        "</table>" +
                        "</div>";
            }
            if(!arcs.equals("")){
                resultat += "<div class='tableau'>" +
                        "<h4>Arcs</h4>" +
                        "<table id='arcs' class='display arcs' cellspacing='0'>" +
                        "<thead>" +
                        "<tr>" +
                        "<th>"+g1+"</th>" +
                        "<th>"+g2+"</th>" +
                        "</thead>" +
                        "<tbody>" +
                        arcs +
                        "</tbody>" +
                        "</table>" +
                        "</div>";
            }
            resultat+="</fieldset>";
        }

        return resultat;
    }

    /**
     * Retourne le libellé du mode en fonction de son identifiant
     * @return libellé du mode en fonction de son identifiant
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
     * Construit une chaine contenant la liste des noms des Methode associés à l'Execution pour l'affichage
     * @return chaine contenant la liste des noms des Methode
     */
    public String printMethodes() {
        String chaine = "";
        for (Methode m : methodes) {
            chaine += m.getNom() + ", ";
        }
        return chaine.substring(0, chaine.length() - 2);
    }

    /**
     * Construit une chaine contenant la liste des noms des Dataset et Subset associés à l'Execution pour l'affichage
     * @return chaine contenant la liste des noms des Dataset et Subset
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
     * Retourne la liste des paramètres associés à l'Execution sous forme de HashMap
     * @return HashMap contenant les paramètres associés à l'Execution
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
     * Calcul la factorielle
     * @param n paramètre de la factorielle à calculer
     * @return résultat de la factorielle
     */
    private int factorielle(int n){
        if(n == 0){
            return 1;
        }else{
            return n * factorielle(n-1);
        }
    }

    /**
     * Determine si l'instance doit être (ré)exécutée
     * @param path chemin du fichier résultat
     * @param dataset nom du dataset de l'instance
     * @param g1 nom du graphe 1 de l'instance
     * @param g2 nom du graphe 2 de l'instance
     * @return vrai si l'instance doit être exécutée, sinon faux
     */
    public boolean aExecuter(String path, String dataset, String g1, String g2){
        g1 = g1.substring(g1.lastIndexOf("\\")+1);
        g2 = g2.substring(g2.lastIndexOf("\\")+1);
        try {
            File file = new File(path);
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = "";
            while((line = in.readLine()) != null){
                //si une ligne contient dataset + graphes
                if(line.contains("Dataset:"+dataset) && line.contains("G1Name:"+g1) && line.contains("G2Name:"+g2)){
                    String[] infos = line.split(";");
                    for(String s : infos){
                        String[] info = s.split(":");
                        if(info.length > 1){
                            if(info[0].equals("SolutionState")){
                                if(!info[1].equals("1") && !info[1].equals("101") && !info[1].equals("102")){
                                    Fichier.supprimerLigne(path,line);
                                    return true; //erreur --> à exécuter
                                }else{
                                    return false; //exécution réussie
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return true; //ligne non trouvée --> à exécuter
    }

    /**
     * Initialise les liste de Double
     * @param init vrai si la liste doit être remplie avec des valeurs par défaut, sinon faux
     * @return liste de Double initialisée
     */
    private List<Double> initArrayList(boolean init) {
        List<Double> valeurs = new ArrayList<>();
        if (init) {
            valeurs.add(Double.MAX_VALUE);
            for (int i = 1; i < 4; i++) {
                valeurs.add(0.0);
            }
        }
        return valeurs;
    }

    /**
     * Initialise la structure qui contiendra les données résultat de forme min, sum, max, count
     * @param init vrai si la liste de Double doit être remplie avec des valeurs par défaut, sinon faux
     * @return structure initialisée
     */
    private HashMap<String, HashMap<String, List<Double>>> initHashMap(boolean init) {
        HashMap<String, HashMap<String, List<Double>>> result = new HashMap<>(subsets.size() + datasets.size());
        for (Dataset d : datasets) {
            HashMap<String, List<Double>> resultSub = new HashMap<>();
            for (Methode m : methodes) {
                resultSub.put(String.valueOf(m.getId()), initArrayList(init));
            }
            result.put(String.valueOf(d.getId()), resultSub);
        }
        for (Subset s : subsets) {
            HashMap<String, List<Double>> resultSub = new HashMap<>();
            for (Methode m : methodes) {
                resultSub.put(String.valueOf(m.getId()), initArrayList(init));
            }
            result.put(s.getId() + "-" + s.getDataset().getId(), resultSub);
        }
        return result;
    }

    /**
     * Initialise la structure qui contiendra les données résultat de forme count
     * @return structure initialisée
     */
    private HashMap<String, HashMap<String, Double>> initHashMap() {
        HashMap<String, HashMap<String, Double>> result = new HashMap<>(subsets.size() + datasets.size());
        for (Dataset d : datasets) {
            HashMap<String, Double> resultSub = new HashMap<>();
            for (Methode m : methodes) {
                resultSub.put(String.valueOf(m.getId()), 0.0);
            }
            result.put(String.valueOf(d.getId()), resultSub);
        }
        for (Subset s : subsets) {
            HashMap<String, Double> resultSub = new HashMap<>();
            for (Methode m : methodes) {
                resultSub.put(String.valueOf(m.getId()), 0.0);
            }
            result.put(s.getId() + "-" + s.getDataset().getId(), resultSub);
        }
        return result;
    }

}