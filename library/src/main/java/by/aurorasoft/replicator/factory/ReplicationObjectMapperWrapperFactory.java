package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.mapperwrapper.ReplicationObjectMapperWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitorjbl.json.JsonViewModule;
import org.springframework.stereotype.Component;

@Component
public final class ReplicationObjectMapperWrapperFactory {

    public ReplicationObjectMapperWrapper create(ObjectMapper source) {
        ObjectMapper mapper = new ObjectMapper(source) {
        };
        mapper.registerModule(new JsonViewModule());
        return new ReplicationObjectMapperWrapper(mapper);
    }
}
