package com.apesource.article.service;

import com.ape.model.article.pojos.ApArticleConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface ApArticleConfigService extends IService<ApArticleConfig> {
    /**
     * 修改文章配置
     * @param map
     */
    public void updateByMap(Map map);
}
