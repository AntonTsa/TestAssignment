package ua.anton.tsa.testassignment.wire.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import org.springframework.format.annotation.DateTimeFormat;
import ua.anton.tsa.testassignment.validation.BirthDate;
import ua.anton.tsa.testassignment.validation.Email;
import ua.anton.tsa.testassignment.validation.NotBlankNullable;
import ua.anton.tsa.testassignment.wire.Request;

import java.time.LocalDate;

/**
 * A DTO for PUT requests
 *
 * @param email       - a {@link String} field for an email
 * @param firstName   - a {@link String} field for a firstname
 * @param lastName    - a {@link String} field for a lastname
 * @param birthDate   - a {@link LocalDate} field for a birthdate
 * @param address     - a nullable {@link String} field for address
 * @param phoneNumber - a nullable {@link String} field for phone number
 */
@Builder
@Jacksonized
@JsonRootName("data")
public record ReplaceUserRequest(
        @Email String email,
        @NotBlank @Size(min = 1, max = 128) String firstName,
        @NotBlank @Size(min = 1, max = 128) String lastName,
        @BirthDate @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate,
        @NotBlankNullable @Size(min = 1, max = 255) String address,
        @NotBlankNullable @Size(min = 1, max = 15) String phoneNumber
) implements Request {}
