package by.aurorasoft.replicator.eventlistener;

import by.aurorasoft.replicator.event.ReplicationEvent;
import by.aurorasoft.replicator.model.consumed.ConsumedReplication;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.github.benmanes.caffeine.cache.Caffeine.newBuilder;
import static java.util.concurrent.TimeUnit.HOURS;

@Component
public final class ReplicationExecutor {
    //TODO: possible put in other class
    private final Cache<UUID, ConsumedReplication<?, ?>> executedReplicationsByUUIDS;

    public ReplicationExecutor(final long expireInHour) {
        executedReplicationsByUUIDS = newBuilder()
                .expireAfterWrite(expireInHour, HOURS)
                .build();
    }

    @EventListener(ReplicationEvent.class)
    public <ID, E extends AbstractEntity<ID>> void execute(final ReplicationEvent<ID, E> event) {
        final ConsumedReplication<ID, E> replication = event.getReplication();
        final JpaRepository<E, ID> repository = event.getRepository();
        replication.execute(repository);
    }



    private boolean isExecuted(final ConsumedReplication<?, ?> replication) {
        return executedReplicationsByUUIDS.getIfPresent(replication.getUuid()) == null;
    }
}
