package com.ape.user.controller.v1;

import com.ape.model.common.dtos.ResponseResult;
import com.ape.model.user.dtos.LoginDto;
import com.ape.user.service.ApUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/login")
@Api(value = "/api/v1/login",tags = "app端用户登录")
public class ApUserLoginController {

    @Autowired
    private ApUserService apUserService;

    @PostMapping("/login_auth")
    @ApiOperation("用户登录")
    public ResponseResult login(@RequestBody LoginDto dto){
        return apUserService.login(dto);
    }

    @PostMapping("/test")
    @ApiOperation("测试")
    public ResponseResult test(){
        return ResponseResult.okResult(1,"测试");
    }
}
