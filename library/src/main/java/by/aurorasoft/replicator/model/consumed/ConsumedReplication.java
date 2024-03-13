package by.aurorasoft.replicator.model.consumed;

import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import static by.aurorasoft.replicator.util.TransportConfigUtil.*;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, property = TYPE_PROPERTY)
@JsonSubTypes(
        {
                @Type(value = SaveConsumedReplication.class, name = SAVE),
                @Type(value = DeleteConsumedReplication.class, name = DELETE)
        }
)
public interface ConsumedReplication<ID, E extends AbstractEntity<ID>> {
    void execute(final JpaRepository<E, ID> repository);
}
