package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.model.setting.ReplicationComponentSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

@Component
public final class ReplicationComponentSettingValidator {

    public <S extends ReplicationComponentSetting<?, ?>> void validate(List<S> settings) {
        throw new UnsupportedOperationException();
        //TODO: check unique repository and topic
    }

    private <S extends ReplicationComponentSetting<?, ?>> Set<String> findDuplicatedTopics(List<S> settings) {
        return findDuplicatedProperties(settings, ReplicationComponentSetting::getTopic);
    }

    private <S extends ReplicationComponentSetting<?, ?>> Set<JpaRepository<?, ?>> findDuplicatedRepositories(List<S> settings) {
        return findDuplicatedProperties(settings, ReplicationComponentSetting::getRepository);
    }

    private <S extends ReplicationComponentSetting<?, ?>, T> Set<T> findDuplicatedProperties(List<S> settings,
                                                                                             Function<S, T> getter) {
        return settings.stream()
                .map(getter)
                .collect(groupingBy(identity(), LinkedHashMap::new, counting()))
                .entrySet()
                .stream()
                .filter(frequencyByTopic -> frequencyByTopic.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(toCollection(LinkedHashSet::new));
    }
}
