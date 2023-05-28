package com.fastcampus.projectboard.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled("Spring Data REST 통합테스트는 불필요하므로 제외시킴")
@DisplayName("Data REST - API 테스트")
@Transactional  // test의 기본 동작은 rollback
//@WebMvcTest // controller slice test
@AutoConfigureMockMvc
@SpringBootTest
public class DataRestTest {

    private final MockMvc mvc;
    public DataRestTest(@Autowired MockMvc mvc){
        this.mvc = mvc;
    }

    @DisplayName("[api] 게시글 리스트 조회")
    @Test
    void givenNothing_whenRequestingArticles_thenReturnsArticlesJsonResponse() throws Exception {
        // Given
            //localhost:8080/api에서 data rest와 hal-explorer가 마치 controller만들어줌.
            // api가 존재하는지 체크alksjdfㅁㄴ이러ㅣㅏㅁㄴㅇ다음이 존재하는지
        // When & Then
        mvc.perform(get("/api/articles"))   //get하고 ctrl+space한번더 그리고 alt+ enter해서 static import를 하기 안그러면 Mockmvcrequestbuilders.get을 다가져옴.
                .andExpect(status().isOk()) // 한번더 ctrl+space 해서 MockMvcResultMatchers.status()에서 alt+ enter해서 static import
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json"))); // localhost:8080/api 에서 application/hal+json으로 세팅되어 있음.
                // alt+enter 해서 MockMvcResultMatchers.content; static import
                // mvc.perform에서 빨간색줄은 exception안 던져서그럼 그래서 마우스 가져다데서 throws exception 해주어야 함.
                // slicetest기때문에 이 상태로는 bean을 주입을 못한다고 함. 그래서, integration test를 한다고 함.  @SpringBootTest을 추가. 그리고, mockmvc인식못해서 @AutoConfigureMockMvc추가
//                .andDo(print());    // 내용을 보여줌
                // @springbootTest는 integration test. 따라서, db에 영향을 미친다. 따라서, @Transactional을 붙인다. test에서는 기본정책은 rollback

    }
    @DisplayName("[api] 게시글 단건 조회")
    @Test
    void givenNothing_whenRequestingArticle_thenReturnsArticleJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles/1"))   //get하고 ctrl+space한번더 그리고 alt+ enter해서 static import를 하기 안그러면 Mockmvcrequestbuilders.get을 다가져옴.
                .andExpect(status().isOk()) // 한번더 ctrl+space 해서 MockMvcResultMatchers.status()에서 alt+ enter해서 static import
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json"))); // localhost:8080/api 에서 application/hal+json으로 세팅되어 있음.


    }
    @DisplayName("[api] 게시글 -> 댓글 리스트 조회")
    @Test
    void givenNothing_whenRequestingArticleCommentsFromArticle_thenReturnsArticleCommentsJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles/1/articleComments"))   //get하고 ctrl+space한번더 그리고 alt+ enter해서 static import를 하기 안그러면 Mockmvcrequestbuilders.get을 다가져옴.
                .andExpect(status().isOk()) // 한번더 ctrl+space 해서 MockMvcResultMatchers.status()에서 alt+ enter해서 static import
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json"))); // localhost:8080/api 에서 application/hal+json으로 세팅되어 있음.
    }
    @DisplayName("[api] 댓글 리스트 조회")
    @Test
    void givenNothing_whenRequestingArticleComments_thenReturnsArticleCommentsJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articleComments"))   //get하고 ctrl+space한번더 그리고 alt+ enter해서 static import를 하기 안그러면 Mockmvcrequestbuilders.get을 다가져옴.
                .andExpect(status().isOk()) // 한번더 ctrl+space 해서 MockMvcResultMatchers.status()에서 alt+ enter해서 static import
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json"))); // localhost:8080/api 에서 application/hal+json으로 세팅되어 있음.
    }
    @DisplayName("[api] 댓글 단건 조회")
    @Test
    void givenNothing_whenRequestingArticleComment_thenReturnsArticleCommentJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articleComments/1"))   //get하고 ctrl+space한번더 그리고 alt+ enter해서 static import를 하기 안그러면 Mockmvcrequestbuilders.get을 다가져옴.
                .andExpect(status().isOk()) // 한번더 ctrl+space 해서 MockMvcResultMatchers.status()에서 alt+ enter해서 static import
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json"))); // localhost:8080/api 에서 application/hal+json으로 세팅되어 있음.
    }
}
