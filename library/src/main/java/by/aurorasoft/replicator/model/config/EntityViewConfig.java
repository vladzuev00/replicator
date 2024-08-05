package by.aurorasoft.replicator.model.config;

import lombok.Value;

@Value
public class EntityViewConfig {
    Class<?> type;
    String[] excludedFields;
}
