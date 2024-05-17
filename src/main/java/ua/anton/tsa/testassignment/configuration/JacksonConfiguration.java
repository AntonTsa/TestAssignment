package ua.anton.tsa.testassignment.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for creating beans with custom configuration
 */
@Configuration
public class JacksonConfiguration {

    /**
     * Create an object mapper with custom configurations
     * Module {@link JavaTimeModule} is responsile for serialization and deserialization of {@link java.time.LocalDate}
     * objects.
     * Serialization feature WRAP_ROOT_VALUE and deserialization feature UNWRAP_ROOT_VALUE are used for enabling
     * {@link com.fasterxml.jackson.annotation.JsonRootName} annotations
     *
     * @return the {@link ObjectMapper} object of customized mapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = JsonMapper.builder()
                        .addModule(new JavaTimeModule())
                .build();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        return mapper;
    }
}
