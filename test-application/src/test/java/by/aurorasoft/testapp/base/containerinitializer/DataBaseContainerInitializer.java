package by.aurorasoft.testapp.base.containerinitializer;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

public final class DataBaseContainerInitializer extends ContainerInitializer<PostgreSQLContainer<?>> {
    private static final String KEY_DATASOURCE_URL = "spring.datasource.url";
    private static final String KEY_USERNAME = "spring.datasource.username";
    private static final String KEY_PASSWORD = "spring.datasource.password";

    private static final String IMAGE_NAME = "postgres:latest";

    private static final String DATA_BASE_NAME = "integration-tests-db";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "sa";

    @Override
    protected String getImageName() {
        return IMAGE_NAME;
    }

    @Override
    protected PostgreSQLContainer<?> createContainer(final DockerImageName imageName) {
        return new PostgreSQLContainer<>(imageName);
    }

    @Override
    protected void configure(final PostgreSQLContainer<?> container) {
        container.withDatabaseName(DATA_BASE_NAME);
        container.withUsername(USERNAME);
        container.withPassword(PASSWORD);
    }

    @Override
    protected Map<String, String> getPropertiesByKeys(final PostgreSQLContainer<?> container) {
        return Map.of(
                KEY_DATASOURCE_URL, container.getJdbcUrl(),
                KEY_USERNAME, container.getUsername(),
                KEY_PASSWORD, container.getPassword()
        );
    }
}
