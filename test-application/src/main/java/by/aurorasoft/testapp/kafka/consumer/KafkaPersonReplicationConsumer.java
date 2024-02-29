package by.aurorasoft.testapp.kafka.consumer;

import by.aurorasoft.replicator.consumer.KafkaReplicationConsumer;
import by.aurorasoft.replicator.model.TransportableReplication;
import by.aurorasoft.testapp.crud.dto.ReplicatedPerson;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeoutException;

import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.IntStream.rangeClosed;

//TODO: refactor if it is needed because of consumer consume only one record
@Component
public final class KafkaPersonReplicationConsumer extends KafkaReplicationConsumer<Long, ReplicatedPerson> {
    private static final int WAIT_CONSUMING_SECONDS = 5;

    private final Phaser phaser;

    public KafkaPersonReplicationConsumer(final AbsServiceCRUD<Long, ?, ReplicatedPerson, ?> service,
                                          final ObjectMapper objectMapper) {
        super(service, objectMapper, ReplicatedPerson.class);
        phaser = createNotTerminatedPhaser();
    }

    public void setExpectedReplicationCount(final int expectedConsumedRecordCount) {
        rangeClosed(1, phaser.getUnarrivedParties()).forEach(i -> phaser.arriveAndDeregister());
        phaser.bulkRegister(expectedConsumedRecordCount + 1);
    }

    @Override
    @KafkaListener(
            topics = "${kafka.topic.sync-person.name}",
            groupId = "${kafka.topic.sync-person.consumer.group-id}",
            containerFactory = "listenerContainerFactorySyncPerson"
    )
    public void listen(final ConsumerRecord<Long, TransportableReplication> record) {
        super.listen(record);
        phaser.arriveAndDeregister();
    }

    public boolean isSuccessConsuming() {
        try {
            final int phaseNumber = phaser.arriveAndDeregister();
            phaser.awaitAdvanceInterruptibly(phaseNumber, WAIT_CONSUMING_SECONDS, SECONDS);
            return true;
        } catch (final InterruptedException exception) {
            currentThread().interrupt();
            return false;
        } catch (final TimeoutException exception) {
            return false;
        }
    }

    private static Phaser createNotTerminatedPhaser() {
        return new Phaser() {

            @Override
            protected boolean onAdvance(final int phase, final int registeredParties) {
                return false;
            }
        };
    }
}
