package gyurix.cryptominer.stratum.inpackets.data;

import gyurix.cryptominer.stratum.Jsonable;
import lombok.Data;

@Data
public class SubmitResult extends Jsonable {
    private String status;
}
