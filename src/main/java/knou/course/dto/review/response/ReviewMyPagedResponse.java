package knou.course.dto.review.response;

import knou.course.domain.review.Review;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewMyPagedResponse {

    private List<ReviewMyListResponse> content;

    private int pageNumber;

    private int pageSize;

    private long totalElements;

    private int totalPages;

    private boolean first;

    private boolean last;

    @Builder
    public ReviewMyPagedResponse(final List<ReviewMyListResponse> content, final int pageNumber, final int pageSize, final long totalElements, final int totalPages, final boolean first, final boolean last) {
        this.content = content;
        this.pageNumber = pageNumber + 1;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.first = first;
        this.last = last;
    }

    public static ReviewMyPagedResponse of(List<ReviewMyListResponse> content, Page<Review> reviewPage) {
        return ReviewMyPagedResponse.builder()
                .content(content)
                .pageNumber(reviewPage.getNumber())
                .pageSize(reviewPage.getSize())
                .totalElements(reviewPage.getTotalElements())
                .totalPages(reviewPage.getTotalPages())
                .first(reviewPage.isFirst())
                .last(reviewPage.isLast())
                .build();
    }
}
