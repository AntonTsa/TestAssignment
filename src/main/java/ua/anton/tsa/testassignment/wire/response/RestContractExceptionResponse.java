package ua.anton.tsa.testassignment.wire.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Builder;
import ua.anton.tsa.testassignment.wire.Response;

import java.time.LocalDateTime;

@Builder
@JsonRootName("errors")
public record RestContractExceptionResponse(
        Integer statusCode,
        String error,
        String details
) implements Response {

}
