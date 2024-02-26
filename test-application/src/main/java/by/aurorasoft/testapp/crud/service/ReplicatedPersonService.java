package by.aurorasoft.testapp.crud.service;

import by.aurorasoft.testapp.crud.dto.ReplicatedPerson;
import by.aurorasoft.testapp.crud.entity.ReplicatedPersonEntity;
import by.aurorasoft.testapp.crud.mapper.ReplicatedPersonMapper;
import by.aurorasoft.testapp.crud.repository.ReplicatedPersonRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.springframework.stereotype.Service;

@Service
public class ReplicatedPersonService extends AbsServiceCRUD<Long, ReplicatedPersonEntity, ReplicatedPerson, ReplicatedPersonRepository> {

    public ReplicatedPersonService(final ReplicatedPersonMapper mapper, final ReplicatedPersonRepository repository) {
        super(mapper, repository);
    }
}
