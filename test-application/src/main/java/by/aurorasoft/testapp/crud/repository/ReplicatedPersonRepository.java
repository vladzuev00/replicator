package by.aurorasoft.testapp.crud.repository;

import by.aurorasoft.testapp.crud.entity.ReplicatedPersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReplicatedPersonRepository extends JpaRepository<ReplicatedPersonEntity, Long> {

    @Override
    @Modifying
    @Query("DELETE FROM ReplicatedPersonEntity e WHERE e.id = :id")
    void deleteById(@SuppressWarnings("NullableProblems") Long id);
}
