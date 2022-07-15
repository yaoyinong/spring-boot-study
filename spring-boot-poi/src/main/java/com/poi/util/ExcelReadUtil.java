package com.poi.util;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;

/**
 * @author yaoyinong
 * @date 2022/7/15 22:39
 * @description 读取Excel工具类
 */
public class ExcelReadUtil {

    /**
     * 获取Cell值
     * @param cell 单元格
     * @return cell单元格值，统一转换为String类型
     */
    public static String getCellValue(XSSFCell cell) {
        if (cell == null) {
            return null;
        }
        String cellValue = "";
        switch (cell.getCellType()) {
            case STRING:
                cellValue = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    cellValue = cell.getDateCellValue() + "";
                } else {
                    cellValue = cell.getRawValue();
                }
                break;
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue() + "";
                break;
            case FORMULA:
                cellValue = cell.getCellFormula() + "";
                break;
            default:
                cellValue = " ";
        }
        return cellValue;
    }

    /**
     * 私有构造函数
     * 禁止实例化
     */
    private ExcelReadUtil() {
    }

}
