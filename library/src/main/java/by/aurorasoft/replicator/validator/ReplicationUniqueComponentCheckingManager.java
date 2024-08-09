package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.model.setting.ReplicationSetting;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public final class ReplicationUniqueComponentCheckingManager {
    private final List<ReplicationUniqueComponentChecker<?>> checkers;

    public <S extends ReplicationSetting<?, ?>> void check(List<S> settings) {
        checkers.forEach(checker -> checker.check(settings));
    }
}
