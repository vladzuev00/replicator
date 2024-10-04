package by.aurorasoft.testapp.crud.repository;

import by.aurorasoft.testapp.crud.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PersonRepository extends JpaRepository<PersonEntity, Long> {

    @Override
    @Modifying
    @Query("DELETE FROM PersonEntity e WHERE e.id = :id")
    void deleteById(@SuppressWarnings("NullableProblems") Long id);
}
