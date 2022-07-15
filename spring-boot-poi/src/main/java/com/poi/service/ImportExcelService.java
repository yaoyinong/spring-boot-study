package com.poi.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author yaoyinong
 * @date 2022/7/15 22:49
 * @description 导入ExcelService接口
 */
public interface ImportExcelService {

    Boolean importExcel(MultipartFile file);

}
