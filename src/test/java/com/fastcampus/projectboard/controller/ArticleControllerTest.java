package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.config.SecurityConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 게시글")
@Import(SecurityConfig.class)   // 내가 직접 만들었기때문에 import해주어야함.
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    private final MockMvc mvc;

    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }
//    @Autowired
//    public ArticleControllerTest(MockMvc mvc) {
//        this.mvc = mvc;
//    }
//    원래는 @Autowired생략가능한데, test에 있는 것들은 @Autowired 생략 불가 MockMvc앞에 꼭 붙여주어야 함.



    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        // Given

        // when & then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())                                  // status는 두번 추천
//                .andExpect(content().contentType(MediaType.TEXT_HTML))       // content는 한번 추천  contentType은 exact match를 본다.
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))       // content는 한번 추천  contentTypeCompatibleWith은 호환되는것까지 허용
                .andExpect(view().name("articles/index"))   // view는 한번 추천
                .andExpect(model().attributeExists("articles"));     // model 한번추천, data가 articles에 의해 내려줄텐데 데이터가 있는지 체크, 데이타로 articles라는걸 넘겨주어야 한다.

                            //추천 두번 받아라. ctrl+enter 두번, 그리고 static import 위해서 alt + enter
                            // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
                            // 빨간줄은 exception 처리안해서 그럼, 마우스 가져다 대서 처리
    }
//    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        // Given

        // when & then
        mvc.perform(get("/articles/1"))
                .andExpect(status().isOk())                                  // status는 두번 추천
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))       // content는 한번 추천
                .andExpect(view().name("articles/detail"))   // 이 url로 들어왔을때 보여줄 view name
                .andExpect(model().attributeExists("article"))     // model 한번추천, data가 articles에 의해 내려줄텐데 데이터가 있는지 체크
                .andExpect(model().attributeExists("articleComments"));     // model 한번추천, data가 articles에 의해 내려줄텐데 데이터가 있는지 체크

        //추천 두번 받아라. ctrl+enter 두번, 그리고 static import 위해서 alt + enter
        // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
        // 빨간줄은 exception 처리안해서 그럼, 마우스 가져다 대서 처리
    }
    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleSearchView_thenReturnsArticleSearchView() throws Exception {
        // Given

        // when & then
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk())                                  // status는 두번 추천
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))       // content는 한번 추천
                .andExpect(model().attributeExists("articles/search"));
        //추천 두번 받아라. ctrl+enter 두번, 그리고 static import 위해서 alt + enter
        // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
        // 빨간줄은 exception 처리안해서 그럼, 마우스 가져다 대서 처리
    }
    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleHashtagSearchView_thenReturnsArticleHashtagSearchView() throws Exception {
        // Given

        // when & then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())                                  // status는 두번 추천
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))       // content는 한번 추천
                .andExpect(model().attributeExists("articles/search-hashtag"));
        //추천 두번 받아라. ctrl+enter 두번, 그리고 static import 위해서 alt + enter
        // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
        // 빨간줄은 exception 처리안해서 그럼, 마우스 가져다 대서 처리
    }
}