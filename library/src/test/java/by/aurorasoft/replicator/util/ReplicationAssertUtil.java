package by.aurorasoft.replicator.util;

import by.aurorasoft.replicator.base.dto.TestDto;
import by.aurorasoft.replicator.model.Replication;
import by.aurorasoft.replicator.model.produced.DeleteProducedReplication;
import by.aurorasoft.replicator.model.produced.ProducedReplication;
import by.aurorasoft.replicator.model.produced.SaveProducedReplication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.experimental.UtilityClass;

import static org.junit.Assert.*;

@UtilityClass
public final class ReplicationAssertUtil {

    @SuppressWarnings("unchecked")
    public static void assertProducingSave(final ProducedReplication<Long> actual, final TestDto expectedDto) {
        final var actualSaveReplication = assertTypeAndDefinedUUIDThenCast(actual, SaveProducedReplication.class);
        final AbstractDto<?> actualDto = actualSaveReplication.getDto();
        assertEquals(expectedDto, actualDto);
    }

    @SuppressWarnings("unchecked")
    public static void assertProducingDelete(final ProducedReplication<Long> actual, final Long expectedEntityId) {
        final var actualDeleteReplication = assertTypeAndDefinedUUIDThenCast(actual, DeleteProducedReplication.class);
        final Object actualEntityId = actualDeleteReplication.getEntityId();
        assertEquals(expectedEntityId, actualEntityId);
    }



    private static <R extends ProducedReplication<Long>> R assertTypeAndDefinedUUIDThenCast(final Replication actual,
                                                                                            final Class<R> expectedType) {
        assertTrue(expectedType.isInstance(actual));
        assertNotNull(actual.getUuid());
        return expectedType.cast(actual);
    }
}
