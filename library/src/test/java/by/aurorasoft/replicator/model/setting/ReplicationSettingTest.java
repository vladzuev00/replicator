package by.aurorasoft.replicator.model.setting;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public final class ReplicationSettingTest {

    @Test
    public void settingShouldBeCreated() {
        String givenTopic = "test-topic";
        @SuppressWarnings("unchecked") JpaRepository<Object, Object> givenRepository = mock(JpaRepository.class);

        ReplicationSetting<Object, Object> actual = new ReplicationSetting<>(givenTopic, givenRepository) {
        };

        String actualTopic = actual.getTopic();
        assertSame(givenTopic, actualTopic);

        JpaRepository<Object, Object> actualRepository = actual.getRepository();
        assertSame(givenRepository, actualRepository);
    }

    @Test
    public void settingShouldNotBeCreatedBecauseOfTopicIsNull() {
        @SuppressWarnings("unchecked") JpaRepository<Object, Object> givenRepository = mock(JpaRepository.class);

        assertThrows(NullPointerException.class, () -> new ReplicationSetting<>(null, givenRepository) {
        });
    }

    @Test
    public void settingShouldNotBeCreatedBecauseOfRepositoryIsNull() {
        String givenTopic = "test-topic";

        assertThrows(NullPointerException.class, () -> new ReplicationSetting<>(givenTopic, null) {
        });
    }
}
