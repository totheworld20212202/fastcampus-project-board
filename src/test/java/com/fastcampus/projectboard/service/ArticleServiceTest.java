package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

//import static org.junit.jupiter.api.Assertions.*;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class) // spring boot test가 아님. 필요한것은 mocking하는 식으로
class ArticleServiceTest {

    @InjectMocks    // mock을 주입하는대상을 @InjectMocks라고 하고 나머지 것들은 @Mock
    private ArticleService sut; // system under test    // 생성자에 까지는 주입불가
    @Mock
    private ArticleRepository articleRepository; // 생성자에 까지 주입가능
    /*
        검색
        각 게시글 페이지로 이동
        페이지네이션 -> Page<> 담겨있음.
        홈 버튼 -> 게시판 페이지로 리다이렉션
        정렬 기능 -> Page<>안에 들어있음.
     */
    @DisplayName("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenNoSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
        // Given
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findAll(pageable)).willReturn(Page.empty());
//        SearchParameters param = SearchParameters.of(SearchType.TITLE,"search keyword");
        // When
        Page<ArticleDto> articles = sut.searchArticles(null, null, pageable);
        // Then
        assertThat(articles).isEmpty();
        then(articleRepository).should().findAll(pageable);
    }

    @DisplayName("검색어와 함께 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
        // Given
        SearchType searchType = SearchType.TITLE;
        String searchKeyword = "title";
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findByTitleContaining(searchKeyword, pageable)).willReturn(Page.empty());

        // When
        Page<ArticleDto> articles = sut.searchArticles(searchType, searchKeyword, pageable);

        // Then
        assertThat(articles).isEmpty();
        then(articleRepository).should().findByTitleContaining(searchKeyword, pageable);
    }

    @DisplayName("게시글을 조회하면, 게시글을 반환한다. ")
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnsArticle(){
        // Given
        Long articleId = 1L;
        Article article = createArticle();
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        // When
        ArticleWithCommentsDto dto = sut.getArticle(articleId);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", article.getTitle())
                .hasFieldOrPropertyWithValue("content", article.getContent())
                .hasFieldOrPropertyWithValue("hashtag", article.getHashtag());
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("없는 게시글을 조회하면, 예외를 던진다.")
    @Test
    void givenNonexistentArticleId_whenSearchingArticle_thenThrowsException() {
        // Given
        Long articleId = 0L;
        given(articleRepository.findById(articleId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> sut.getArticle(articleId));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 없습니다 - articleId: " + articleId);
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("게시글 정보를 입력하면, 게시글을 생성한다.")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSavesArticle(){
        // Given
//        ArticleDto dto = ArticleDto.of(LocalDateTime.now(),"Uno","title","content","#java");
//        willDoNothing().given(articleRepository).save(any(Article.class));  // 사실 given() method는 return을 함. 그래서, willDoNothing을못씀
//      given(articleRepository.save(any(Article.class))).willReturn(null); // 그래서, .willReturn을 달아두었음.
        //willDoNothing(). BDDMockito의 경우 doNothing은 willDoNothing()
            // .given() : ctrl+space 두번하면 BDDMockito.given 이라는 게뜬다. alt +enter 해서 static import. assertj에 있는것이라고함.
            // any의 경우 ArgumentMatchers.any()
        ArticleDto dto = createArticleDto();
        given(articleRepository.save(any(Article.class))).willReturn(createArticle());
        // When
        sut.saveArticle(dto);
//        sut.saveArticle(ArticleDto.of(LocalDateTime.now(),"Uno","title","content","#java"));
        // Then
        then(articleRepository).should().save(any(Article.class));
            // then() : BDDMockito.then()

    }
    @DisplayName("게시글의 수정 정보를 입력하면, 게시글을 수정한다.")
    @Test
    void givenModifiedArticleInfo_whenUpdatingArticle_thenUpdatesArticle() {
        // Given
//        ArticleDto dto = ArticleDto.of(LocalDateTime.now(),"Uno","title","content","#java");
//        willDoNothing().given(articleRepository).save(any(Article.class));  // 사실 given() method는 return을 함. 그래서, willDoNothing을못씀
//        given(articleRepository.save(any(Article.class))).willReturn(null); // 그래서, .willReturn을 달아두었음.
        Article article = createArticle();
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
        given(articleRepository.getReferenceById(dto.id())).willReturn(article);
        //
        //willDoNothing(). BDDMockito의 경우 doNothing은 willDoNothing()
        // .given() : ctrl+space 두번하면 BDDMockito.given 이라는 게뜬다. alt +enter 해서 static import. assertj에 있는것이라고함.
        // any의 경우 ArgumentMatchers.any()
        // When
        sut.updateArticle(dto);
//        sut.updateArticle(1L, ArticleUpdateDto.of("title","content","#java"));
        // Then
//        then(articleRepository).should().save(any(Article.class));
        // then() : BDDMockito.then()
        assertThat(article)
                .hasFieldOrPropertyWithValue("title", dto.title())
                .hasFieldOrPropertyWithValue("content", dto.content())
                .hasFieldOrPropertyWithValue("hashtag", dto.hashtag());
        then(articleRepository).should().getReferenceById(dto.id());

    }
    @DisplayName("없는 게시글의 수정 정보를 입력하면, 경고 로그를 찍고 아무 것도 하지 않는다.")
    @Test
    void givenNonexistentArticleInfo_whenUpdatingArticle_thenLogsWarningAndDoesNothing() {
        // Given
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
        given(articleRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

        // When
        sut.updateArticle(dto);

        // Then
        then(articleRepository).should().getReferenceById(dto.id());
    }
    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle(){
        // Given
//        willDoNothing().given(articleRepository).delete(any(Article.class)); // 그래서, .willReturn을 달아두었음.
        Long articleId = 1L;
        willDoNothing().given(articleRepository).deleteById(articleId);
        // When
//        sut.saveArticle(dto);
        sut.deleteArticle(1L);
        // Then
//        then(articleRepository).should().delete(any(Article.class));
        // then() : BDDMockito.then()
        then(articleRepository).should().deleteById(articleId);
    }
    private UserAccount createUserAccount() {
        return UserAccount.of(
                "uno",
                "password",
                "uno@email.com",
                "Uno",
                null
        );
    }

    private Article createArticle() {
        return Article.of(
                createUserAccount(),
                "title",
                "content",
                "#java"
        );
    }

    private ArticleDto createArticleDto() {
        return createArticleDto("title", "content", "#java");
    }

    private ArticleDto createArticleDto(String title, String content, String hashtag) {
        return ArticleDto.of(1L,
                createUserAccountDto(),
                title,
                content,
                hashtag,
                LocalDateTime.now(),
                "Uno",
                LocalDateTime.now(),
                "Uno");
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                1L,
                "uno",
                "password",
                "uno@mail.com",
                "Uno",
                "This is memo",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }
}