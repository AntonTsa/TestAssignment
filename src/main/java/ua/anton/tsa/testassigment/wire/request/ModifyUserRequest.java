package ua.anton.tsa.testassigment.wire.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import ua.anton.tsa.testassigment.validation.BirthDate;
import ua.anton.tsa.testassigment.validation.BirthDateNullable;
import ua.anton.tsa.testassigment.validation.NotBlankNullable;
import ua.anton.tsa.testassigment.wire.Request;

@Builder
@Jacksonized
public record ModifyUserRequest(
        @NotBlankNullable @Email String email,
        @NotBlankNullable @Size(min = 1, max = 128) String firstName,
        @NotBlankNullable @Size(min = 1, max = 128) String lastName,
        @BirthDateNullable String birthDate,
        @NotBlankNullable @Size(min = 1, max = 255) String address,
        @NotBlankNullable @Size(min = 1, max = 15) String phoneNumber
) implements Request {}
