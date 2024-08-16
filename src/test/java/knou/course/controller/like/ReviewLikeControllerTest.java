package knou.course.controller.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import knou.course.dto.like.response.ReviewLikeStatusResponse;
import knou.course.service.like.ReviewLikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReviewLikeController.class)
class ReviewLikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewLikeService reviewLikeService;

    @BeforeEach
    void setUp() {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken("1", "", List.of()));
    }

    @DisplayName("리뷰 좋아요")
    @Test
    void createReviewLike() throws Exception {
        // given
        final Long reviewId = 1L;

        // when // then
        mockMvc.perform(
                        post("/api/v1/like/{reviewId}", reviewId).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("리뷰 좋아요 취소")
    @Test
    void deleteReviewLike() throws Exception {
        // given
        final Long reviewId = 1L;

        // when // then
        mockMvc.perform(
                        delete("/api/v1/like/{reviewId}", reviewId).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("리뷰 좋아요 개수와 여부")
    @Test
    void getReviewLikes() throws Exception {
        // given
        List<ReviewLikeStatusResponse> result = List.of();

        BDDMockito.given(reviewLikeService.getLikeStatusByReviewIds(List.of(), 1L)).willReturn(result);
        // when // then
        mockMvc.perform(
                        get("/api/v1/likes").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("reviewIds", "1", "2", "3")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}