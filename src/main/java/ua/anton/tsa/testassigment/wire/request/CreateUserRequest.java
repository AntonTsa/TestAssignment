package ua.anton.tsa.testassigment.wire.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import org.springframework.format.annotation.DateTimeFormat;
import ua.anton.tsa.testassigment.validation.BirthDate;
import ua.anton.tsa.testassigment.validation.NotBlankNullable;
import ua.anton.tsa.testassigment.wire.Request;

import java.time.LocalDate;

@Builder
@Jacksonized
public record CreateUserRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 1, max = 128) String firstName,
        @NotBlank @Size(min = 1, max = 128) String lastName,
        @BirthDate @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate,
        @NotBlankNullable @Size(min = 1, max = 255) String address,
        @NotBlankNullable @Size(min = 1, max = 15) String phoneNumber
) implements Request {}
