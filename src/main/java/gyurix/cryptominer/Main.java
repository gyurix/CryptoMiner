package gyurix.cryptominer;

import gyurix.cryptominer.stratum.StratumConnection;

import java.net.URI;

public class Main {
    private static final String PASS = "x";
    private static final String URL = "stratum+tcp://etn.nasf.eu:3333";
    private static final String USER = "etnkHbMqapBSTis28uPwiHiR9tFYqqSSiSPyGW6uxkrd7bGbbtQbbgtAyqthPGrghzGuM4YZhGBPqbatE1WZ1W4X4xvXkNs4Ep";

    public static void main(String[] args) throws Throwable {
        StratumConnection con = new StratumConnection(new URI(URL), USER, PASS);
    }

}
