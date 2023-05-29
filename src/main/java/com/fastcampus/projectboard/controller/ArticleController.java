package com.fastcampus.projectboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * /articles
 * /articles/{article-id}
 * /articles/search
 * /articles/search-hashtag
 */
@RequestMapping("/articles")
@Controller
public class ArticleController {

    @GetMapping
    public String articles(ModelMap map){
        map.addAttribute("articles", List.of());
        return "articles/index";
    }

    @GetMapping("/{articleId}") // 1,2,3 이런식으로 articleId가 들어올것인데 이를 {articleId}로 표현
    public String article(@PathVariable Long articleId, ModelMap map){
        // @PathVariable이라는 것은 위의 getmapping로 전달되는 parameter를 articleId라는 parameter로 받겠다는 뜻이다.
        map.addAttribute("article", "article"); // TODO: 구현할 때 여기에 실제 데이터 
        map.addAttribute("articleComments", List.of());
        return "articles/detail";
    }
}
