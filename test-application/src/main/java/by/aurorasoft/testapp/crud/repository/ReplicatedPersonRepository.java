package by.aurorasoft.testapp.crud.repository;

import by.aurorasoft.testapp.crud.entity.ReplicatedPersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplicatedPersonRepository extends JpaRepository<ReplicatedPersonEntity, Long> {

}
