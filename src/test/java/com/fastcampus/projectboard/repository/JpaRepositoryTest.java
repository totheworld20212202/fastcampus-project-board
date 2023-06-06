package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.config.JpaConfig;
import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.UserAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
@ActiveProfiles("testdb")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)    // JpaConfig는 내가 직접 만들어서 인식할수 없어서 직접 참조할수있도록 세팅
@DataJpaTest    // slice test
class JpaRepositoryTest {

    // 원래는 @autowired를 사용하는데, @datajpatest안에 springextension에 autowired 로직덕분에 생성자형태로 만들수있다고 함.
//    private ArticleRepository articleRepository;
//    private ArticleCommentRepository articleCommentRepository;
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final UserAccountRepository userAccountRepository;

    public JpaRepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCommentRepository articleCommentRepository,
            @Autowired UserAccountRepository userAccountRepository
    ) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
        this.userAccountRepository = userAccountRepository;
    }

        @DisplayName("select 테스트")
        @Test
        void givenTestData_whenSelecting_thenWorksFine(){
            // Given

            // When
            List<Article> articles = articleRepository.findAll();

            // Then
            assertThat(articles)
                    .isNotNull()
                    .hasSize(123);
        }

        @DisplayName("insert 테스트")
        @Test
        void givenTestData_whenInserting_thenWorksFine(){
            // Given
            long previousCount = articleRepository.count();
            UserAccount userAccount = userAccountRepository.save(UserAccount.of("uno", "pw", null, null, null));
            Article article = Article.of(userAccount, "new article", "new content", "#spring");
            // When
            articleRepository.save(article);
            // Then
            assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
        }

        @DisplayName("update 테스트")
        @Test
        void givenTestData_whenUpdating_thenWorksFine(){
            // Given
            Article article = articleRepository.findById(1L).orElseThrow();
            String updatedHashtag = "#springboot";
            article.setHashtag(updatedHashtag);

            // When
            Article savedArticle = articleRepository.saveAndFlush(article);
//            Article savedArticle = articleRepository.save(article);
                // @DataJpaTest    // slice test기본적으로 정책이 테스트 끝나고 rollback임. 그래서 saveAndFlush를 해줌
                // @Transactional이 걸려있닥함. rollback되면 변경점이 적용안될수있음.
            // Then
            assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag",updatedHashtag);
        }

        @DisplayName("delete 테스트")
        @Test
        void givenTestData_whenDeleting_thenWorksFine(){
            // Given
            Article article = articleRepository.findById(1L).orElseThrow();
            long previousArticleCount = articleRepository.count();
            long previousArticleComment = articleCommentRepository.count();
            int deletedCommentSize = article.getArticleComments().size();

            // When
            articleRepository.delete(article);
            // Then
            assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
            assertThat(articleCommentRepository.count()).isEqualTo(previousArticleComment - deletedCommentSize);
        }
}
