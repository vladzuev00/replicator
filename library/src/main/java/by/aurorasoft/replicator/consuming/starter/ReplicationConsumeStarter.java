package by.aurorasoft.replicator.consuming.starter;

import by.aurorasoft.replicator.model.setting.ReplicationConsumerSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class ReplicationConsumeStarter {
    private final List<ReplicationConsumerSetting<?, ?>> consumerSettings;
    private final ReplicationConsumerStarter consumerStarter;

    @EventListener(ContextRefreshedEvent.class)
    public void start() {
        consumerSettings.forEach(consumerStarter::start);
    }
}
