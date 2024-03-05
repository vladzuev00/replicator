package by.aurorasoft.testapp.base;

import by.aurorasoft.testapp.base.containerinitializer.DBContainerInitializer;
import by.aurorasoft.testapp.base.containerinitializer.KafkaContainerInitializer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static java.time.ZoneOffset.UTC;
import static java.util.TimeZone.getTimeZone;
import static java.util.TimeZone.setDefault;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = {DBContainerInitializer.class, KafkaContainerInitializer.class})
public abstract class AbstractSpringBootTest {

    @PersistenceContext
    @SuppressWarnings("unused")
    protected EntityManager entityManager;

    @BeforeClass
    public static void setDefaultTimeZone() {
        setDefault(getTimeZone(UTC));
    }
}
