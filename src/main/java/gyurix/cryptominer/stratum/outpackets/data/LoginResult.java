package gyurix.cryptominer.stratum.outpackets.data;

import gyurix.cryptominer.stratum.inpackets.data.JobInfo;
import lombok.Data;

@Data
public class LoginResult {
    private String id;
    private JobInfo job;
}
