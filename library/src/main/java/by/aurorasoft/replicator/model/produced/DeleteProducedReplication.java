package by.aurorasoft.replicator.model.produced;

import lombok.Value;

@Value
public class DeleteProducedReplication<ID> implements ProducedReplication<ID> {
    ID entityId;
}
