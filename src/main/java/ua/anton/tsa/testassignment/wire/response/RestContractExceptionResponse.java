package ua.anton.tsa.testassignment.wire.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Builder;
import ua.anton.tsa.testassignment.wire.Response;


/**
 * A class for formatting errors output
 *
 * @param statusCode   - {@link Integer} param for error's status code
 * @param reasonPhrase - {@link String} param for error's reason Phrase
 * @param error        - {@link String} param for error's message
 * @param details      - {@link String} param for details
 */
@Builder
@JsonRootName("errors")
public record RestContractExceptionResponse(
        Integer statusCode,
        String reasonPhrase,
        String error,
        String details
) implements Response {

}
