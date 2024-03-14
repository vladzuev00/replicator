package by.aurorasoft.replicator.event;

import by.aurorasoft.replicator.consuming.consumer.ReplicationConsumer;
import by.aurorasoft.replicator.model.consumed.ConsumedReplication;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.data.jpa.repository.JpaRepository;

@Getter
public final class ReplicationEvent<ID, E extends AbstractEntity<ID>> extends ApplicationEvent {
    private final ConsumedReplication<ID, E> replication;

    public ReplicationEvent(final ReplicationConsumer<ID, E> consumer, final ConsumedReplication<ID, E> replication) {
        super(consumer);
        this.replication = replication;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ReplicationConsumer<ID, E> getSource() {
        return (ReplicationConsumer<ID, E>) super.getSource();
    }

    public JpaRepository<E, ID> getRepository() {
        return getSource().getRepository();
    }
}
