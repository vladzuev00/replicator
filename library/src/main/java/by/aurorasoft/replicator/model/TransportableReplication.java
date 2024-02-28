package by.aurorasoft.replicator.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class TransportableReplication {
    ReplicationType type;
    String dtoJson;

    @JsonCreator
    public TransportableReplication(@JsonProperty("type") final ReplicationType type,
                                    @JsonProperty("dtoJson") final String dtoJson) {
        this.type = type;
        this.dtoJson = dtoJson;
    }
}
