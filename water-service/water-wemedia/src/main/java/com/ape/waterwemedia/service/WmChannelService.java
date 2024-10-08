package com.ape.waterwemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ape.model.common.dtos.ResponseResult;
import com.ape.model.wemedia.pojos.WmChannel;

public interface WmChannelService extends IService<WmChannel> {

    /**
     * 查询所有频道
     * @return
     */
    public ResponseResult findAll();


}