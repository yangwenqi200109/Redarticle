package com.ape.waterwemedia.controller.v1;


import com.ape.model.common.dtos.ResponseResult;
import com.ape.model.wemedia.dtos.WmNewDto;
import com.ape.model.wemedia.dtos.WmNewsDto;
import com.ape.model.wemedia.dtos.WmNewsPageReqDto;
import com.ape.waterwemedia.service.WmNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news")
public class WmNewsController {
    @Autowired
    private WmNewsService wmNewsService;

    @PostMapping("/list")
    public ResponseResult findList(@RequestBody WmNewsPageReqDto dto){
        return wmNewsService.findList(dto);
    }

    //发布文章
    @PostMapping("/submit")
    public ResponseResult submitNews(@RequestBody WmNewsDto dto){
        return wmNewsService.submitNews(dto);
    }

    //查看详情
    @GetMapping("/one/{id}")
    public ResponseResult selOne(@PathVariable int id){
        return wmNewsService.selOne(id);
    }

    //删除文章
    @GetMapping("del_news/{id}")
    public ResponseResult delNews(@PathVariable int id){
        return wmNewsService.delNews(id);
    }

    //文章上下架
    @PostMapping("down_or_up")
    public ResponseResult downOrUp(@RequestBody WmNewsDto dto){
        return wmNewsService.downOrUp(dto);
    }

}
