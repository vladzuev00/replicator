package by.aurorasoft.replicator.util;

import by.aurorasoft.replicator.base.dto.TestDto;
import by.aurorasoft.replicator.base.entity.TestEntity;
import by.aurorasoft.replicator.model.Replication;
import by.aurorasoft.replicator.model.consumed.ConsumedReplication;
import by.aurorasoft.replicator.model.consumed.DeleteConsumedReplication;
import by.aurorasoft.replicator.model.consumed.SaveConsumedReplication;
import by.aurorasoft.replicator.model.produced.DeleteProducedReplication;
import by.aurorasoft.replicator.model.produced.ProducedReplication;
import by.aurorasoft.replicator.model.produced.SaveProducedReplication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.experimental.UtilityClass;

import java.util.UUID;

import static org.junit.Assert.*;

@UtilityClass
public final class ReplicationAssertUtil {

    public static void assertProducedSave(final ProducedReplication<Long> actual, final TestDto expectedDto) {
        final var actualSaveReplication = assertDefinedUUIDAndTypeThenCast(actual, SaveProducedReplication.class);
        final AbstractDto<?> actualDto = actualSaveReplication.getDto();
        assertEquals(expectedDto, actualDto);
    }

    public static void assertProducedDelete(final ProducedReplication<Long> actual, final Long expectedEntityId) {
        final var actualDeleteReplication = assertDefinedUUIDAndTypeThenCast(actual, DeleteProducedReplication.class);
        assertEquals(expectedEntityId, actualDeleteReplication.getEntityId());
    }

    public static void assertConsumedSave(final ConsumedReplication<Long, TestEntity> actual,
                                          final UUID expectedUUID,
                                          final TestEntity expectedEntity) {
        final var actualSaveReplication = assertUUIDAndTypeThenCast(actual, expectedUUID, SaveConsumedReplication.class);
        assertEquals(expectedEntity, actualSaveReplication.getEntity());
    }

    public static void assertConsumedDelete(final ConsumedReplication<Long, TestEntity> actual,
                                            final UUID expectedUUID,
                                            final Long expectedEntityId) {
        final var actualDeleteReplication = assertUUIDAndTypeThenCast(actual, expectedUUID, DeleteConsumedReplication.class);
        assertEquals(expectedEntityId, actualDeleteReplication.getEntityId());
    }

    private static <R extends Replication> R assertDefinedUUIDAndTypeThenCast(final Replication actual,
                                                                              final Class<R> expectedType) {
        assertNotNull(actual.getUuid());
        return assertTypeThenCast(actual, expectedType);
    }

    private static <R extends Replication> R assertUUIDAndTypeThenCast(final Replication actual,
                                                                       final UUID expectedUUID,
                                                                       final Class<R> expectedType) {
        assertEquals(expectedUUID, actual.getUuid());
        return assertTypeThenCast(actual, expectedType);
    }

    private static <R extends Replication> R assertTypeThenCast(final Replication actual, final Class<R> expectedType) {
        assertTrue(expectedType.isInstance(actual));
        return expectedType.cast(actual);
    }
}
