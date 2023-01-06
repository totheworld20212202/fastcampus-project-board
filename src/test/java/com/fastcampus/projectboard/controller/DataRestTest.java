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
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//@WebMvcTest // web mvc는 slice test. controller외에 bean들을 load하지 않는다. 따라서, datarest의 autoconfiguration을 읽지않았다.
@Disabled("Spring Data REST 통합테스트는 불필요하므로 제외시킴")
@DisplayName("Data REST - API 테스트")
@SpringBootTest // 그래서 integration test를 사용해야함. webEnvironment는 mock을 써야함. 이 상태에서는 mockmvc존재알수없다.
@AutoConfigureMockMvc   // 그래서 이걸 추가해주어야한다.
@Transactional  // test에서 동작하는 Transactional은 기본동작이 rollback
public class DataRestTest {
    private final MockMvc mvc;

    public DataRestTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[api] 게시글 리스트 조회")
    @Test
    void givenNothing_whenRequestingArticles_thenReturnsArticlesJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles"))  // get()하고 ctrl+space한번 더 누르면  MockMvcRequestBuilders.get()이나온다. alt + enter를 눌러서 static import.
                .andExpect(status().isOk()) // 여기서도 status하고 ctrl+space 두번 눌러서 alt+enter
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json;charset=UTF-8")));
//                .andDo(print()); //pr까지누르고 alt+enter해서 static import

    }

    @DisplayName("[api] 게시글 단건 조회")
    @Test
    void givenNothing_whenRequestingArticle_thenReturnsArticlesJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles/1"))  // get()하고 ctrl+space한번 더 누르면  MockMvcRequestBuilders.get()이나온다. alt + enter를 눌러서 static import.
                .andExpect(status().isOk()) // 여기서도 status하고 ctrl+space 두번 눌러서 alt+enter
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json;charset=UTF-8")));
//                .andDo(print()); //pr까지누르고 alt+enter해서 static import

    }

    @DisplayName("[api] 게시글 -> 댓글 리스트 조회")
    @Test
    void givenNothing_whenRequestingArticleCommentsFromArticle_thenReturnsArticleCommentsJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles/1/articleComments"))  // get()하고 ctrl+space한번 더 누르면  MockMvcRequestBuilders.get()이나온다. alt + enter를 눌러서 static import.
                .andExpect(status().isOk()) // 여기서도 status하고 ctrl+space 두번 눌러서 alt+enter
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json;charset=UTF-8")));
//                .andDo(print()); //pr까지누르고 alt+enter해서 static import

    }

    @DisplayName("[api] 댓글 리스트 조회")
    @Test
    void givenNothing_whenRequestingArticleComments_thenReturnsArticleCommentsJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articleComments"))  // get()하고 ctrl+space한번 더 누르면  MockMvcRequestBuilders.get()이나온다. alt + enter를 눌러서 static import.
                .andExpect(status().isOk()) // 여기서도 status하고 ctrl+space 두번 눌러서 alt+enter
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json;charset=UTF-8")));
//                .andDo(print()); //pr까지누르고 alt+enter해서 static import

    }

    @DisplayName("[api] 댓글 단건 조회")
    @Test
    void givenNothing_whenRequestingArticleComment_thenReturnsArticleCommentJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articleComments/1"))  // get()하고 ctrl+space한번 더 누르면  MockMvcRequestBuilders.get()이나온다. alt + enter를 눌러서 static import.
                .andExpect(status().isOk()) // 여기서도 status하고 ctrl+space 두번 눌러서 alt+enter
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json;charset=UTF-8")));
//                .andDo(print()); //pr까지누르고 alt+enter해서 static import

    }

}
