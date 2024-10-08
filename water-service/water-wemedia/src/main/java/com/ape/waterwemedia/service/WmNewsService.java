package com.ape.waterwemedia.service;

import com.ape.model.wemedia.dtos.WmNewDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ape.model.common.dtos.ResponseResult;
import com.ape.model.wemedia.dtos.WmNewsDto;
import com.ape.model.wemedia.dtos.WmNewsPageReqDto;
import com.ape.model.wemedia.pojos.WmNews;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface WmNewsService extends IService<WmNews> {

    /**
     * 条件查询文章列表
     * @param dto
     * @return
     */
    public ResponseResult findList(WmNewsPageReqDto dto);

    /**
     * 发布修改文章或保存为草稿
     * @param dto
     * @return
     */
    public ResponseResult submitNews(WmNewsDto dto);

    //查看文章详情
    public ResponseResult selOne( int id);

    //文章删除
    public ResponseResult delNews(int id);

    //文章上下架
    public ResponseResult downOrUp(WmNewsDto dto);
}
