package metier;

import outils.Reseau;

import java.util.concurrent.Callable;

public class Transferer implements Callable<String> {
    private String network;

    public Transferer(String network) {
        this.network = network;
    }

    @Override
    public String call() throws Exception {
        Reseau.synchroViaReseau(network);
        return network;
    }
}
