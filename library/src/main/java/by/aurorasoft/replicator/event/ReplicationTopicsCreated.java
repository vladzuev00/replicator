package by.aurorasoft.replicator.event;

import by.aurorasoft.replicator.topiccreator.ReplicationTopicCreator;
import org.springframework.context.ApplicationEvent;

public final class ReplicationTopicsCreated extends ApplicationEvent {

    public ReplicationTopicsCreated(final ReplicationTopicCreator source) {
        super(source);
    }
}
