package by.aurorasoft.replicator.model.replication.produced;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
@ToString
public abstract class ProducedReplication {

    @JsonProperty(BODY)
    private final Object body;

    @JsonIgnore
    public final Object getEntityId() {
        return getEntityId(body);
    }

    protected abstract Object getEntityId(final Object body);
}
