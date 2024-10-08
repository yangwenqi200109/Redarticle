package com.ape.user.service.impl;

import com.ape.model.common.dtos.ResponseResult;
import com.ape.model.common.enums.AppHttpCodeEnum;
import com.ape.model.user.dtos.LoginDto;
import com.ape.model.user.pojos.ApUser;
import com.ape.utils.common.AppJwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ape.user.mapper.ApUserMapper;
import com.ape.user.service.ApUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;


@Service
@Transactional
@Slf4j
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {
    /**
     * app端登录功能
     * @param dto
     * @return
     */
    @Override
    public ResponseResult login(LoginDto dto) {
        //1.正常登录 用户名和密码
        if (StringUtils.isBlank(dto.getPhone())&&StringUtils.isBlank(dto.getPassword())){
            //1.1 根据手机号查询用户信息
            LambdaQueryWrapper<ApUser> queryWrapper = Wrappers.lambdaQuery();// 创建一个 Lambda 查询包装器
            ApUser dbUser = getOne(queryWrapper.eq(ApUser::getPhone, dto.getPhone())); // 设置查询条件为用户的手机号码等于传入的参数 phone,根据构建好的查询条件从数据库中获取单个满足条件的用户对象并返回
            if (dbUser == null){
                return  ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"用户信息不存在！");
            }
            //1.2 比对密码
            String salt = dbUser.getSalt();
            String password = dbUser.getPassword();
            String pswd = DigestUtils.md5DigestAsHex((password+salt).getBytes());
            if (!pswd.equals(dbUser.getPassword())){
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR,"密码错误");
            }
            //1.3 返回数据  jwt  user
            String token = AppJwtUtil.getToken(dbUser.getId().longValue());
            Map<String,Object> map = new HashMap<>();
            map.put("token",token);
            dbUser.setSalt("");
            dbUser.setPassword("");
            map.put("user",dbUser);
            return  ResponseResult.okResult(map);

        }else {
            //2.游客登录
            Map<String,Object> map = new HashMap<>();
            map.put("token",AppJwtUtil.getToken(0L));
            return ResponseResult.okResult(map);
        }
    }
}
