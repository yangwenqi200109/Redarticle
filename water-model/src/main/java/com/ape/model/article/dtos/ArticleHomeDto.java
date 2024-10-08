package com.ape.model.article.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ArticleHomeDto {

    // 最大时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    Date maxBehotTime;
    // 最小时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    Date minBehotTime;
    // 分页size
    Integer size;
    // 频道ID
    String tag;
}