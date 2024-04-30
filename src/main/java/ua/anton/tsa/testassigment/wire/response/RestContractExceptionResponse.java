package ua.anton.tsa.testassigment.wire.response;

import lombok.Builder;
import ua.anton.tsa.testassigment.wire.Response;

import java.time.LocalDateTime;

@Builder
public record RestContractExceptionResponse(
        LocalDateTime timestamp,
        String error,
        String message
) implements Response {

}
