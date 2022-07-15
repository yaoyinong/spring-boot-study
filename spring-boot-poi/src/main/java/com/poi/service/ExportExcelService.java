package com.poi.service;

import javax.servlet.http.HttpServletResponse;

/**
 * @author yaoyinong
 * @date 2022/7/15 23:39
 * @description
 */
public interface ExportExcelService {

    void download(HttpServletResponse response);

}
