package com.ape.apis.article.fallback;
import com.ape.apis.article.IArticleClient;
import com.ape.model.article.dtos.ArticleDto;
import com.ape.model.common.dtos.ResponseResult;
import com.ape.model.common.enums.AppHttpCodeEnum;
import org.springframework.stereotype.Component;

@Component
public class IArticleClientFallback implements IArticleClient {
    @Override
    public ResponseResult saveArticle(ArticleDto dto) {
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR,"获取数据失败");
    }

    @Override
    public ResponseResult delArticle(Long id) {
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR,"获取数据失败");
    }
}
