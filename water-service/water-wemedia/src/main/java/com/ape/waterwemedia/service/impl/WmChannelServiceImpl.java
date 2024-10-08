package com.ape.waterwemedia.service.impl;

import com.ape.model.common.dtos.ResponseResult;
import com.ape.model.wemedia.pojos.WmChannel;
import com.ape.waterwemedia.mapper.WmChannelMapper;
import com.ape.waterwemedia.service.WmChannelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel> implements WmChannelService {


    /**
     * 查询所有频道
     * @return
     */
    @Override
    public ResponseResult findAll() {
        return ResponseResult.okResult(list());
    }
}