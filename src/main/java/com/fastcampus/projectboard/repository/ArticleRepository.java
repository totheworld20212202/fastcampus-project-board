package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article,Long>,
        QuerydslPredicateExecutor<Article>, // Article안에 모든 field에 관한 기본 검색기능을 추가해줌.
        QuerydslBinderCustomizer<QArticle>
{
    Page<Article> findByTitle(String title, Pageable pageable);
    @Override
    default void customize(QuerydslBindings bindings, QArticle root){
        bindings.excludeUnlistedProperties(true); // article의 모든 field 검색이 querydslpredicateExecutor에 의해 열려 있는데, 선택적 field 검색되도록함.
        bindings.including(root.title, root.content, root.hashtag,root.createdAt,root.createdBy);
//        bindings.bind(root.title).first(((path, value) -> path.eq(value)));
//        bindings.bind(root.title).first((SimpleExpression::eq));
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // like '%${v}%'
        bindings.bind(root.content).first((StringExpression::containsIgnoreCase));// like'%${v}%' 부분 검색 %는 와일드 카드
        bindings.bind(root.hashtag).first((StringExpression::containsIgnoreCase));// like'%${v}%' 부분 검색 %는 와일드 카드
        bindings.bind(root.createdBy).first((StringExpression::containsIgnoreCase));// like'%${v}%' 부분 검색 %는 와일드 카드
        bindings.bind(root.createdAt).first((DateTimeExpression::eq));// like'%${v}%' 부분 검색 %는 와일드 카드
//        bindings.bind(root.title).first((StringExpression::likeIgnoreCase));// like '${v}' 부분검색이긴한데, %의 와일드 카드가 없음.
    };
}
