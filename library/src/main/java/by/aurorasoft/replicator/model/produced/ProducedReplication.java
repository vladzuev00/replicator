package by.aurorasoft.replicator.model.produced;

import by.aurorasoft.replicator.model.Replication;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.UUID;

import static by.aurorasoft.replicator.util.TransportNameUtil.*;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, property = TYPE_PROPERTY)
@JsonSubTypes(
        {
                @Type(value = SaveProducedReplication.class, name = SAVE),
                @Type(value = DeleteProducedReplication.class, name = DELETE)
        }
)
public abstract class ProducedReplication<ID> extends Replication {

    public ProducedReplication(final UUID uuid) {
        super(uuid);
    }

    public abstract ID getEntityId();
}
