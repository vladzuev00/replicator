package by.aurorasoft.replicator.configurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitorjbl.json.JsonViewModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public final class ObjectMapperConfigurerTest {

    @Mock
    private ObjectMapper mockedMapper;

    private ObjectMapperConfigurer configurer;

    @Captor
    private ArgumentCaptor<JsonViewModule> jsonViewModuleArgumentCaptor;

    @BeforeEach
    public void initializeConfigurer() {
        configurer = new ObjectMapperConfigurer(mockedMapper);
    }

    @Test
    public void mapperShouldBeConfigured() {
        configurer.configure();

        verify(mockedMapper, times(1)).registerModule(jsonViewModuleArgumentCaptor.capture());

        JsonViewModule capturedModule = jsonViewModuleArgumentCaptor.getValue();
        assertNotNull(capturedModule);
    }
}
