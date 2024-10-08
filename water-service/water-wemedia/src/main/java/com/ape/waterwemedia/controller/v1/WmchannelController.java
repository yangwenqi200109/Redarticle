package com.ape.waterwemedia.controller.v1;


import com.ape.model.common.dtos.ResponseResult;
import com.ape.waterwemedia.service.WmChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/channel")
public class WmchannelController {
    @Autowired
    private WmChannelService wmChannelService;

    //频道列表查询
        @GetMapping("/channels")
    public ResponseResult findAll(){
        return wmChannelService.findAll();
    }
}
