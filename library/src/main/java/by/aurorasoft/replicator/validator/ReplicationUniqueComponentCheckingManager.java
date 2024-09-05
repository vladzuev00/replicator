package by.aurorasoft.replicator.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class ReplicationUniqueComponentCheckingManager {
    private final List<ReplicationUniqueComponentChecker<?>> checkers;

//    public <S extends ReplicationSetting<?, ?>> void check(List<S> settings) {
//        checkers.forEach(checker -> checker.check(settings));
//    }
}
