package by.aurorasoft.replicator.model.produced;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import static by.aurorasoft.replicator.util.TransportNameUtil.DELETE_ENTITY_ID;

@Value
public class DeleteProducedReplication<ID> implements ProducedReplication<ID> {

    @JsonProperty(DELETE_ENTITY_ID)
    ID entityId;
}
