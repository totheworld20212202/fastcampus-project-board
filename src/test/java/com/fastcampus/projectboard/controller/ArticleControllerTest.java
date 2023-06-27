package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.config.SecurityConfig;
import com.fastcampus.projectboard.domain.constant.FormStatus;
import com.fastcampus.projectboard.domain.constant.SearchType;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.dto.request.ArticleRequest;
import com.fastcampus.projectboard.dto.response.ArticleResponse;
import com.fastcampus.projectboard.service.ArticleService;
import com.fastcampus.projectboard.service.PaginationService;
import com.fastcampus.projectboard.util.FormDataEncoder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 게시글")
@Import({SecurityConfig.class, FormDataEncoder.class})   // 내가 직접 만들었기때문에 import해주어야함.
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    private final MockMvc mvc;
    private final FormDataEncoder formDataEncoder;

    @MockBean // spring slice test controller test에서
    private ArticleService articleService;  // 생성자 주입불가. 구현코드가 생성안되었음. 그래서, field 주입

    @MockBean
    private PaginationService paginationService;

    public ArticleControllerTest(
            @Autowired MockMvc mvc,
            @Autowired FormDataEncoder formDataEncoder
    ) {
        this.mvc = mvc;
        this.formDataEncoder = formDataEncoder;
    }
//    @Autowired
//    public ArticleControllerTest(MockMvc mvc) {
//        this.mvc = mvc;
//    }
//    원래는 @Autowired생략가능한데, test에 있는 것들은 @Autowired 생략 불가 MockMvc앞에 꼭 붙여주어야 함.



//    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        // Given
        given(articleService.searchArticles(eq(null),eq(null),any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(),anyInt())).willReturn(List.of(0,1,2,3,4));
            // Given부분은 mockbean 세팅하는것 : articleService.searchArticles에 eq(null),eq(null),any(Pageable.class))이 들어오면, 빈 페이지를 준다.
            // any : argumentMathers 두번 ctlr+enter로 제안받아서 alt +enter static import
            // argumentmatcher는 field중 일부만 사용못함. 그래서, eq()를 사용하는것임
            // given 두번 제안받아서 static import bdd mockito

        // when & then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())                                  // status는 두번 추천
//                .andExpect(content().contentType(MediaType.TEXT_HTML))       // content는 한번 추천  contentType은 exact match를 본다.
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))       // content는 한번 추천  contentTypeCompatibleWith은 호환되는것까지 허용
                .andExpect(view().name("articles/index"))   // view는 한번 추천
                .andExpect(model().attributeExists("articles"))     // model 한번추천, data가 articles에 의해 내려줄텐데 데이터가 있는지 체크, 데이타로 articles라는걸 넘겨주어야 한다.
                .andExpect(model().attributeExists("paginationBarNumbers"))     // model 한번추천, data가 articles에 의해 내려줄텐데 데이터가 있는지 체크, 데이타로 articles라는걸 넘겨주어야 한다.
                .andExpect(model().attributeExists("searchTypes"));     // model 한번추천, data가 articles에 의해 내려줄텐데 데이터가 있는지 체크, 데이타로 articles라는걸 넘겨주어야 한다.
//                .andExpect(model().attributeExists("searchTypes"));
        then(articleService).should().searchArticles(eq(null),eq(null),any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());
                            // then은 한번 추천받아라 그리고 그냥 enter
                            //추천 두번 받아라. ctrl+enter 두번, 그리고 static import 위해서 alt + enter
                            // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
                            // 빨간줄은 exception 처리안해서 그럼, 마우스 가져다 대서 처리
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 검색어와 함께 호출")
    @Test
    public void givenSearchKeyword_whenSearchingArticlesView_thenReturnsArticlesView() throws Exception {
        // Given
        SearchType searchType = SearchType.TITLE;
        String searchValue = "title";
        given(articleService.searchArticles(eq(searchType),eq(searchValue),any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(),anyInt())).willReturn(List.of(0,1,2,3,4));
        // Given부분은 mockbean 세팅하는것 : articleService.searchArticles에 eq(null),eq(null),any(Pageable.class))이 들어오면, 빈 페이지를 준다.
        // any : argumentMathers 두번 ctlr+enter로 제안받아서 alt +enter static import
        // argumentmatcher는 field중 일부만 사용못함. 그래서, eq()를 사용하는것임
        // given 두번 제안받아서 static import bdd mockito

        // when & then
        mvc.perform(get("/articles")
                        .queryParam("searchType",searchType.name())
                        .queryParam("searchValue",searchValue)
                )

                .andExpect(status().isOk())                                  // status는 두번 추천
//                .andExpect(content().contentType(MediaType.TEXT_HTML))       // content는 한번 추천  contentType은 exact match를 본다.
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))       // content는 한번 추천  contentTypeCompatibleWith은 호환되는것까지 허용
                .andExpect(view().name("articles/index"))   // view는 한번 추천
                .andExpect(model().attributeExists("articles"))     // model 한번추천, data가 articles에 의해 내려줄텐데 데이터가 있는지 체크, 데이타로 articles라는걸 넘겨주어야 한다.
                .andExpect(model().attributeExists("searchTypes"));     // model 한번추천, data가 articles에 의해 내려줄텐데 데이터가 있는지 체크, 데이타로 articles라는걸 넘겨주어야 한다.
//                .andExpect(model().attributeExists("searchTypes"));
        then(articleService).should().searchArticles(eq(searchType),eq(searchValue),any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());
        // then은 한번 추천받아라 그리고 그냥 enter
        //추천 두번 받아라. ctrl+enter 두번, 그리고 static import 위해서 alt + enter
        // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
        // 빨간줄은 exception 처리안해서 그럼, 마우스 가져다 대서 처리
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 페이징, 정렬 기능")
    @Test
    void givenPagingAndSortingParams_whenSearchingArticlesView_thenReturnsArticlesView() throws Exception {
        // Given
        String sortName = "title";
        String direction = "desc";
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        List<Integer> barNumbers = List.of(1, 2, 3, 4, 5);
        given(articleService.searchArticles(null, null, pageable)).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages())).willReturn(barNumbers);

        // When & Then
        mvc.perform(
                        get("/articles")
                                .queryParam("page", String.valueOf(pageNumber))
                                .queryParam("size", String.valueOf(pageSize))
                                .queryParam("sort", sortName + "," + direction)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attribute("paginationBarNumbers", barNumbers));
        then(articleService).should().searchArticles(null, null, pageable);
        then(paginationService).should().getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages());
    }

//    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        // Given
        Long articleId = 1L;
        long totalCount = 1L;
        given(articleService.getArticleWithComments(articleId)).willReturn(createArticleWithCommentsDto());
        given(articleService.getArticleCount()).willReturn(totalCount);
            // Given부분은 mockbean 세팅하는것 : articleId=1L이 들어오면 createArticleWithCommentsDto()를
            // return하도록 articleService.getArticle을 임시로 만든것. 이걸해주는게 mockBean

        // when & then
        mvc.perform(get("/articles/" + articleId))
                .andExpect(status().isOk())                                  // status는 두번 추천
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))       // content는 한번 추천
                .andExpect(view().name("articles/detail"))   // 이 url로 들어왔을때 보여줄 view name
                .andExpect(model().attributeExists("article"))     // model 한번추천, data가 articles에 의해 내려줄텐데 데이터가 있는지 체크
                .andExpect(model().attributeExists("articleComments"))     // model 한번추천, data가 articles에 의해 내려줄텐데 데이터가 있는지 체크
                .andExpect(model().attribute("totalCount", totalCount));
        then(articleService).should().getArticleWithComments(articleId);
        then(articleService).should().getArticleCount();
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
                .andExpect(view().name("articles/search"));
        //추천 두번 받아라. ctrl+enter 두번, 그리고 static import 위해서 alt + enter
        // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
        // 빨간줄은 exception 처리안해서 그럼, 마우스 가져다 대서 처리
    }

//    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleHashtagView_thenReturnsArticleHashtagView() throws Exception {
        // Given
        List<String> hashtags = List.of("#java","#spring","#boot");
        given(articleService.searchArticlesViaHashtag(eq(null),any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(),anyInt())).willReturn(List.of(1,2,3,4,5));
        given(articleService.getHashtags()).willReturn(hashtags);
        // when & then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())                                  // status는 두번 추천
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))       // content는 한번 추천
                .andExpect(view().name("articles/search-hashtag"))
                .andExpect(model().attribute("articles",Page.empty()))
                .andExpect(model().attribute("hashtags",hashtags))
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(model().attribute("searchType", SearchType.HASHTAG));
        then(articleService).should().searchArticlesViaHashtag(eq(null),any(Pageable.class));
        then(articleService).should().getHashtags();
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());

        //추천 두번 받아라. ctrl+enter 두번, 그리고 static import 위해서 alt + enter
        // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
        // 빨간줄은 exception 처리안해서 그럼, 마우스 가져다 대서 처리
    }

//    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출, 해시태그 입력")
    @Test
    public void givenHashtag_whenRequestingArticleHashtagView_thenReturnsArticleHashtagView() throws Exception {
        // Given
        String hashtag = "#java";
        List<String> hashtags = List.of("#java","#spring","#boot");
        given(articleService.searchArticlesViaHashtag(eq(hashtag),any(Pageable.class))).willReturn(Page.empty());
        given(articleService.getHashtags()).willReturn(hashtags);
        given(paginationService.getPaginationBarNumbers(anyInt(),anyInt())).willReturn(List.of(1,2,3,4,5));
        // when & then
        mvc.perform(
                get("/articles/search-hashtag")
                        .queryParam("searchValue", hashtag)

        )
                .andExpect(status().isOk())                                  // status는 두번 추천
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))       // content는 한번 추천
                .andExpect(view().name("articles/search-hashtag"))
                .andExpect(model().attribute("articles",Page.empty()))
                .andExpect(model().attribute("hashtags",hashtags))
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(model().attribute("searchType", SearchType.HASHTAG));
        then(articleService).should().searchArticlesViaHashtag(eq(hashtag),any(Pageable.class));
        then(articleService).should().getHashtags();
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());
        //추천 두번 받아라. ctrl+enter 두번, 그리고 static import 위해서 alt + enter
        // import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
        // 빨간줄은 exception 처리안해서 그럼, 마우스 가져다 대서 처리
    }
    @DisplayName("[view][GET] 새 게시글 작성 페이지")
    @Test
    void givenNothing_whenRequesting_thenReturnsNewArticlePage() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/articles/form"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/form"))
                .andExpect(model().attribute("formStatus", FormStatus.CREATE));
    }

    @DisplayName("[view][POST] 새 게시글 등록 - 정상 호출")
    @Test
    void givenNewArticleInfo_whenRequesting_thenSavesNewArticle() throws Exception {
        // Given
        ArticleRequest articleRequest = ArticleRequest.of("new title", "new content", "#new");
        willDoNothing().given(articleService).saveArticle(any(ArticleDto.class));

        // When & Then
        mvc.perform(
                        post("/articles/form")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(formDataEncoder.encode(articleRequest))
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles"))
                .andExpect(redirectedUrl("/articles"));
        then(articleService).should().saveArticle(any(ArticleDto.class));
    }

    @DisplayName("[view][GET] 게시글 수정 페이지")
    @Test
    void givenNothing_whenRequesting_thenReturnsUpdatedArticlePage() throws Exception {
        // Given
        long articleId = 1L;
        ArticleDto dto = createArticleDto();
        given(articleService.getArticle(articleId)).willReturn(dto);

        // When & Then
        mvc.perform(get("/articles/" + articleId + "/form"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/form"))
                .andExpect(model().attribute("article", ArticleResponse.from(dto)))
                .andExpect(model().attribute("formStatus", FormStatus.UPDATE));
        then(articleService).should().getArticle(articleId);
    }

    @DisplayName("[view][POST] 게시글 수정 - 정상 호출")
    @Test
    void givenUpdatedArticleInfo_whenRequesting_thenUpdatesNewArticle() throws Exception {
        // Given
        long articleId = 1L;
        ArticleRequest articleRequest = ArticleRequest.of("new title", "new content", "#new");
        willDoNothing().given(articleService).updateArticle(eq(articleId), any(ArticleDto.class));

        // When & Then
        mvc.perform(
                        post("/articles/" + articleId + "/form")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(formDataEncoder.encode(articleRequest))
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles/" + articleId))
                .andExpect(redirectedUrl("/articles/" + articleId));
        then(articleService).should().updateArticle(eq(articleId), any(ArticleDto.class));
    }

    @DisplayName("[view][POST] 게시글 삭제 - 정상 호출")
    @Test
    void givenArticleIdToDelete_whenRequesting_thenDeletesArticle() throws Exception {
        // Given
        long articleId = 1L;
        willDoNothing().given(articleService).deleteArticle(articleId);

        // When & Then
        mvc.perform(
                        post("/articles/" + articleId + "/delete")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles"))
                .andExpect(redirectedUrl("/articles"));
        then(articleService).should().deleteArticle(articleId);
    }


    private ArticleDto createArticleDto() {
        return ArticleDto.of(
                createUserAccountDto(),
                "title",
                "content",
                "#java"
        );
    }
    private ArticleWithCommentsDto createArticleWithCommentsDto() {
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "java",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "uno",
                "pw",
                "uno@mail.com",
                "Uno",
                "memo",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }
}