package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.model.setting.ReplicationComponentSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public final class ReplicationComponentUniqueRepositoryValidator extends ReplicationComponentUniquePropertyValidator<JpaRepository<?, ?>> {
    private static final String VIOLATION_MESSAGE = "Duplicated repositories were found";

    public ReplicationComponentUniqueRepositoryValidator() {
        super(VIOLATION_MESSAGE);
    }

    @Override
    protected <S extends ReplicationComponentSetting<?, ?>> JpaRepository<?, ?> getProperty(S setting) {
        return setting.getRepository();
    }
}
