package com.poi.api;

import com.poi.service.ExportExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author yaoyinong
 * @date 2022/7/15 22:42
 * @description 生成并下载Excel
 */
@RestController
@RequestMapping("/export")
public class ExportController {

    @Autowired
    private ExportExcelService exportExcelService;

    @GetMapping("/download")
    public void download(HttpServletResponse response) {
        try {
            // 设置输出的格式
            String fileName = "测试.xlsx";
            response.reset();
            response.setContentType("application/force-download");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            exportExcelService.download(response);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
