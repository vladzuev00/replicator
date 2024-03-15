package by.aurorasoft.replicator.model.produced;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static by.aurorasoft.replicator.util.TransportNameUtil.ENTITY_ID_NAME;

@RequiredArgsConstructor
@Getter
public final class DeleteProducedReplication<ID> implements ProducedReplication<ID> {

    @JsonProperty(ENTITY_ID_NAME)
    private final ID entityId;
}
