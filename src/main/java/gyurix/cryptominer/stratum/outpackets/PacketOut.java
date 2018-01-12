package gyurix.cryptominer.stratum.outpackets;

import gyurix.cryptominer.stratum.Jsonable;
import lombok.Getter;

@Getter
public class PacketOut extends Jsonable {
    private long id = System.nanoTime();
    private String method;

    public PacketOut(String method) {
        this.method = method;
    }
}
