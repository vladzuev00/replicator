package by.aurorasoft.testapp.crud.mapper;

import by.aurorasoft.testapp.crud.dto.ReplicatedPerson;
import by.aurorasoft.testapp.crud.entity.ReplicatedPersonEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class ReplicatedPersonMapper extends AbsMapperEntityDto<ReplicatedPersonEntity, ReplicatedPerson> {

    public ReplicatedPersonMapper(final ModelMapper modelMapper, final EntityManager entityManager) {
        super(modelMapper, entityManager, ReplicatedPersonEntity.class, ReplicatedPerson.class);
    }

    @Override
    protected ReplicatedPerson create(final ReplicatedPersonEntity entity) {
        return new ReplicatedPerson(
                entity.getId(),
                entity.getName(),
                entity.getSurname(),
                entity.getBirthDate()
        );
    }
}
