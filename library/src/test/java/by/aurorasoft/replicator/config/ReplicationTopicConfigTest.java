package by.aurorasoft.replicator.config;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public final class ReplicationTopicConfigTest extends AbstractSpringBootTest {

    @Autowired
    private ReplicationTopicConfig config;

    @Test
    public void configShouldBeCreated() {
        final ReplicationTopicConfig expected = ReplicationTopicConfig.builder()
                .partitionCount(1)
                .replicationFactor(3)
                .build();
        assertEquals(expected, config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void configShouldNotBeCreatedBecauseOfPartitionCountLessThanMinValid() {
        ReplicationTopicConfig.builder()
                .partitionCount(0)
                .replicationFactor(3)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void configShouldNotBeCreatedBecauseOfPartitionCountBiggerThanMaxValid() {
        ReplicationTopicConfig.builder()
                .partitionCount(11)
                .replicationFactor(3)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void configShouldNotBeCreatedBecauseOfReplicationFactorLessThanMinValid() {
        ReplicationTopicConfig.builder()
                .partitionCount(1)
                .replicationFactor(0)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void configShouldNotBeCreatedBecauseOfReplicationFactorBiggerThanMaxValid() {
        ReplicationTopicConfig.builder()
                .partitionCount(1)
                .replicationFactor(11)
                .build();
    }
}
