package by.aurorasoft.replicator.model.setting;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public final class ReplicationSettingTest {

    @Test
    public void settingShouldBeCreated() {
        String givenTopic = "test-topic";
        @SuppressWarnings("unchecked") JpaRepository<Object, Object> givenRepository = mock(JpaRepository.class);

        new ReplicationSetting<>(givenTopic, givenRepository) {};
    }

    @Test
    public void settingShouldNotBeCreatedBecauseOfTopicIsNull() {
        @SuppressWarnings("unchecked") JpaRepository<Object, Object> givenRepository = mock(JpaRepository.class);

        assertThrows(NullPointerException.class, () -> new ReplicationSetting<>(null, givenRepository) {});
    }

    @Test
    public void settingShouldNotBeCreatedBecauseOfRepositoryIsNull() {
        String givenTopic = "test-topic";

        assertThrows(NullPointerException.class, () -> new ReplicationSetting<>(givenTopic, null) {});
    }
}
