package by.aurorasoft.replicator.model;

import by.aurorasoft.replicator.model.replication.DeleteReplication;
import by.aurorasoft.replicator.model.replication.Replication;
import by.aurorasoft.replicator.model.replication.SaveReplication;
import by.aurorasoft.replicator.model.replication.UpdateReplication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;

public enum ReplicationType {
    SAVE {
        @Override
        public <ID, DTO extends AbstractDto<ID>> SaveReplication<ID, DTO> createReplication(final DTO dto) {
            return new SaveReplication<>(dto);
        }
    },

    UPDATE {
        @Override
        public <ID, DTO extends AbstractDto<ID>> UpdateReplication<ID, DTO> createReplication(final DTO dto) {
            return new UpdateReplication<>(dto);
        }
    },

    DELETE {
        @Override
        public <ID, DTO extends AbstractDto<ID>> DeleteReplication<ID, DTO> createReplication(final DTO dto) {
//            return new DeleteReplication<>(dto);
            return null;
        }
    };

    public abstract <ID, DTO extends AbstractDto<ID>> Replication<ID, DTO> createReplication(final DTO dto);
}
