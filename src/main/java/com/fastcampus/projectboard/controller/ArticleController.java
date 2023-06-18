package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.response.ArticleResponse;
import com.fastcampus.projectboard.dto.response.ArticleWithCommentsResponse;
import com.fastcampus.projectboard.service.ArticleService;
import com.fastcampus.projectboard.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * /articles
 * /articles/{article-id}
 * /articles/search
 * /articles/search-hashtag
 */
@RequiredArgsConstructor    // articleservice injection
@RequestMapping("/articles")
@Controller
public class ArticleController {

    private final ArticleService  articleService;
    private final PaginationService paginationService;

    // pageable에 기본 page=0으로 되어있고, controller에 page번호를 주면 알아서 pageable안으로 들어가도록 세팅되어 있다.
    @GetMapping
    public String articles(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map){
//        map.addAttribute("articles", List.of());
        Page<ArticleResponse> articles = articleService.searchArticles(searchType,searchValue,pageable).map(ArticleResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());
        map.addAttribute("articles", articles); // 여기서, articles에는 특정페이지의 10개데이터가 들어있음. 모두가 아님
        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("searchTypes", SearchType.values());   // enum에 .values()가 있다. array의 형태로 넘겨줌

        return "articles/index";
        // @RequestParam을 통해서 getparameter를 불러오고, required=false라는
        // 것은 반드시 있지 않아도 된다. 없다면 전체를 조회할것
    }

    @GetMapping("/{articleId}") // 1,2,3 이런식으로 articleId가 들어올것인데 이를 {articleId}로 표현
    public String article(@PathVariable Long articleId, ModelMap map){
        // @PathVariable이라는 것은 위의 getmapping로 전달되는 parameter를 articleId라는 parameter로 받겠다는 뜻이다.
        ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(articleService.getArticle(articleId));
        map.addAttribute("article", article);
        map.addAttribute("articleComments", article.articleCommentsResponse());
        return "articles/detail";
    }

    @GetMapping("/search-hashtag")
    public String searchHashtag(
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map
    ){
        Page<ArticleResponse> articles = articleService.searchArticlesViaHashtag(searchValue,pageable).map(ArticleResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());
        List<String> hashtags = articleService.getHashtags();
        map.addAttribute("articles", articles); // 여기서, articles에는 특정페이지의 10개데이터가 들어있음. 모두가 아님
        map.addAttribute("hashtags", hashtags); // 여기서, articles에는 특정페이지의 10개데이터가 들어있음. 모두가 아님
        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("searchType", SearchType.HASHTAG);   // enum에 .values()가 있다. array의 형태로 넘겨줌

        return "articles/search-hashtag";
    }
}
