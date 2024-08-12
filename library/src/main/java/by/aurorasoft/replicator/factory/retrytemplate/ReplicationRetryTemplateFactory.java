package by.aurorasoft.replicator.factory.retrytemplate;

import by.aurorasoft.replicator.exception.RelatedReplicationNotDeliveredException;
import by.aurorasoft.replicator.property.ReplicationRetryConsumeProperty;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;
import org.springframework.stereotype.Component;

@Component
public final class ReplicationRetryTemplateFactory {

    public RetryTemplate create(ReplicationRetryConsumeProperty property) {
        return new RetryTemplateBuilder()
                .fixedBackoff(property.getTimeLapseMs())
                .maxAttempts(property.getMaxAttempts())
                .retryOn(RelatedReplicationNotDeliveredException.class)
                .build();
    }
}
