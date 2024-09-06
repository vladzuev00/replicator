package by.aurorasoft.replicator.factory.retrytemplate;

import by.aurorasoft.replicator.exception.RelatedReplicationNotDeliveredException;
import by.aurorasoft.replicator.property.ReplicationRetryConsumeProperty;
import org.junit.jupiter.api.Test;
import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.BinaryExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.CompositeRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toUnmodifiableSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ReplicationRetryTemplateFactoryTest {
    private static final String FIELD_NAME_TEMPLATE_BACK_OFF_POLICY = "backOffPolicy";
    private static final String FIELD_NAME_TEMPLATE_RETRY_POLICY = "retryPolicy";

    private static final String FIELD_NAME_POLICY_BACK_OFF_PERIOD = "backOffPeriod";
    private static final String FIELD_NAME_POLICY_POLICIES = "policies";

    private static final String FIELD_NAME_CLASSIFIER_CLASSIFIED = "classified";

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

        RetryPolicy actualRetryPolicy = getRetryPolicy(actual);
        int actualMaxAttempts = actualRetryPolicy.getMaxAttempts();
        assertEquals(givenMaxAttempts, actualMaxAttempts);

        assertTrue(actualRetryPolicy instanceof CompositeRetryPolicy);
        var actualRetriedExceptions = getRetriedExceptions((CompositeRetryPolicy) actualRetryPolicy);
        assertTrue(actualRetriedExceptions.contains(RelatedReplicationNotDeliveredException.class));
    }

    private BackOffPolicy getBackOffPolicy(RetryTemplate template) {
        return getFieldValue(template, FIELD_NAME_TEMPLATE_BACK_OFF_POLICY, BackOffPolicy.class);
    }

    private long getBackOffPeriod(FixedBackOffPolicy policy) {
        return (Long) getFieldValue(policy, FIELD_NAME_POLICY_BACK_OFF_PERIOD, Supplier.class).get();
    }

    private RetryPolicy getRetryPolicy(RetryTemplate template) {
        return getFieldValue(template, FIELD_NAME_TEMPLATE_RETRY_POLICY, RetryPolicy.class);
    }

    private Set<Class<? extends Throwable>> getRetriedExceptions(CompositeRetryPolicy compositePolicy) {
        return stream(getPolicies(compositePolicy))
                .filter(policy -> policy instanceof BinaryExceptionClassifierRetryPolicy)
                .map(policy -> (BinaryExceptionClassifierRetryPolicy) policy)
                .map(BinaryExceptionClassifierRetryPolicy::getExceptionClassifier)
                .flatMap(this::getClassifiedExceptions)
                .collect(toUnmodifiableSet());
    }

    private RetryPolicy[] getPolicies(CompositeRetryPolicy policy) {
        return getFieldValue(policy, FIELD_NAME_POLICY_POLICIES, RetryPolicy[].class);
    }

    @SuppressWarnings("unchecked")
    private Stream<Class<? extends Throwable>> getClassifiedExceptions(BinaryExceptionClassifier classifier) {
        return getFieldValue(classifier, FIELD_NAME_CLASSIFIER_CLASSIFIED, Map.class)
                .keySet()
                .stream();
    }
}
