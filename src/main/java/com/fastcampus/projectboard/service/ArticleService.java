package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true) // 변경하는것이 아닌 읽는것만 하므로
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        if(searchKeyword == null || searchKeyword.isBlank()){
            return articleRepository.findAll(pageable).map(ArticleDto::from);
            // findAll이 page를 반환하는데, map은 argument의 함수를 사용하여 page를 ArticleDto로 변환해주는 함수다.
            // ArticleDto::from은 ArticleDto 클래스안에 from이라는 method를 의미
            // .isBlank() : 빈 문자열이거나 스페이스바로만 이루어진 것
        }
        return switch (searchType){
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashtag("#" + searchKeyword, pageable).map(ArticleDto::from);
        };
//        return Page.empty();
//        return List.of();
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(Long articleId) {

        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(()-> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }
    // List.of()는 empty list로 not null임. []

    public void saveArticle(ArticleDto dto) {
        articleRepository.save(dto.toEntity());
    }

    public void updateArticle(ArticleDto dto) {
        try{
            Article article = articleRepository.getReferenceById(dto.id());
            if(dto.title() != null){
                article.setTitle(dto.title());  // dto는 현재 record로 만들어져있는데, getter, setter를 이미 가지고 있음.
            }
            if(dto.content() != null){
                article.setContent(dto.content());
            }
            article.setHashtag(dto.hashtag());
            articleRepository.save(article);
            // Transactional로 묶여있고, 영속성 context 끝날때, 감지해서 알아서 save함.

        } catch (EntityNotFoundException e){
            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다 - dto: {}", dto);
        }// string interpolation : dto: {}", dto. 원래는 " dto: " + dto이런형식
            // warning안찍을때 메모리 잡는 부담을 줄일수 있다고 함.
    }

    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }

}
