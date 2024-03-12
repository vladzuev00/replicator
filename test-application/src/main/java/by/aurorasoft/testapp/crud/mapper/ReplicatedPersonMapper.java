package by.aurorasoft.testapp.crud.mapper;

import by.aurorasoft.testapp.crud.dto.ReplicatedPerson;
import by.aurorasoft.testapp.crud.entity.ReplicatedPersonEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
import jakarta.persistence.EntityManager;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class ReplicatedPersonMapper extends AbsMapperEntityDto<ReplicatedPersonEntity, ReplicatedPerson> {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
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

    //TODO: remove
    protected Converter<ReplicatedPerson, ReplicatedPersonEntity> createConverterDtoToEntity() {
        return (context) -> {
            final ReplicatedPerson source = context.getSource();
            final ReplicatedPersonEntity destination = context.getDestination();
            mapSpecificFields(source, destination);
            return destination;
        };
    }
}
