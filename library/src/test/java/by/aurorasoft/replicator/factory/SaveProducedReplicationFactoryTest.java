//package by.aurorasoft.replicator.factory;
//
//import by.aurorasoft.replicator.annotation.ReplicatedRepository.EntityView;
//import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
//import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
//import by.aurorasoft.replicator.model.view.EntityJsonView;
//import by.aurorasoft.replicator.testentity.TestEntity;
//import by.aurorasoft.replicator.testrepository.SecondTestRepository;
//import org.aspectj.lang.JoinPoint;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
//import static by.aurorasoft.replicator.testutil.ViewConfigUtil.checkEquals;
//import static by.aurorasoft.replicator.testutil.ViewConfigUtil.createViewConfig;
//import static java.util.stream.IntStream.range;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertSame;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public final class SaveProducedReplicationFactoryTest {
//    private static final String FIELD_NAME_REPLICATION_BODY = "body";
//
//    @Mock
//    private EntityJsonViewFactory mockedEntityJsonViewFactory;
//
//    private SaveProducedReplicationFactory replicationFactory;
//
//    @Captor
//    private ArgumentCaptor<EntityView[]> entityViewConfigsArgumentCaptor;
//
//    @BeforeEach
//    public void initializeReplicationFactory() {
//        replicationFactory = new SaveProducedReplicationFactory(mockedEntityJsonViewFactory);
//    }
//
//    @Test
//    public void replicationShouldBeCreated() {
//        TestEntity givenSavedEntity = new TestEntity();
//        JoinPoint givenJoinPoint = mock(JoinPoint.class);
//
//        Object givenTarget = new SecondTestRepository();
//        when(givenJoinPoint.getTarget()).thenReturn(givenTarget);
//
//        @SuppressWarnings("unchecked") EntityJsonView<TestEntity> givenEntityJsonView = mock(EntityJsonView.class);
//        when(mockedEntityJsonViewFactory.create(same(givenSavedEntity), any(EntityView[].class)))
//                .thenReturn(givenEntityJsonView);
//
//        SaveProducedReplication actual = replicationFactory.create(givenSavedEntity, givenJoinPoint);
//        Object actualBody = getBody(actual);
//        assertSame(givenEntityJsonView, actualBody);
//
//        EntityView[] expectedEntityViewConfigs = new EntityView[]{
//                createViewConfig(TestEntity.class, new String[]{"first-field"}),
//                createViewConfig(Object.class, new String[]{"second-field", "third-field", "fourth-field"})
//        };
//        verifyEntityViewConfigs(expectedEntityViewConfigs);
//    }
//
//    private Object getBody(ProducedReplication<?> replication) {
//        return getFieldValue(replication, FIELD_NAME_REPLICATION_BODY, Object.class);
//    }
//
//    private void verifyEntityViewConfigs(EntityView[] expected) {
//        verify(mockedEntityJsonViewFactory, times(1)).create(any(), entityViewConfigsArgumentCaptor.capture());
//        EntityView[] actual = entityViewConfigsArgumentCaptor.getValue();
//        assertEquals(expected.length, actual.length);
//        range(0, expected.length).forEach(i -> checkEquals(expected[i], actual[i]));
//    }
//}
