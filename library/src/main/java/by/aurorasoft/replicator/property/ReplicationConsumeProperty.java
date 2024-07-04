package by.aurorasoft.replicator.property;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public abstract class ReplicationConsumeProperty {

    @NotBlank
    private final String topicName;

    @NotBlank
    private final String pipelineId;
}
