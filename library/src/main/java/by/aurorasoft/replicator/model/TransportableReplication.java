package by.aurorasoft.replicator.model;

import lombok.*;
import lombok.experimental.FieldNameConstants;

@Value
@AllArgsConstructor
@Builder
@FieldNameConstants
public class TransportableReplication {
    ReplicationType type;
    String dtoJson;
}
