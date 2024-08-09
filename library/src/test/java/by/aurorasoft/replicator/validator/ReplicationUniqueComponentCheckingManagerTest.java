package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.model.setting.ReplicationSetting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ReplicationUniqueComponentCheckingManagerTest {

    @Mock
    private ReplicationUniqueComponentChecker<?> firstMockedChecker;

    @Mock
    private ReplicationUniqueComponentChecker<?> secondMockedChecker;

    @Mock
    private ReplicationUniqueComponentChecker<?> thirdMockedChecker;

    private ReplicationUniqueComponentCheckingManager manager;

    @BeforeEach
    public void initializeManager() {
        manager = new ReplicationUniqueComponentCheckingManager(
                List.of(firstMockedChecker, secondMockedChecker, thirdMockedChecker)
        );
    }

    @Test
    public void settingsShouldBeChecked() {
        @SuppressWarnings("unchecked") List<ReplicationSetting<?, ?>> givenSettings = mock(List.class);

        manager.check(givenSettings);

        verify(firstMockedChecker, times(1)).check(same(givenSettings));
        verify(secondMockedChecker, times(1)).check(same(givenSettings));
        verify(thirdMockedChecker, times(1)).check(same(givenSettings));
    }
}
