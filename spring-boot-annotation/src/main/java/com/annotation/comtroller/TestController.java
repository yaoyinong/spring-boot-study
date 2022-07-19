package com.annotation.comtroller;

import com.annotation.anno.PrintLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yaoyinong
 * @date 2022/7/19 21:09
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/annotation")
public class TestController {

    @PrintLog
    @GetMapping("/test")
    public Map<String, Object> logTest(@RequestParam String message, @RequestParam Integer code) {
        log.info("执行Controller方法...");
        Map<String, Object> map = new HashMap<>();
        map.put("respCode", code);
        map.put("respMsg", "resp" + message);
//        int a = 1 / 0;
        return map;
    }

}
