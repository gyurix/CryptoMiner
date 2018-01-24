package gyurix.cryptominer.stratum.inpackets.data;

import gyurix.cryptominer.stratum.Jsonable;
import lombok.Getter;

@Getter
public class JobInfo extends Jsonable {
    private String jobId, blob, target;
}
