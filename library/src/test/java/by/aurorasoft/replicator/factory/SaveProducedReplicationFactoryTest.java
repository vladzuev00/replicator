package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedService.ViewConfig;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import by.aurorasoft.replicator.model.view.DtoJsonView;
import by.aurorasoft.replicator.v2.dto.TestV2Dto;
import by.aurorasoft.replicator.v2.service.SecondTestV2CRUDService;
import org.aspectj.lang.JoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
import static by.aurorasoft.replicator.testutil.ViewConfigUtil.checkEquals;
import static by.aurorasoft.replicator.testutil.ViewConfigUtil.createViewConfig;
import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class SaveProducedReplicationFactoryTest {
    private static final String FIELD_NAME_REPLICATION_BODY = "body";

    @Mock
    private DtoJsonViewFactory mockedDtoJsonViewFactory;

    private SaveProducedReplicationFactory replicationFactory;

    @Captor
    private ArgumentCaptor<ViewConfig[]> viewConfigsArgumentCaptor;

    @Before
    public void initializeReplicationFactory() {
        replicationFactory = new SaveProducedReplicationFactory(mockedDtoJsonViewFactory);
    }

    @Test
    public void replicationShouldBeCreated() {
        Object givenSavedDto = new Object();
        JoinPoint givenJoinPoint = mock(JoinPoint.class);

        Object givenTarget = new SecondTestV2CRUDService();
        when(givenJoinPoint.getTarget()).thenReturn(givenTarget);

        DtoJsonView<Object> givenDtoJsonView = new DtoJsonView<>(givenSavedDto);
        when(mockedDtoJsonViewFactory.create(same(givenSavedDto), any(ViewConfig[].class)))
                .thenReturn(givenDtoJsonView);

        SaveProducedReplication actual = replicationFactory.create(givenSavedDto, givenJoinPoint);
        Object actualBody = getBody(actual);
        assertSame(givenDtoJsonView, actualBody);

        ViewConfig[] expectedViewConfigs = new ViewConfig[]{
                createViewConfig(
                        TestV2Dto.class,
                        new String[]{"first-field"},
                        new String[]{"second-field"}
                ),
                createViewConfig(
                        TestV2Dto.class,
                        new String[]{"third-field", "fourth-field"},
                        new String[]{"fifth-field", "sixth-field", "seventh-field"}
                )
        };
        verifyViewConfigs(expectedViewConfigs);
    }

    private static Object getBody(ProducedReplication<?> replication) {
        return getFieldValue(replication, FIELD_NAME_REPLICATION_BODY, Object.class);
    }

    private void verifyViewConfigs(ViewConfig[] expected) {
        verify(mockedDtoJsonViewFactory, times(1)).create(any(), viewConfigsArgumentCaptor.capture());
        ViewConfig[] actual = viewConfigsArgumentCaptor.getValue();
        assertEquals(expected.length, actual.length);
        range(0, expected.length).forEach(i -> checkEquals(expected[i], actual[i]));
    }
}
