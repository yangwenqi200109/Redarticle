package com.apesource.article.service;


import com.ape.model.article.dtos.ArticleDto;
import com.ape.model.article.dtos.ArticleHomeDto;
import com.ape.model.article.pojos.ApArticle;
import com.ape.model.common.dtos.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PathVariable;

public interface ApArticleService extends IService<ApArticle> {
    /**
     * 根据参数加载文章列表
     * @param loadtype 1为加载更多  2为加载最新
     * @param dto
     * @return
     */
    ResponseResult load(Short loadtype, ArticleHomeDto dto);


    /**
     * 保存app端相关文章
     * @param dto
     * @return
     */
    ResponseResult saveArticle(ArticleDto dto) ;

    ResponseResult delArticle(Long id);
}
