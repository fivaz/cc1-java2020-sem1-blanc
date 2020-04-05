package vue;

import metier.Connector;
import metier.Statut;
import outils.Reseau;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Affichage {

    private Statut statut = Statut.INIT;

    public void demarrerSynchronisation() {
        String network = null;
        try {
            network = tryToConnect();
            System.out.print("Avancement: " + statut + " -> ");
            statut = Statut.CONNECT_OK;
            System.out.print(statut);
        } catch (ExecutionException | InterruptedException e) {
            System.out.print("Avancement: " + statut + " -> ");
            statut = Statut.CONNECT_ERROR;
            System.out.print(statut);
        }
        System.out.println(" Reseau:" + network);
        try {
            tryToSync(network);
            System.out.print("Avancement: " + statut + " -> ");
            statut = Statut.TRANSFERT_OK;
            System.out.print(statut);

        } catch (InterruptedException e) {
            System.out.print("Avancement: " + statut + " -> ");
            statut = Statut.TRANSFERT_ERROR;
            System.out.print(statut);
        }
        System.out.println(" Reseau:" + network);
    }

    private void tryToSync(String network) throws InterruptedException {
        System.out.print("Avancement: " + statut + " -> ");
        statut = Statut.TRANSFERT;
        System.out.println(statut);
        Reseau.synchroViaReseau(network);
    }

    private String tryToConnect() throws ExecutionException, InterruptedException {
        System.out.print("Avancement: " + statut + " -> ");
        statut = Statut.CONNECT;
        System.out.println(statut);
        List<Connector> connections = new ArrayList<>();
        List<String> networks = Reseau.listeDesReseaux();
//        List<String> networks = Arrays.asList("3G", "3G");

        for (String network : networks)
            connections.add(new Connector(network));

        ExecutorService connectionExecutor = Executors.newCachedThreadPool();
        String connectionResult = connectionExecutor.invokeAny(connections);
        connectionExecutor.shutdown();
        return connectionResult;
    }
}
