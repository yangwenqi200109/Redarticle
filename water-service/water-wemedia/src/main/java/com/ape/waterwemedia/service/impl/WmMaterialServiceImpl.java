package com.ape.waterwemedia.service.impl;

import com.ape.file.service.FileStorageService;
import com.ape.waterwemedia.mapper.WmMaterialMapper;
import com.ape.waterwemedia.service.WmMaterialService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ape.model.common.dtos.PageResponseResult;
import com.ape.model.common.dtos.ResponseResult;
import com.ape.model.common.enums.AppHttpCodeEnum;
import com.ape.model.wemedia.dtos.WmMaterialDto;
import com.ape.model.wemedia.pojos.WmMaterial;
import com.ape.utils.thread.WmThreadLocalUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 图片上传
     *
     * @param multipartFile
     * @return
     */
    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {

        //1.检查参数
        if (multipartFile == null || multipartFile.getSize() == 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.上传图片到minIO中
        String fileName = UUID.randomUUID().toString().replace("-", "");
        //aa.jpg
        String originalFilename = multipartFile.getOriginalFilename();
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileId = null;
        try {
            fileId = fileStorageService.uploadImgFile("", fileName + postfix, multipartFile.getInputStream());
            log.info("上传图片到MinIO中，fileId:{}", fileId);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("WmMaterialServiceImpl-上传文件失败");
        }

        //3.保存到数据库中
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUserId(WmThreadLocalUtil.getUser().getId());
        wmMaterial.setUrl(fileId);
        wmMaterial.setIsCollection((short) 0);
        wmMaterial.setType((short) 0);
        wmMaterial.setCreatedTime(new Date());
        save(wmMaterial);

        //4.返回结果

        return ResponseResult.okResult(wmMaterial);
    }

    /**
     * 素材列表查询
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult findList(WmMaterialDto dto) {
        //1.检查参数
        dto.checkParam();
        //2.分页查询
        IPage page = new Page(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<WmMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //是否收藏
        if (dto.getIsCollection() != null && dto.getIsCollection() == 1) {
            lambdaQueryWrapper.eq(WmMaterial::getIsCollection, dto.getIsCollection());
        }
        //按照用户查询
        lambdaQueryWrapper.eq(WmMaterial::getUserId, WmThreadLocalUtil.getUser().getId());
        //按照时间倒序
        lambdaQueryWrapper.orderByDesc(WmMaterial::getCreatedTime);
        page = page(page, lambdaQueryWrapper);
        //3.结果返回
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }


    //删除图片
    public ResponseResult delPic(int id) {
        WmMaterial wmMaterial = baseMapper.selectById(id);
        if (wmMaterial != null) {
            int result = baseMapper.deleteById(id);
            if (result > 0) {
                return ResponseResult.okResult("删除成功！");
            } else {
                return ResponseResult.errorResult(501, "文件删除失败，可能没有找到对应的文件或者出现其他问题！");
            }
        }
        return ResponseResult.errorResult(1002, "数据不存在！");
    }


    //收藏
    public ResponseResult cancelCol(int id){
        short a=0;
        WmMaterial wmMaterial = baseMapper.selectById(id);
        if (wmMaterial != null) {
            wmMaterial.setIsCollection(a);
            int result = baseMapper.updateById(wmMaterial);
            if (result > 0) {
                return ResponseResult.okResult("收藏成功！");
            } else {
                return ResponseResult.errorResult(501, "素材收藏失败，可能没有找到对应的文件或者出现其他问题！");
            }
        }
        return ResponseResult.errorResult(1002, "素材不存在！");
    }


    //取消收藏
    public ResponseResult cancel(int id){
        short a=1;
        WmMaterial wmMaterial = baseMapper.selectById(id);
        if (wmMaterial != null) {
            wmMaterial.setIsCollection(a);
            int result = baseMapper.updateById(wmMaterial);
            if (result > 0) {
                return ResponseResult.okResult("取消收藏成功！");
            } else {
                return ResponseResult.errorResult(501, "素材取消收藏失败，可能没有找到对应的文件或者出现其他问题！");
            }
        }
        return ResponseResult.errorResult(1002, "素材不存在！");
    }


}

