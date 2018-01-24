package Tools;

import Model.Execution;

import java.util.concurrent.*;

/**
 * Classe permettant la gestion des Executions dans une file de threads
 */
public class Executor {
    /**
     * ExecutorService
     */
    private static ExecutorService executorService;

    /**
     * Constructeur de la classe Executor
     */
    private Executor() {
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Met l'Execution dans la file d'exécution de l'Executor
     * @param e Execution à exécuter
     */
    public static void execute(Execution e) {
        if (executorService == null) {
            new Executor();
        }
        executorService.submit(e);
    }
}
