package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.objectmapper.ReplicationObjectMapperWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.Test;
import org.springframework.data.geo.GeoModule;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


public final class ReplicationObjectMapperWrapperFactoryTest {
    private final ReplicationObjectMapperWrapperFactory factory = new ReplicationObjectMapperWrapperFactory();

    @Test
    public void wrapperShouldBeCreated() {
        ObjectMapper givenSource = new ObjectMapper()
                .registerModule(new GeoModule())
                .registerModule(new SimpleModule());

        ReplicationObjectMapperWrapper actual = factory.create(givenSource);
        Set<Object> actualModuleIds = actual.getMapper().getRegisteredModuleIds();
        Set<Object> expectedModuleIds = Set.of("Spring Data Geo Mixins", "SimpleModule-1", "json-view");
        assertEquals(expectedModuleIds, actualModuleIds);
    }
}
