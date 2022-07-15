package com.modules.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.modules.entity.UserInfo;
import com.modules.mapper.UserInfoMapper;
import com.modules.service.UserInfoService;
import org.springframework.stereotype.Service;

/**
 * @author yaoyinong
 * @date 2022/7/14 22:13
 * @description UserInfoServiceImpl
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {


}
