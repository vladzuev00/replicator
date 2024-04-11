package by.aurorasoft.testapp.base;

import by.aurorasoft.testapp.base.containerinitializer.DBContainerInitializer;
import by.aurorasoft.testapp.base.containerinitializer.KafkaContainerInitializer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.BeforeClass;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static java.time.ZoneOffset.UTC;
import static java.util.TimeZone.getTimeZone;
import static java.util.TimeZone.setDefault;

@SpringBootTest
@ContextConfiguration(initializers = {DBContainerInitializer.class, KafkaContainerInitializer.class})
public abstract class AbstractSpringBootTest {

    @PersistenceContext
    protected EntityManager entityManager;

    @BeforeClass
    public static void setDefaultTimeZone() {
        setDefault(getTimeZone(UTC));
    }
}
