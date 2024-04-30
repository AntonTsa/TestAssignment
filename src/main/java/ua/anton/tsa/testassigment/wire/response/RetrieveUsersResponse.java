package ua.anton.tsa.testassigment.wire.response;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import ua.anton.tsa.testassigment.wire.Response;

import java.time.LocalDate;

@Builder
@Jacksonized
public record RetrieveUsersResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        LocalDate birthDate,
        String address,
        String phoneNumber
) implements Response {}
