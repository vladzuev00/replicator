package by.aurorasoft.replicator.property;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@Validated
public abstract class ReplicationConsumeProperty {

    @NotBlank
    private final String topicName;

    @NotBlank
    private final String pipelineId;
}
