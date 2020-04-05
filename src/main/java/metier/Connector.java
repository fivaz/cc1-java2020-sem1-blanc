package metier;

import outils.Reseau;

import java.util.concurrent.Callable;

public class Connector implements Callable<String> {
    private String network;

    public Connector(String network) {
        this.network = network;
    }

    @Override
    public String call() throws Exception {
        Reseau.connectionReseau(network);
        return network;
    }
}
