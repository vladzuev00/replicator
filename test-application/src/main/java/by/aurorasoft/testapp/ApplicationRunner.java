package by.aurorasoft.testapp;

import by.aurorasoft.replicator.config.ReplicationConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@Import(ReplicationConfig.class)
public class ApplicationRunner {

    public static void main(String... args) {
        run(ApplicationRunner.class, args);
    }
}
