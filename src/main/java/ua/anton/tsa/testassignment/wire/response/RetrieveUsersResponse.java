package ua.anton.tsa.testassignment.wire.response;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import ua.anton.tsa.testassignment.wire.Response;

import java.time.LocalDate;

/**
 * A DTO for GET requests
 * @param id          - a {@link Long} unique identifier of the object
 * @param email       - a {@link String} field for an email
 * @param firstName   - a {@link String} field for a firstname
 * @param lastName    - a {@link String} field for a lastname
 * @param birthDate   - a {@link LocalDate} field for a birthdate
 * @param address     - a nullable {@link String} field for address
 * @param phoneNumber - a nullable {@link String} field for phone number
 */
@Builder
@Jacksonized
public record RetrieveUsersResponse (
        Long id,
        String email,
        String firstName,
        String lastName,
        LocalDate birthDate,
        String address,
        String phoneNumber
) implements Response {}
