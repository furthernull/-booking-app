package bookingapp.config;

import org.testcontainers.containers.PostgreSQLContainer;

public class CustomPostgreSqlContainer extends PostgreSQLContainer<CustomPostgreSqlContainer> {
    private static final String DB_IMAGE = "postgres:17-alpine";

    private static CustomPostgreSqlContainer container;

    private CustomPostgreSqlContainer() {
        super(DB_IMAGE);
    }

    public static synchronized CustomPostgreSqlContainer getInstance() {
        if (container == null) {
            container = new CustomPostgreSqlContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("TEST_DB_URL", container.getJdbcUrl());
        System.setProperty("TEST_DB_USERNAME", container.getUsername());
        System.setProperty("TEST_DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
    }
}
