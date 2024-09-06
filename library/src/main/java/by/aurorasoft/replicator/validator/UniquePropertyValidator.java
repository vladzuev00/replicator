package by.aurorasoft.replicator.validator;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

@RequiredArgsConstructor
public abstract class UniquePropertyValidator<S, P> {
    private static final String EXCEPTION_MESSAGE_TEMPLATE = "%s\n\tDuplicates: %s";

    private final String violationMessage;

    public final void validate(Collection<S> sources) {
        Set<P> duplicatedProperties = findDuplicatedProperties(sources);
        if (!duplicatedProperties.isEmpty()) {
            throwViolationException(duplicatedProperties);
        }
    }

    protected abstract P getProperty(S source);

    private Set<P> findDuplicatedProperties(Collection<S> sources) {
        return sources.stream()
                .map(this::getProperty)
                .collect(groupingBy(identity(), LinkedHashMap::new, counting()))
                .entrySet()
                .stream()
                .filter(frequencyByProperty -> frequencyByProperty.getValue() > 1)
                .map(Entry::getKey)
                .collect(toCollection(LinkedHashSet::new));
    }

    private void throwViolationException(Set<P> duplicatedProperties) {
        String message = EXCEPTION_MESSAGE_TEMPLATE.formatted(violationMessage, duplicatedProperties);
        throw new IllegalStateException(message);
    }
}
