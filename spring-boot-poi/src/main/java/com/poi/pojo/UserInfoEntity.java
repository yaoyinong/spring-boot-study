package com.poi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yaoyinong
 * @date 2022/7/16 00:12
 * @description 测试Entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoEntity {

    /**
     * 编号
     */
    private String no;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 住址
     */
    private String address;

    /**
     * 电话号
     */
    private String telephone;

    /**
     * 余额
     */
    private String balance;

}
