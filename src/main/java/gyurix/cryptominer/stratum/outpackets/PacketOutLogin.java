package gyurix.cryptominer.stratum.outpackets;

import gyurix.cryptominer.stratum.inpackets.data.LoginData;
import lombok.Getter;

@Getter
public class PacketOutLogin extends PacketOut {
    private final LoginData params;

    public PacketOutLogin(LoginData loginData) {
        super("login");
        this.params = loginData;
    }
}
