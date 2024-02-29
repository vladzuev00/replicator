package by.aurorasoft.replicator.model;

import by.aurorasoft.replicator.base.dto.TestDto;
import by.aurorasoft.replicator.model.replication.DeleteReplication;
import by.aurorasoft.replicator.model.replication.Replication;
import by.aurorasoft.replicator.model.replication.SaveReplication;
import by.aurorasoft.replicator.model.replication.UpdateReplication;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static by.aurorasoft.replicator.model.ReplicationType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ReplicationTypeTest {
    private static final TestDto GIVEN_DTO = new TestDto(255L);

    @ParameterizedTest
    @MethodSource("provideTypeAndExpected")
    public void replicationShouldBeCreated(final ReplicationType givenType, final Replication<Long, TestDto> expected) {
        final Replication<Long, TestDto> actual = givenType.createReplication(GIVEN_DTO);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideTypeAndExpected() {
        return Stream.of(
                Arguments.of(SAVE, new SaveReplication<>(GIVEN_DTO)),
                Arguments.of(UPDATE, new UpdateReplication<>(GIVEN_DTO)),
                Arguments.of(DELETE, new DeleteReplication<>(GIVEN_DTO))
        );
    }
}
