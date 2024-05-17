package ua.anton.tsa.testassignment.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Class for proper injection of "user.age.min" property
 */
@Validated
@ConfigurationProperties(prefix = "user")
public record UserProperties(@NotNull Age age) {
    public record Age(@NotNull Integer min) {}
}
