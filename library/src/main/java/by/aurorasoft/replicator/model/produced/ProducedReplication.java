package by.aurorasoft.replicator.model.produced;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static by.aurorasoft.replicator.util.TransportNameUtil.*;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, property = TYPE_PROPERTY)
@JsonSubTypes(
        {
                @Type(value = SaveProducedReplication.class, name = SAVE),
                @Type(value = DeleteProducedReplication.class, name = DELETE)
        }
)
public interface ProducedReplication<ID> {
    ID getEntityId();
}
