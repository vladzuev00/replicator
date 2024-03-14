package by.aurorasoft.replicator.util;

import by.aurorasoft.replicator.base.dto.TestDto;
import by.aurorasoft.replicator.model.Replication;
import by.aurorasoft.replicator.model.produced.DeleteProducedReplication;
import by.aurorasoft.replicator.model.produced.ProducedReplication;
import by.aurorasoft.replicator.model.produced.SaveProducedReplication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.experimental.UtilityClass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@UtilityClass
public final class ReplicationAssertUtil {

    @SuppressWarnings("unchecked")
    public static void assertSaveProducedReplication(final ProducedReplication<Long> actual,
                                                     final TestDto expectedDto) {
        final var actualSaveReplication = assertTypeThenCast(actual, SaveProducedReplication.class);
        final AbstractDto<?> actualDto = actualSaveReplication.getDto();
        assertEquals(expectedDto, actualDto);
    }

    @SuppressWarnings("unchecked")
    public static void assertDeleteProducedReplication(final ProducedReplication<Long> actual,
                                                       final Long expectedEntityId) {
        final var actualDeleteReplication = assertTypeThenCast(actual, DeleteProducedReplication.class);
        final Object actualEntityId = actualDeleteReplication.getEntityId();
        assertEquals(expectedEntityId, actualEntityId);
    }

    private static <R extends ProducedReplication<Long>> R assertTypeThenCast(final Replication actual,
                                                                              final Class<R> expectedType) {
        assertTrue(expectedType.isInstance(actual));
        return expectedType.cast(actual);
    }
}
