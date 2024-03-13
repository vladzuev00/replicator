package by.aurorasoft.replicator.model.produced;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, property = "type")
@JsonSubTypes(
        {
                @Type(value = SaveProducedReplication.class, name = "save"),
                @Type(value = DeleteProducedReplication.class, name = "delete")
        }
)
public interface ProducedReplication<ID> {
    ID getEntityId();
}
