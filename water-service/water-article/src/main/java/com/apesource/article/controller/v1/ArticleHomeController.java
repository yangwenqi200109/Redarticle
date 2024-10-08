package com.apesource.article.controller.v1;


import com.ape.common.constants.ArticleConstants;
import com.ape.model.article.dtos.ArticleDto;
import com.ape.model.article.dtos.ArticleHomeDto;
import com.ape.model.common.dtos.ResponseResult;
import com.apesource.article.service.ApArticleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/article")
@Api(value = "/api/v1/article",tags = "app端文章登录")
public class ArticleHomeController {
    @Autowired
    private ApArticleService apArticleService;

    @PostMapping("/load")
    public ResponseResult load(@RequestBody ArticleHomeDto dto) {
        return apArticleService.load(ArticleConstants.LOADTYPE_LOAD_MORE,dto);
    }

    @PostMapping("/loadmore")
    public ResponseResult loadMore(@RequestBody ArticleHomeDto dto) {
        return apArticleService.load(ArticleConstants.LOADTYPE_LOAD_MORE,dto);
    }

    @PostMapping("/loadnew")
    public ResponseResult loadNew(@RequestBody ArticleHomeDto dto) {
        return apArticleService.load(ArticleConstants.LOADTYPE_LOAD_NEW,dto);
    }

    @PostMapping("/save")
    public ResponseResult saveArticle(@RequestBody ArticleDto dto){
        return apArticleService.saveArticle(dto);
    }

    @GetMapping("/del/id")
    public ResponseResult delArticle(@PathVariable("id")Long id){
        return apArticleService.delArticle(id);
    }

}

