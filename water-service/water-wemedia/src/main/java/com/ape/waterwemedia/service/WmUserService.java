package com.ape.waterwemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ape.model.common.dtos.ResponseResult;
import com.ape.model.wemedia.dtos.WmLoginDto;
import com.ape.model.wemedia.pojos.WmUser;

public interface WmUserService extends IService<WmUser> {

    /**
     * 自媒体端登录
     * @param dto
     * @return
     */
    public ResponseResult login(WmLoginDto dto);

}