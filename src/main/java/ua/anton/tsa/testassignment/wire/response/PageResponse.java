package ua.anton.tsa.testassignment.wire.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.anton.tsa.testassignment.wire.Response;

import java.util.List;

@Getter
@Builder
@Jacksonized
@JsonRootName("response")
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> implements Response {
    @JsonProperty("data") private List<T> content;
    @JsonProperty("page_info") private Pageable pageable;
    private boolean first;
    private boolean last;
    private long totalElements;
    private int totalPages;
    private int numberOfElements;

    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.pageable = page.getPageable();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.numberOfElements = page.getNumberOfElements();
    }
}
