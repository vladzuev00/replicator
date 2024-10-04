package by.aurorasoft.testapp.crud.repository;

import by.aurorasoft.testapp.crud.entity.ReplicatedAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReplicatedAddressRepository extends JpaRepository<ReplicatedAddressEntity, Long> {

    @Override
    @Modifying
    @Query("DELETE FROM ReplicatedAddressEntity e WHERE e.id = :id")
    void deleteById(@SuppressWarnings("NullableProblems") Long id);
}
