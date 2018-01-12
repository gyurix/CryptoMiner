package gyurix.cryptominer.stratum.outpackets;

public class PacketOutSubmit extends PacketOut {
    public PacketOutSubmit(String method) {
        super("submit");
    }

}
