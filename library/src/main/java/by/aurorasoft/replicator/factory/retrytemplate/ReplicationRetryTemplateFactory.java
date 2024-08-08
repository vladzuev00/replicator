package by.aurorasoft.replicator.factory.retrytemplate;

import by.aurorasoft.replicator.exception.RelatedReplicationNotDeliveredException;
import by.aurorasoft.replicator.property.ReplicationRetryConsumeProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationRetryTemplateFactory {
    private final ReplicationRetryConsumeProperty property;

    public RetryTemplate create() {
        return new RetryTemplateBuilder()
                .fixedBackoff(property.getTimeLapseMs())
                .maxAttempts(property.getMaxAttempts())
                .retryOn(RelatedReplicationNotDeliveredException.class)
                .build();
    }
}
