package com.ape.user.service;

import com.ape.model.common.dtos.ResponseResult;
import com.ape.model.user.dtos.LoginDto;
import com.ape.model.user.pojos.ApUser;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ApUserService extends IService<ApUser> {
    /**
     * app端登录功能
     * @param dto
     * @return
     */
    public ResponseResult login(LoginDto dto);
}
