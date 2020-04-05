package vue;

import metier.Connector;
import metier.Statut;
import org.apache.log4j.Logger;
import outils.Reseau;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Affichage {

    private Statut statut = Statut.INIT;
    private Statut previousStatus = Statut.INIT;

    static final Logger logger = Logger.getLogger(Affichage.class);

    public void demarrerSynchronisation() {
        try {
            String network = tryToConnect();
            try {
                tryToSync(network);
            } catch (InterruptedException e) {
                previousStatus = statut;
                statut = Statut.TRANSFERT_ERROR;
                logger.info("Avancement: " + statut + " -> " + previousStatus);
            }
        } catch (ExecutionException | InterruptedException e) {
            previousStatus = statut;
            statut = Statut.CONNECT_ERROR;
            logger.info("Avancement: " + statut + " -> " + previousStatus);
        }
    }

    private void tryToSync(String network) throws InterruptedException {
        previousStatus = statut;
        statut = Statut.TRANSFERT;
        logger.info("Avancement: " + statut + " -> " + previousStatus + " Reseau:" + network);
        Reseau.synchroViaReseau(network);
        previousStatus = statut;
        statut = Statut.TRANSFERT_OK;
        logger.info("Avancement: " + statut + " -> " + previousStatus + " Reseau:" + network);
    }

    private String tryToConnect() throws ExecutionException, InterruptedException {
        previousStatus = statut;
        statut = Statut.CONNECT;
        logger.info("Avancement: " + statut + " -> " + previousStatus);
        List<Connector> connections = new ArrayList<>();
        List<String> networks = Reseau.listeDesReseaux();
//        List<String> networks = Arrays.asList("3G", "3G");

        for (String network : networks)
            connections.add(new Connector(network));

        ExecutorService connectionExecutor = Executors.newCachedThreadPool();
        String connectionResult = connectionExecutor.invokeAny(connections);
        connectionExecutor.shutdown();
        previousStatus = statut;
        statut = Statut.CONNECT_OK;
        logger.info("Avancement: " + statut + " -> " + previousStatus + " Reseau:" + connectionResult);
        return connectionResult;
    }
}
