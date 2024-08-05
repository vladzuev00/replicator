package by.aurorasoft.replicator.model.pipeline;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class ReplicationPipeline {
    private final String topic;
}
