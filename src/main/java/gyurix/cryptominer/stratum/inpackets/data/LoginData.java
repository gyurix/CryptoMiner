package gyurix.cryptominer.stratum.inpackets.data;

import gyurix.cryptominer.stratum.Jsonable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginData extends Jsonable {
    private String login, pass, agent;
}
