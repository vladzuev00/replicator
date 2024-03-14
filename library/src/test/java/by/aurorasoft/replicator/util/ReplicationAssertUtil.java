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

    public static void assertSave(final ProducedReplication<Long> actual, final TestDto expectedDto) {
        final var castedActual = assertDefinedUUIDThenCast(actual, SaveProducedReplication.class);
        final AbstractDto<?> actualDto = castedActual.getDto();
        assertEquals(expectedDto, actualDto);
    }

    public static void assertDelete(final ProducedReplication<Long> actual, final Long expectedEntityId) {
        final var castedActual = assertDefinedUUIDThenCast(actual, DeleteProducedReplication.class);
        assertEquals(expectedEntityId, castedActual.getEntityId());
    }

    public static void assertSave(final ConsumedReplication<Long, TestEntity> actual,
                                  final UUID expectedUUID,
                                  final TestEntity expectedEntity) {
        final var castedActual = assertUUIDAndTypeThenCast(actual, expectedUUID, SaveConsumedReplication.class);
        assertEquals(expectedEntity, castedActual.getEntity());
    }

    public static void assertDelete(final ConsumedReplication<Long, TestEntity> actual,
                                    final UUID expectedUUID,
                                    final Long expectedEntityId) {
        final var castedActual = assertUUIDAndTypeThenCast(actual, expectedUUID, DeleteConsumedReplication.class);
        assertEquals(expectedEntityId, castedActual.getEntityId());
    }

    private static <R extends Replication> R assertDefinedUUIDThenCast(final Replication actual,
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
