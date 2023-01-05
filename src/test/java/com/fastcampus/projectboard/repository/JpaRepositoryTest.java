package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.config.JpaConfig;
import com.fastcampus.projectboard.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;    // test 할때 필요 assertThat()

//@ActiveProfiles("testdb")
@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {


    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCommentRepository articleCommentRepository
    ) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
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



        // When
        Article savedArticle = articleRepository.save(Article.of("new article", "new content", "#spring"));

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
        Article savedArticle = articleRepository.saveAndFlush(article); // 기본적으로 영속성테스트는 transactional rollback이 걸려있음.
                                                                // update을하고 특이사항 (조회) 라던지 그런것이 없으면 다시 돌아가서 #spring으로 바뀜
                                                                // 그래서, 그냥 save가 아닌 SaveAndFlush를 해줌

        // Then
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag",updatedHashtag);


    }

    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine(){
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        int deletedCommentsSize = article.getArticleComments().size();
        String updatedHashtag = "#springboot";
        article.setHashtag(updatedHashtag);




        // When
        articleRepository.delete(article); // 기본적으로 영속성테스트는 transactional rollback이 걸려있음.
        // update을하고 특이사항 (조회) 라던지 그런것이 없으면 다시 돌아가서 #spring으로 바뀜
        // 그래서, 그냥 save가 아닌 SaveAndFlush를 해줌

        // Then
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentsSize);

        // 전체 테스트는 ctrl + shift + F10
    }







// 일단 localhost:8080/h2-console이 되려면 spring 자체 application을 띄워야한다.



}
