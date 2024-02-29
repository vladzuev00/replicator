//package by.aurorasoft.replicator.holder;
//
//import by.aurorasoft.replicator.annotation.ReplicatedService;
//import by.aurorasoft.replicator.base.AbstractSpringBootTest;
//import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
//import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
//import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
//import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
//import org.apache.kafka.common.serialization.LongSerializer;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import static java.util.Objects.requireNonNullElse;
//import static org.junit.Assert.assertEquals;
//import static org.springframework.aop.framework.AopProxyUtils.getSingletonTarget;
//
//public final class ReplicatedServiceHolderTest extends AbstractSpringBootTest {
//
//    @Autowired
//    private TestFirstService firstService;
//
//    @Autowired
//    private TestSecondService secondService;
//
//    @Autowired
//    private TestThirdService thirdService;
//
//    private final TestFourthService fourthService = new TestFourthService();
//
//    @Test
//    public void holderShouldBeCreated() {
//        final ReplicatedServiceHolder actual = new ReplicatedServiceHolder(
//                List.of(
//                        firstService,
//                        secondService,
//                        thirdService,
//                        fourthService
//                )
//        );
//
//        final Set<?> actualServices = new HashSet<>(actual.getServices());
//        final Set<?> expectedServices = Set.of(unProxy(firstService), unProxy(secondService), fourthService);
//        assertEquals(expectedServices, actualServices);
//    }
//
//    private Object unProxy(final Object object) {
//        return requireNonNullElse(getSingletonTarget(object), object);
//    }
//
//    @ReplicatedService(topicName = "first-topic", idSerializer = LongSerializer.class)
//    static class TestFirstService extends AbsServiceRUD<
//            Long,
//            AbstractEntity<Long>,
//            AbstractDto<Long>,
//            AbsMapperEntityDto<AbstractEntity<Long>, AbstractDto<Long>>,
//            JpaRepository<AbstractEntity<Long>, Long>
//            > {
//
//        public TestFirstService() {
//            super(null, null);
//        }
//    }
//
//    @ReplicatedService(topicName = "second-topic", idSerializer = LongSerializer.class)
//    static class TestSecondService extends AbsServiceRUD<
//            Long,
//            AbstractEntity<Long>,
//            AbstractDto<Long>,
//            AbsMapperEntityDto<AbstractEntity<Long>, AbstractDto<Long>>,
//            JpaRepository<AbstractEntity<Long>, Long>
//            > {
//
//        public TestSecondService() {
//            super(null, null);
//        }
//    }
//
//    @Service
//    static class TestThirdService extends AbsServiceRUD<
//            Long,
//            AbstractEntity<Long>,
//            AbstractDto<Long>,
//            AbsMapperEntityDto<AbstractEntity<Long>, AbstractDto<Long>>,
//            JpaRepository<AbstractEntity<Long>, Long>
//            > {
//
//        public TestThirdService() {
//            super(null, null);
//        }
//    }
//
//    @ReplicatedService(topicName = "fourth-topic", idSerializer = LongSerializer.class)
//    static class TestFourthService extends AbsServiceRUD<
//            Long,
//            AbstractEntity<Long>,
//            AbstractDto<Long>,
//            AbsMapperEntityDto<AbstractEntity<Long>, AbstractDto<Long>>,
//            JpaRepository<AbstractEntity<Long>, Long>
//            > {
//
//        public TestFourthService() {
//            super(null, null);
//        }
//    }
//}
