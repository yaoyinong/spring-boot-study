package com.poi.service.impl;

import com.poi.service.ImportExcelService;
import com.poi.util.ExcelReadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.stream.IntStream;

/**
 * @author yaoyinong
 * @date 2022/7/15 22:49
 * @description 导入ExcelService实现类
 */
@Slf4j
@Service
public class ImportExcelServiceImpl implements ImportExcelService {

    @Override
    public Boolean importExcel(MultipartFile file) {
        if (file == null) {
            log.error("上传文件不能为空");
            return false;
        }
        String fileName = file.getName();
        log.info("导入Excel开始,name:{}", fileName);
        try (InputStream inputStream = file.getInputStream();
             XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            //遍历每个sheet
            int sheetCount = workbook.getNumberOfSheets();
            IntStream.range(0, sheetCount).forEach(sheetIndex -> {
                //遍历sheet中的每一行
                XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
                log.info("开始遍历sheet:{}", sheet.getSheetName());
                int rowCount = sheet.getPhysicalNumberOfRows();
                IntStream.range(1, rowCount).forEach(rowIndex -> {
                    XSSFRow row = sheet.getRow(rowIndex);
                    String no = ExcelReadUtil.getCellValue(row.getCell(0));
                    String name = ExcelReadUtil.getCellValue(row.getCell(1));
                    String sex = ExcelReadUtil.getCellValue(row.getCell(2));
                    String address = ExcelReadUtil.getCellValue(row.getCell(3));
                    String telephone = ExcelReadUtil.getCellValue(row.getCell(4));
                    String balance = ExcelReadUtil.getCellValue(row.getCell(5));
                    log.info("正在读取第{}行数据,编号:{},姓名:{},性别:{},住址:{},手机号:{},余额:{}", rowIndex, no, name, sex, address, telephone, balance);
                });
                log.info("sheet:{} 读取完毕", sheet.getSheetName());
            });
        } catch (Exception e) {
            log.error("读取文件内容失败:{}", e.getMessage(), e);
            return false;
        }
        return true;
    }

}
