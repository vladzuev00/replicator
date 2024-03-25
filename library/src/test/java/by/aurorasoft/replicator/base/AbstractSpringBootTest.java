package by.aurorasoft.replicator.base;

import by.aurorasoft.replicator.base.config.TestConfig;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public abstract class AbstractSpringBootTest {

}
