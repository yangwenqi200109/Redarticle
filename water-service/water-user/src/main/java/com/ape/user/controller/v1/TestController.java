package com.ape.user.controller.v1;

import com.ape.model.common.dtos.ResponseResult;
import com.ape.model.user.dtos.LoginDto;
import com.ape.user.service.ApUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/test")
@Api(value = "/api/test",tags = "app端测试")
public class TestController {

    @Autowired
    private ApUserService apUserService;

    @GetMapping("/test")
    public ResponseResult login(){
        return null;
    }

}
