package ua.anton.tsa.testassignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import ua.anton.tsa.testassignment.configuration.UserProperties;

/**
 * Main Application class
 */
@SpringBootApplication(scanBasePackages = Constants.SERVICE_PACKAGE)
@ConfigurationPropertiesScan(basePackageClasses = UserProperties.class)
public class TestAssigmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestAssigmentApplication.class, args);
    }

}
