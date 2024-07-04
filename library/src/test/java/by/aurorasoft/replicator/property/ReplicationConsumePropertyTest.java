package by.aurorasoft.replicator.property;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.Assert.assertTrue;

//public final class ReplicationConsumePropertyTest extends AbstractSpringBootTest {
//
////    @Autowired
////    private Validator validator;
//
//    @Test
//    public void propertyShouldBeValid() {
//        ReplicationConsumeProperty givenProperty = new ReplicationConsumeProperty("test-topic", "test-id") {
//        };
//
//        Set<ConstraintViolation<ReplicationConsumeProperty>> violations = validator.validate(givenProperty);
//        assertTrue(violations.isEmpty());
//    }
//}
