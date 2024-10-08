package com.ape.waterwemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ape.model.common.dtos.ResponseResult;
import com.ape.model.wemedia.dtos.WmMaterialDto;
import com.ape.model.wemedia.pojos.WmMaterial;
import org.springframework.web.multipart.MultipartFile;

public interface WmMaterialService extends IService<WmMaterial> {

    /**
     * 图片上传
     * @param multipartFile
     * @return
     */
    public ResponseResult uploadPicture(MultipartFile multipartFile);

    /**
     * 素材列表查询
     * @param dto
     * @return
     */
    public ResponseResult findList( WmMaterialDto dto);


    //图片素材删除
    public ResponseResult delPic(int id);


    //图片收藏
    public ResponseResult cancelCol(int id);

    //图片取消收藏
    public ResponseResult cancel(int id);
}