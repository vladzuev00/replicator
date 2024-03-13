package by.aurorasoft.replicator.model.produced;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public final class DeleteProducedReplication<ID> implements ProducedReplication<ID> {
    private final ID entityId;
}
