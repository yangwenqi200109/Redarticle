package com.ape.model.article.dtos;

import com.ape.model.article.pojos.ApArticle;
import lombok.Data;

@Data
public class ArticleDto  extends ApArticle {

    /**
     * 文章内容
     */
    private String content;
}
