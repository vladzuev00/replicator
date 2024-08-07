package by.aurorasoft.replicator.base;

import by.aurorasoft.replicator.registry.DeleteReplicationProducerRegistry;
import by.aurorasoft.replicator.registry.ReplicatedRepositoryRegistry;
import by.aurorasoft.replicator.registry.SaveReplicationProducerRegistry;
import by.aurorasoft.replicator.testconfig.TestConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
public abstract class AbstractSpringBootTest {

    //TODO: remove
    @MockBean
    private SaveReplicationProducerRegistry saveReplicationProducerRegistry;

    //TODO: remove
    @MockBean
    private DeleteReplicationProducerRegistry deleteReplicationProducerRegistry;

    //TODO: remove
    @MockBean
    private ReplicatedRepositoryRegistry replicatedRepositoryRegistry;
}
