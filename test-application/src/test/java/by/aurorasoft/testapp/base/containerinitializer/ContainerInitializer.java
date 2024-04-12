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

import static org.testcontainers.utility.DockerImageName.parse;

@RequiredArgsConstructor
public abstract class ContainerInitializer<C extends Startable> implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public final void initialize(@NotNull final ConfigurableApplicationContext context) {
        final C container = startContainer(context);
        overrideAppProperties(container, context);
    }

    protected abstract String getImageName();

    protected abstract C createContainer(final DockerImageName imageName);

    protected abstract void configure(final C container);

    protected abstract Map<String, String> getPropertiesByKeys(final C container);

    private C startContainer(final ConfigurableApplicationContext context) {
        final DockerImageName imageName = parseImageName();
        final C container = createContainer(imageName);
        try {
            configure(container);
            container.start();
            closeOnContextClosed(container, context);
            return container;
        } catch (final Throwable exception) {
            container.close();
            throw exception;
        }
    }

    private DockerImageName parseImageName() {
        return parse(getImageName());
    }

    private void closeOnContextClosed(final C container, final ConfigurableApplicationContext context) {
        context.addApplicationListener((ApplicationListener<ContextClosedEvent>) event -> container.close());
    }

    private void overrideAppProperties(final C container, final ConfigurableApplicationContext context) {
        TestPropertyValues.of(getPropertiesByKeys(container)).applyTo(context.getEnvironment());
    }
}
