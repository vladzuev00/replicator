package by.aurorasoft.replicator.model.replication.produced;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import static by.aurorasoft.replicator.util.TransportNameUtil.ENTITY_ID_NAME;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public final class DeleteProducedReplication<ID> implements ProducedReplication<ID> {

    @JsonProperty(ENTITY_ID_NAME)
    private final ID entityId;
}
