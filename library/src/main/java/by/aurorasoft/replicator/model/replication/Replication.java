package by.aurorasoft.replicator.model.replication;

import by.aurorasoft.replicator.model.ReplicationType;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.CLASS;

@JsonTypeInfo(use = CLASS, property = "@class")
@JsonSubTypes(
        {
                @Type(value = SaveReplication.class, name = "SaveReplication"),
                @Type(value = UpdateReplication.class, name = "UpdateReplication"),
                @Type(value = DeleteReplication.class, name = "DeleteReplication"),
        }
)
public interface Replication<ID, DTO extends AbstractDto<ID>> {
    ID getEntityId();

    void execute(final AbsServiceCRUD<ID, ?, DTO, ?> service);

    ReplicationType getType();
}
