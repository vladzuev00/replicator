package by.aurorasoft.testapp.crud.repository;

import by.aurorasoft.testapp.crud.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<PersonEntity, Long> {

}
