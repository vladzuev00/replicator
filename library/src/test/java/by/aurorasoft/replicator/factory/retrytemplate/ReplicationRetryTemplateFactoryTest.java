package by.aurorasoft.replicator.factory.retrytemplate;

import by.aurorasoft.replicator.property.ReplicationRetryConsumeProperty;
import org.junit.jupiter.api.Test;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.function.Supplier;

import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ReplicationRetryTemplateFactoryTest {
    private static final String FIELD_NAME_TEMPLATE_BACK_OFF_POLICY = "backOffPolicy";
    private static final String FIELD_NAME_POLICY_BACK_OFF_PERIOD = "backOffPeriod";

    private final ReplicationRetryTemplateFactory factory = new ReplicationRetryTemplateFactory();

    @Test
    public void templateShouldBeCreated() {
        long givenTimeLapseMs = 255;
        int givenMaxAttempts = 10;
        var givenProperty = new ReplicationRetryConsumeProperty(givenTimeLapseMs, givenMaxAttempts);

        RetryTemplate actual = factory.create(givenProperty);

        BackOffPolicy actualBackOffPolicy = getBackOffPolicy(actual);
        assertTrue(actualBackOffPolicy instanceof FixedBackOffPolicy);
        long actualBackOffPeriod = getBackOffPeriod((FixedBackOffPolicy) actualBackOffPolicy);
        assertEquals(givenTimeLapseMs, actualBackOffPeriod);
    }

    private BackOffPolicy getBackOffPolicy(RetryTemplate template) {
        return getFieldValue(template, FIELD_NAME_TEMPLATE_BACK_OFF_POLICY, BackOffPolicy.class);
    }

    private long getBackOffPeriod(FixedBackOffPolicy policy) {
        return (Long) getFieldValue(policy, FIELD_NAME_POLICY_BACK_OFF_PERIOD, Supplier.class).get();
    }
}
