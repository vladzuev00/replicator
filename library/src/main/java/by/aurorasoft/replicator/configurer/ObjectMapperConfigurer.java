package by.aurorasoft.replicator.configurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitorjbl.json.JsonViewModule;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public final class ObjectMapperConfigurer {

    @EventListener(ContextRefreshedEvent.class)
    public void configure(ObjectMapper mapper) {
        mapper.registerModule(new JsonViewModule());
    }
}
