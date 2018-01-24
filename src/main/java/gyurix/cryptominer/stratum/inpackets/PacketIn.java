package gyurix.cryptominer.stratum.inpackets;

import gyurix.cryptominer.stratum.Jsonable;
import lombok.Getter;

@Getter
public class PacketIn extends Jsonable {
    private String error;
    private String method;
    private long id;
    private String jsonrpc;
}
