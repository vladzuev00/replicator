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
public abstract class ReplicationComponentUniquePropertyValidator<S extends ReplicationComponentSetting<?, ?>, P> {
    private final String violationMessage;

    public final void validate(List<S> settings) {
        Set<P> duplicatedProperties = findDuplicatedProperties(settings);
        if (!duplicatedProperties.isEmpty()) {
            throw new IllegalStateException(violationMessage);
        }
    }

    protected abstract P getProperty(S setting);

    private Set<P> findDuplicatedProperties(List<S> settings) {
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
