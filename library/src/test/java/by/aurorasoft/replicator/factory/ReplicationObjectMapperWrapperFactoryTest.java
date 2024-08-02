package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.mapperwrapper.ReplicationObjectMapperWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.data.web.config.SpringDataJacksonConfiguration.PageModule;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public final class ReplicationObjectMapperWrapperFactoryTest {
    private final ReplicationObjectMapperWrapperFactory factory = new ReplicationObjectMapperWrapperFactory();

    @Test
    public void wrapperShouldBeCreated() {
        ObjectMapper givenSource = new ObjectMapper().registerModule(new PageModule());

        ReplicationObjectMapperWrapper actual = factory.create(givenSource);
        Set<Object> actualModuleIds = actual.getMapper().getRegisteredModuleIds();
        Set<Object> expectedModuleIds = Set.of(
                "org.springframework.data.web.config.SpringDataJacksonConfiguration$PageModule",
                "json-view"
        );
        assertEquals(expectedModuleIds, actualModuleIds);

        ObjectMapper actualMapper = actual.getMapper();
        assertNotSame(givenSource, actualMapper);
    }
}
