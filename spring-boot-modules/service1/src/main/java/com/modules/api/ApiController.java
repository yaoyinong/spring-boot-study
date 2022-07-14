package com.modules.api;

import com.modules.entity.UserInfo;
import com.modules.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yaoyinong
 * @date 2022/7/14 22:17
 * @description User测试API
 */
@RestController
@RequestMapping("/api/user")
public class ApiController {

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("getById/{id}")
    public UserInfo getById(@PathVariable String id) {
        return userInfoService.getById(id);
    }

}
