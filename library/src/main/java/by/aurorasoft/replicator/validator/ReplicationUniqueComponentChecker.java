package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.model.setting.ReplicationSetting;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

@RequiredArgsConstructor
public abstract class ReplicationUniqueComponentChecker<P> {
    private final String violationMessage;

    public final <S extends ReplicationSetting<?, ?>> void check(List<S> settings) {
        if (!findDuplicatedProperties(settings).isEmpty()) {
            throw new IllegalStateException(violationMessage);
        }
    }

    protected abstract <S extends ReplicationSetting<?, ?>> P getProperty(S setting);

    private <S extends ReplicationSetting<?, ?>> Set<P> findDuplicatedProperties(List<S> settings) {
        return settings.stream()
                .map(this::getProperty)
                .collect(groupingBy(identity(), LinkedHashMap::new, counting()))
                .entrySet()
                .stream()
                .filter(frequencyByProperty -> frequencyByProperty.getValue() > 1)
                .map(Entry::getKey)
                .collect(toCollection(LinkedHashSet::new));
    }
}
