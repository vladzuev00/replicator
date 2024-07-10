package by.aurorasoft.replicator.base;

import by.aurorasoft.replicator.base.config.TestConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
public abstract class AbstractSpringBootTest {

}
