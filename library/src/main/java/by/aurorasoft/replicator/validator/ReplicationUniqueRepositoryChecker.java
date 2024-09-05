//package by.aurorasoft.replicator.validator;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Component;
//
//@Component
//public final class ReplicationUniqueRepositoryChecker extends ReplicationUniqueComponentChecker<JpaRepository<?, ?>> {
//    private static final String VIOLATION_MESSAGE = "Duplicated repositories were found";
//
//    public ReplicationUniqueRepositoryChecker() {
//        super(VIOLATION_MESSAGE);
//    }
//
//    @Override
//    protected <S extends ReplicationSetting<?, ?>> JpaRepository<?, ?> getProperty(S setting) {
//        return setting.getRepository();
//    }
//}
