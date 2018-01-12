package jp.nyatla.minya.stratum;

import org.codehaus.jackson.JsonNode;

public class StratumJsonJob {
    public final String blob, job_id, target;

    //{"blob":"0105","job_id":"7138","target":"ffff7f00"}
    public StratumJsonJob(JsonNode node) {
        blob = node.get("blob").asText();
        job_id = node.get("job_id").asText();
        target = node.get("target").asText();
    }
}
