package by.aurorasoft.replicator.event;

import by.aurorasoft.replicator.topiccreator.ReplicationTopicCreator;
import org.springframework.context.ApplicationEvent;

public final class ReplicationTopicsCreatedEvent extends ApplicationEvent {

    public ReplicationTopicsCreatedEvent(ReplicationTopicCreator source) {
        super(source);
    }
}
