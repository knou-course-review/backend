package knou.course.dto.course.response;

import knou.course.domain.course.Course;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CoursePagedResponse {

    private List<CourseListResponse> content;

    private int pageNumber;

    private int pageSize;

    private long totalElements;

    private int totalPages;

    private boolean first;

    private boolean last;

    @Builder
    public CoursePagedResponse(final List<CourseListResponse> content, final int pageNumber, final int pageSize, final long totalElements, final int totalPages, final boolean first, final boolean last) {
        this.content = content;
        this.pageNumber = pageNumber + 1;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.first = first;
        this.last = last;
    }

    public static CoursePagedResponse of(List<CourseListResponse> course, Page<Course> coursePage) {
        return CoursePagedResponse.builder()
                .content(course)
                .pageNumber(coursePage.getNumber())
                .pageSize(coursePage.getSize())
                .totalElements(coursePage.getTotalElements())
                .totalPages(coursePage.getTotalPages())
                .first(coursePage.isFirst())
                .last(coursePage.isLast())
                .build();
    }
}
