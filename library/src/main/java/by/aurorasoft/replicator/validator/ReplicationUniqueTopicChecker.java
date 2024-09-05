//package by.aurorasoft.replicator.validator;
//
//import org.springframework.stereotype.Component;
//
//@Component
//public final class ReplicationUniqueTopicChecker extends ReplicationUniqueComponentChecker<String> {
//    private static final String VIOLATION_MESSAGE = "Duplicated topics were found";
//
//    public ReplicationUniqueTopicChecker() {
//        super(VIOLATION_MESSAGE);
//    }
//
//    @Override
//    protected <S extends ReplicationSetting<?, ?>> String getProperty(S setting) {
//        return setting.getTopic();
//    }
//}
