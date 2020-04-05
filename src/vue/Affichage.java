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
        try {
            tryToConnect();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void tryToConnect() throws ExecutionException, InterruptedException {
        List<Connector> connections = new ArrayList<>();
        ArrayList<String> networks = Reseau.listeDesReseaux();

        for (String network : networks)
            connections.add(new Connector(network));

        ExecutorService connectionExecutor = Executors.newCachedThreadPool();
        String connectionResult = connectionExecutor.invokeAny(connections);
        connectionExecutor.shutdown();
    }
}
