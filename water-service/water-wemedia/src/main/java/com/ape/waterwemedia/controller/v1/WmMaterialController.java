package com.ape.waterwemedia.controller.v1;

import com.ape.model.common.dtos.ResponseResult;
import com.ape.model.wemedia.dtos.WmMaterialDto;
import com.ape.waterwemedia.service.WmMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/material")
public class WmMaterialController {

    @Autowired
    private WmMaterialService wmMaterialService;

    //素材上传
    @PostMapping("/upload_picture")
    public ResponseResult uploadPicture(MultipartFile multipartFile){
        return wmMaterialService.uploadPicture(multipartFile);
    }

    //素材列表查询（全部、收藏）
    @PostMapping("/list")
    public ResponseResult findList(@RequestBody WmMaterialDto dto){
        return wmMaterialService.findList(dto);
    }


    //图片删除
    @GetMapping("/del_picture/{id}")
    public ResponseResult delPic(@PathVariable("id")int id){
        return wmMaterialService.delPic(id);
    }

    //收藏和取消
    @GetMapping("cancel_collect/{id}")
    public ResponseResult cancelCol(@PathVariable("id")int id){
        return wmMaterialService.cancelCol(id);
    }


    //取消收藏
    @GetMapping("cancel/{id}")
    public ResponseResult cancel(@PathVariable("id")int id){
        return wmMaterialService.cancel(id);
    }

}
