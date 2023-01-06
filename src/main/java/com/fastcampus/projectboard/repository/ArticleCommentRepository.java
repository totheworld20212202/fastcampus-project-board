package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.domain.QArticle;
import com.fastcampus.projectboard.domain.QArticleComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>,  //ArticleComment안에 검색기능을 추가해줌
        QuerydslBinderCustomizer<QArticleComment>{

    @Override
    default void customize(QuerydslBindings bindings,QArticleComment root){
        bindings.excludeUnlistedProperties(true);
        bindings.including( root.content, root.createdAt,root.createdBy);
//        bindings.bind(root.title).first(StringExpression::likeIgnoreCase);  // like '${v}'
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase); // like '%${v}%
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // like '%${v}%'
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase); // like '%${v}%'
    }
}
