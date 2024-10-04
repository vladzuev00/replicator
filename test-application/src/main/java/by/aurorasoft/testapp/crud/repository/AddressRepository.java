package by.aurorasoft.testapp.crud.repository;

import by.aurorasoft.testapp.crud.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    @Override
    @Modifying
    @Query("DELETE FROM AddressEntity e WHERE e.id = :id")
    void deleteById(@SuppressWarnings("NullableProblems") Long id);
}
