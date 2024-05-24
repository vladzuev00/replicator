package by.aurorasoft.testapp.crud.v2.repository;

import by.aurorasoft.testapp.crud.v2.entity.ReplicatedPersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplicatedPersonRepository extends JpaRepository<ReplicatedPersonEntity, Long> {

}
