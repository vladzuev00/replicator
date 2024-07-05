package by.aurorasoft.testapp.base.containerinitializer;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.testcontainers.lifecycle.Startable;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;
import java.util.Optional;

import static org.testcontainers.utility.DockerImageName.parse;

@RequiredArgsConstructor
public abstract class ContainerInitializer<C extends Startable>
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public final void initialize(@NotNull ConfigurableApplicationContext context) {
        C container = startContainer(context);
        overrideAppProperties(container, context);
    }

    protected abstract String getImageName();

    protected abstract Optional<String> getOtherImageName();

    protected abstract C createContainer(DockerImageName imageName);

    protected abstract void configure(C container);

    protected abstract Map<String, String> getPropertiesByKeys(C container);

    private C startContainer(ConfigurableApplicationContext context) {
        DockerImageName imageName = parseImageName();
        C container = createContainer(imageName);
        try {
            configure(container);
            container.start();
            closeOnContextClosed(container, context);
            return container;
        } catch (Throwable exception) {
            container.close();
            throw exception;
        }
    }

    private DockerImageName parseImageName() {
        DockerImageName imageName = parse(getImageName());
        return getOtherImageName()
                .map(imageName::asCompatibleSubstituteFor)
                .orElse(imageName);
    }

    private void closeOnContextClosed(C container, ConfigurableApplicationContext context) {
        context.addApplicationListener((ApplicationListener<ContextClosedEvent>) event -> container.close());
    }

    private void overrideAppProperties(C container, ConfigurableApplicationContext context) {
        TestPropertyValues.of(getPropertiesByKeys(container)).applyTo(context.getEnvironment());
    }
}
