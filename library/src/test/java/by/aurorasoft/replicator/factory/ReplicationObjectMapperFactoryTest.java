package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.objectmapper.ReplicationObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Test;
import org.springframework.data.geo.GeoModule;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public final class ReplicationObjectMapperFactoryTest {
    private final ReplicationObjectMapperFactory factory = new ReplicationObjectMapperFactory();

    @Test
    public void mapperShouldBeCreated() {
        ObjectMapper givenSource = new ObjectMapper()
                .registerModule(new GeoModule())
                .registerModule(new SimpleModule());

        ReplicationObjectMapper actual = factory.create(givenSource);
        Set<Object> actualModuleIds = actual.getRegisteredModuleIds();
        Set<Object> expectedModuleIds = Set.of("Spring Data Geo Mixins", "SimpleModule-1", "json-view");
        assertEquals(expectedModuleIds, actualModuleIds);
    }
}
