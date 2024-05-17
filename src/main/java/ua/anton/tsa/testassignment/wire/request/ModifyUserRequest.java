package ua.anton.tsa.testassignment.wire.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.NoArgsConstructor;
import ua.anton.tsa.testassignment.wire.Request;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A DTO for PATCH requests
 * Extends {@link LinkedHashMap} object and contains {@link String} fields as keys and {@link String} values as values
 */
@NoArgsConstructor
@JsonRootName("data")
public class ModifyUserRequest extends LinkedHashMap<String, String> implements Request {

    public ModifyUserRequest( Map<String, String> map) {
        super(map);
    }
}
