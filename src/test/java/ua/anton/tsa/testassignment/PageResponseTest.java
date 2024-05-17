package ua.anton.tsa.testassignment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Class for custom extension of {@link PageImpl} class
 *
 * @param <T> for GET requests DTO Type
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageResponseTest<T> extends PageImpl<T> {

    @SuppressWarnings("unused")
    public PageResponseTest(@JsonProperty("data") List<T> content,
                            @JsonProperty("number") Integer number,
                            @JsonProperty("size") Integer size,
                            @JsonProperty("totalElements") Long totalElements,
                            @JsonProperty("pageable") PageRequest pageRequest) {
        super(content, size > 0 ? PageRequest.of(number, size) : Pageable.unpaged(), totalElements);
    }

    public PageResponseTest(List<T> list, PageRequest pageRequest) {
        super(list, pageRequest, list.size());
    }

}
