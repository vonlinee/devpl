package io.devpl.backend.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class PoiUtils {

    /**
     * @param path 本地文件路径
     * @return Workbook对象
     */
    public static Workbook open(String path, FileInputStream fis) throws IOException {
        Workbook workbook = null;
        if (path.endsWith(".xls")) {
            // 2003版本的excel，用.xls结尾
            workbook = new HSSFWorkbook(fis);
        } else if (path.endsWith(".xlsx")) {
            // 2007版本的excel，用.xlsx结尾
            workbook = new XSSFWorkbook(fis);
        }
        return workbook;
    }
}
