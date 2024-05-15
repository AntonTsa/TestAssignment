package ua.anton.tsa.testassignment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageResponseTest<T> extends PageImpl<T> {

    @SuppressWarnings("unused")
    public PageResponseTest(@JsonProperty("content") List<T> content,
                            @JsonProperty("number") Integer number,
                            @JsonProperty("size") Integer size,
                            @JsonProperty("totalElements") Long totalElements,
                            @JsonProperty("pageable") Object pageable) {
        super(content, size > 0 ? PageRequest.of(number, size) : Pageable.unpaged(), totalElements);
    }

    public PageResponseTest(List<T> list, PageRequest pageRequest) {
        super(list, pageRequest, list.size());
    }

    static <T> PageResponseTest<T> of(List<T> list, int pageNumber, int pageSize) {
        return new PageResponseTest<>(list, PageRequest.of(pageNumber, pageSize));
    }

}
