package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.model.setting.ReplicationComponentSetting;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

@RequiredArgsConstructor
public abstract class ReplicationComponentUniquePropertyValidator<P> {
    private final String violationMessage;

    public final <S extends ReplicationComponentSetting<?, ?>> void validate(List<S> settings) {
        if (!findDuplicatedProperties(settings).isEmpty()) {
            throw new IllegalStateException(violationMessage);
        }
    }

    protected abstract <S extends ReplicationComponentSetting<?, ?>> P getProperty(S setting);

    private <S extends ReplicationComponentSetting<?, ?>> Set<P> findDuplicatedProperties(List<S> settings) {
        return settings.stream()
                .map(this::getProperty)
                .collect(groupingBy(identity(), LinkedHashMap::new, counting()))
                .entrySet()
                .stream()
                .filter(frequencyByTopic -> frequencyByTopic.getValue() > 1)
                .map(Entry::getKey)
                .collect(toCollection(LinkedHashSet::new));
    }
}
