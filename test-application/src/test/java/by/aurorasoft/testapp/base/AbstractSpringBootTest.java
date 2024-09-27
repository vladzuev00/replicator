package by.aurorasoft.testapp.base;

import by.aurorasoft.testapp.ApplicationRunner;
import by.aurorasoft.testapp.base.containerinitializer.DataBaseContainerInitializer;
import by.aurorasoft.testapp.base.containerinitializer.KafkaContainerInitializer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static java.time.ZoneOffset.UTC;
import static java.util.TimeZone.getTimeZone;
import static java.util.TimeZone.setDefault;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@SpringBootTest
//@Transactional(propagation = NOT_SUPPORTED)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@ContextConfiguration(initializers = {DataBaseContainerInitializer.class, KafkaContainerInitializer.class})
public abstract class AbstractSpringBootTest {

    @PersistenceContext
    protected EntityManager entityManager;

    @BeforeAll
    public static void setDefaultTimeZone() {
        setDefault(getTimeZone(UTC));
    }
}
