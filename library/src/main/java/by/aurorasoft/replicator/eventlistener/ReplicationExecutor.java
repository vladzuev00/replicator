package by.aurorasoft.replicator.eventlistener;

import by.aurorasoft.replicator.event.ReplicationEvent;
import by.aurorasoft.replicator.model.consumed.ConsumedReplication;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public final class ReplicationExecutor {

    @EventListener(ReplicationEvent.class)
    public <ID, E extends AbstractEntity<ID>> void execute(final ReplicationEvent<ID, E> event) {
        final ConsumedReplication<ID, E> replication = event.getReplication();
        final JpaRepository<E, ID> repository = event.getRepository();
        replication.execute(repository);
    }
}
