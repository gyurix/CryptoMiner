package gyurix.cryptominer.stratum.inpackets;

import gyurix.cryptominer.stratum.Jsonable;

public class PacketIn extends Jsonable {
    private String error;
    private long id;
    private String jsonrpc;
}
