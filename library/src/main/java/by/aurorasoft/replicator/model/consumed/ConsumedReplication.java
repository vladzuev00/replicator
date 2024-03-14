package by.aurorasoft.replicator.model.consumed;

import by.aurorasoft.replicator.model.Replication;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

import static by.aurorasoft.replicator.util.TransportNameUtil.*;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, property = TYPE_PROPERTY)
@JsonSubTypes(
        {
                @Type(value = SaveConsumedReplication.class, name = SAVE),
                @Type(value = DeleteConsumedReplication.class, name = DELETE)
        }
)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class ConsumedReplication<ID, E extends AbstractEntity<ID>> extends Replication {

    public ConsumedReplication(final UUID uuid) {
        super(uuid);
    }

    public abstract void execute(final JpaRepository<E, ID> repository);
}
