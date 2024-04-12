package by.aurorasoft.testapp.base.containerinitializer;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

import static org.testcontainers.utility.DockerImageName.parse;

public final class DBContainerInitializer extends ContainerInitializer<PostgreSQLContainer<?>> {
    private static final String PROPERTY_KEY_DATASOURCE_URL = "spring.datasource.url";
    private static final String PROPERTY_KEY_USERNAME = "spring.datasource.username";
    private static final String PROPERTY_KEY_PASSWORD = "spring.datasource.password";

    private static final String FULL_IMAGE_NAME = "mdillon/postgis:latest";
    private static final String OTHER_IMAGE_NAME = "postgres";
    private static final DockerImageName DOCKER_IMAGE_NAME = parse(FULL_IMAGE_NAME)
            .asCompatibleSubstituteFor(OTHER_IMAGE_NAME);

    private static final String DATA_BASE_NAME = "integration-tests-db";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "sa";

    @Override
    protected PostgreSQLContainer<?> createContainer() {
        return new PostgreSQLContainer<>(DOCKER_IMAGE_NAME);
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
                PROPERTY_KEY_DATASOURCE_URL, container.getJdbcUrl(),
                PROPERTY_KEY_USERNAME, container.getUsername(),
                PROPERTY_KEY_PASSWORD, container.getPassword()
        );
    }
}
