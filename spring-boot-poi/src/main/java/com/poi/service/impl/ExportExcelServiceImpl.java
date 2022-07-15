package com.poi.service.impl;

import com.poi.pojo.UserInfoEntity;
import com.poi.service.ExportExcelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yaoyinong
 * @date 2022/7/15 23:41
 * @description
 */
@Slf4j
@Service
public class ExportExcelServiceImpl implements ExportExcelService {

    private static final List<UserInfoEntity> userInfoList;

    static {
        userInfoList = new ArrayList<>();
        userInfoList.add(new UserInfoEntity("T001", "小王", "男", "东京", "18611124551", "0.31"));
        userInfoList.add(new UserInfoEntity("T002", "小玉", "女", "纽约", "17621201156", "157.91"));
        userInfoList.add(new UserInfoEntity("T003", "小刘", "男", "南京", "15241124551", "0.31"));
        userInfoList.add(new UserInfoEntity("T004", "小红", "女", "北京", "13850140501", "8.1"));
    }

    @Override
    public void download(HttpServletResponse response) {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ServletOutputStream out = response.getOutputStream()) {
            log.info("开始生成Excel...");
            XSSFSheet sheet = workbook.createSheet("测试sheet1");
            List<String> title = Arrays.asList("编号", "姓名", "性别", "住址", "手机号", "余额");
            createTitle(0, sheet, title);
            for (int i = 0; i < userInfoList.size(); i++) {
                UserInfoEntity userInfo = userInfoList.get(i);
                List<String> strings = Arrays.asList(userInfo.getNo(), userInfo.getName(), userInfo.getSex(), userInfo.getAddress(), userInfo.getTelephone(), userInfo.getBalance());
                createTitle(i + 1, sheet, strings);
            }
            log.info("数据写入完毕,共:{}行", userInfoList.size());
            workbook.write(out);
        } catch (Exception e) {
            log.error("生成文件失败:{}", e.getMessage(), e);
        }
        log.info("生成Excel完毕");
    }

    private void createTitle(int rownum, XSSFSheet modelSheet, List<String> modelTitle) {
        XSSFRow row = modelSheet.createRow(rownum);
        for (int i = 0; i < modelTitle.size(); i++) {
            row.createCell(i).setCellValue(modelTitle.get(i));
        }
    }

}
