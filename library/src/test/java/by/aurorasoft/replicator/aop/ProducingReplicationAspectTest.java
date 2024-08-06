package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.registry.DeleteReplicationProducerRegistry;
import by.aurorasoft.replicator.registry.SaveReplicationProducerRegistry;
import by.aurorasoft.replicator.testrepository.FirstTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public final class ProducingReplicationAspectTest extends AbstractSpringBootTest {

    @MockBean
    private SaveReplicationProducerRegistry mockedSaveProducerRegistry;

    @MockBean
    private DeleteReplicationProducerRegistry mockedDeleteProducerRegistry;

    @Autowired
    private FirstTestRepository repository;


}
