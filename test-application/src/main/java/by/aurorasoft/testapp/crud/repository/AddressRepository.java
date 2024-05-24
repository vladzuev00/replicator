package by.aurorasoft.testapp.crud.repository;

import by.aurorasoft.testapp.crud.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

}
