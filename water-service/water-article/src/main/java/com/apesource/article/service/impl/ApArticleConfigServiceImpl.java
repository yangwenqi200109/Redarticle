package com.apesource.article.service.impl;

import com.ape.model.article.pojos.ApArticleConfig;
import com.apesource.article.mapper.ApArticleConfigMapper;
import com.apesource.article.service.ApArticleConfigService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class ApArticleConfigServiceImpl extends ServiceImpl<ApArticleConfigMapper, ApArticleConfig> implements ApArticleConfigService {
    /**
     * 修改文章配置
     *
     * @param map
     */
    @Override
    public void updateByMap(Map map) {
        //0 下架 1 上架
        Object enable = map.get("enable");
        boolean isDown = true;
        if (enable.equals(1)) {
            isDown = false;
        }
        //修改文章配置
        update(Wrappers.<ApArticleConfig>lambdaUpdate().eq(ApArticleConfig::getArticleId,map.get("articleId")).set(ApArticleConfig::getIsDown,isDown));
    }
}
