package by.aurorasoft.replicator.model.replication.produced;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import static by.aurorasoft.replicator.util.TransportNameUtil.*;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, property = TYPE)
@JsonSubTypes(
        {
                @Type(value = SaveProducedReplication.class, name = SAVE),
                @Type(value = DeleteProducedReplication.class, name = DELETE)
        }
)
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public abstract class ProducedReplication<B> {

    @JsonProperty(BODY)
    private final B body;

    //TODO: getEntityId
}
