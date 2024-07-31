package by.aurorasoft.replicator.testrepository;

import by.aurorasoft.replicator.testentity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<TestEntity, Long> {

}
