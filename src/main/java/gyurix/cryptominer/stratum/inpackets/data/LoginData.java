package gyurix.cryptominer.stratum.inpackets.data;

import gyurix.cryptominer.stratum.Jsonable;
import lombok.Data;

@Data
public class LoginData extends Jsonable {
    private String login, pass, agent;
}
