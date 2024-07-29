package by.aurorasoft.replicator.objectmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitorjbl.json.JsonViewModule;
import org.springframework.stereotype.Component;

@Component
public final class ReplicationObjectMapperFactory {

    public ReplicationObjectMapper create(ObjectMapper source) {
        ReplicationObjectMapper mapper = new ReplicationObjectMapper(source);
        mapper.registerModule(new JsonViewModule());
        return mapper;
    }
}
