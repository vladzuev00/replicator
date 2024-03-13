package by.aurorasoft.replicator.model.produced;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static by.aurorasoft.replicator.util.TransportConfigUtil.*;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, property = TYPE_PROPERTY_NAME)
@JsonSubTypes(
        {
                @Type(value = SaveProducedReplication.class, name = SAVE_TYPE_NAME),
                @Type(value = DeleteProducedReplication.class, name = DELETE_TYPE_NAME)
        }
)
public interface ProducedReplication<ID> {
    ID getEntityId();
}
