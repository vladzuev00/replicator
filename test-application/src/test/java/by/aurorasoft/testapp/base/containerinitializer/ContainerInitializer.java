package by.aurorasoft.testapp.base.containerinitializer;

import lombok.Value;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public abstract class ContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public final void initialize(final ConfigurableApplicationContext context) {
        TestPropertyValues.of(getPropertyValuesByKeys()).applyTo(context.getEnvironment());
    }

    protected abstract Stream<TestProperty> getProperties();

    private Map<String, String> getPropertyValuesByKeys() {
        return getProperties().collect(toMap(TestProperty::getKey, TestProperty::getValue));
    }

    @Value
    protected static class TestProperty {
        String key;
        String value;
    }
}
