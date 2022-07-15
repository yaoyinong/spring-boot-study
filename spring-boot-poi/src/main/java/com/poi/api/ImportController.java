package com.poi.api;

import com.poi.service.ImportExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yaoyinong
 * @date 2022/7/15 22:42
 * @description 导入Excel接口
 */
@RestController
@RequestMapping("/import")
public class ImportController {

    @Autowired
    private ImportExcelService importExcelService;

    @PostMapping("/importExcel")
    public Boolean importExcel(@RequestPart("file") MultipartFile file) {
        return importExcelService.importExcel(file);
    }

}
