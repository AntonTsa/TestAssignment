package ua.anton.tsa.testassignment.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "user")
public record UserProperties(@NotNull Age age) {
    public record Age(@NotNull Integer min) {}
}
