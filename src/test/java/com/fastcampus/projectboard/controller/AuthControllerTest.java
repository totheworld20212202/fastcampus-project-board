package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.config.SecurityConfig;
import com.fastcampus.projectboard.service.ArticleService;
import com.fastcampus.projectboard.service.PaginationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@DisplayName("View 컨트롤러 - 인증")
@Import(SecurityConfig.class)   // 내가 직접 만들었기때문에 import해주어야함.
@WebMvcTest(AuthControllerTest.EmptyController.class)
public class AuthControllerTest {

    private final MockMvc mvc;

    @MockBean private ArticleService articleService;
    @MockBean private PaginationService paginationService;
    public AuthControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }
//    @Autowired
//    public ArticleControllerTest(MockMvc mvc) {
//        this.mvc = mvc;
//    }
//    원래는 @Autowired생략가능한데, test에 있는 것들은 @Autowired 생략 불가 MockMvc앞에 꼭 붙여주어야 함.



    @DisplayName("[view][GET] 로그인 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenTryingToLogIn_thenReturnsLogInView() throws Exception {
        // Given

        // when & then
        mvc.perform(get("/login"))
                .andExpect(status().isOk())                                  // status는 두번 추천
//                .andExpect(content().contentType(MediaType.TEXT_HTML))       // content는 한번 추천  contentType은 exact match를 본다.
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));       // content는 한번 추천  contentTypeCompatibleWith은 호환되는것까지 허용
        then(articleService).shouldHaveNoInteractions();
        then(paginationService).shouldHaveNoInteractions();
        //추천 두번 받아라. ctrl+enter 두번, 그리고 static import 위해서 alt + enter
        // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
        // 빨간줄은 exception 처리안해서 그럼, 마우스 가져다 대서 처리
    }
    @TestComponent
    static class EmptyController{}
}
