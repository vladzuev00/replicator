package by.aurorasoft.replicator.configurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitorjbl.json.JsonViewModule;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ObjectMapperConfigurer {
    private final ObjectMapper mapper;

    //TODO: не успевает скорее всего до ReplicationKafkaTemplateFactory
    @EventListener(ContextRefreshedEvent.class)
    public void configure() {
        mapper.registerModule(new JsonViewModule());
    }
}
