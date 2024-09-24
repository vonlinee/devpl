package io.devpl.backend.service.impl;

import io.devpl.backend.entity.TableInfo;
import io.devpl.backend.service.DatabaseManageService;
import io.devpl.backend.utils.PoiUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

@Service
public class DatabaseManageServiceImpl implements DatabaseManageService {

    public TableInfo importFromExcel(String filePath) {
        return new TableInfo();
    }

    /**
     * 读取excel文件内容生成数据库表ddl
     *
     * @param filePath excel文件的绝对路径
     */
    public static void getDataFromExcel(String filePath) {
        if (!filePath.endsWith(".xls") && !filePath.endsWith(".xlsx")) {
            System.out.println("文件不是excel类型");
        }
        try (FileInputStream fis = new FileInputStream(filePath)){
            try (Workbook workbook = PoiUtils.open(filePath, fis)){
                Iterator<Sheet> sheets = workbook.sheetIterator();
                while (sheets.hasNext()) {
                    StringBuilder ddl = new StringBuilder();
                    // 是否自增
                    boolean autoIncrement = false;
                    Sheet sheet = sheets.next();
                    System.out.println("--------------------------当前读取的sheet页：" + sheet.getSheetName() + "--------------------------");
                    // 当前读取行的行号
                    int rowId = 1;
                    Iterator<Row> rows = sheet.rowIterator();
                    String tableEnglishName = "";
                    String tableChineseName = "";
                    while (rows.hasNext()) {
                        Row row = rows.next();
                        //获取表英文名
                        if (rowId == 1) {
                            Cell cell1 = row.getCell(0);
                            if (!"表英文名".equals(cell1.getStringCellValue())) {
                                System.out.println("第一行第一格应该为“表英文名”!");
                                return;
                            }
                            Cell cell2 = row.getCell(1);
                            tableEnglishName = cell2.getStringCellValue();
                            ddl.append("CREATE TABLE " + "`").append(tableEnglishName).append("` (").append("\r\n");
                            rowId++;
                            continue;
                        }
                        //获取表中文名
                        if (rowId == 2) {
                            Cell cell1 = row.getCell(0);
                            if (!"表中文名".equals(cell1.getStringCellValue())) {
                                System.out.println("第2行第一格应该为“表中文名”!");
                                return;
                            }
                            Cell cell2 = row.getCell(1);
                            tableChineseName = cell2.getStringCellValue();
                            rowId++;
                            continue;
                        }
                        //校验属性列名称和顺序
                        if (rowId == 3) {
                            if (row.getPhysicalNumberOfCells() != 6) {
                                System.out.println("第2行应该只有6个单元格!");
                                return;
                            }
                            Iterator<Cell> cells = row.cellIterator();
                            StringBuilder tableField = new StringBuilder();
                            while (cells.hasNext()) {
                                tableField.append(cells.next().getStringCellValue().trim());
                            }
                            if (!"字段名类型长度,小数点是否为主键是否自增注释".contentEquals(tableField)) {
                                System.out.println("第3行应该为 字段名 类型 长度,小数点 是否为主键 是否自增 注释 !");
                                return;
                            }
                            rowId++;
                            continue;
                        }
                        if (!row.cellIterator().hasNext()) {
                            break;
                        }
                        // 字段名
                        String fieldName = row.getCell(0).getStringCellValue();
                        if (fieldName == null | "".equals(fieldName)) {
                            break;
                        }
                        // 字段类型
                        String fieldType = row.getCell(1).getStringCellValue();
                        // 字段长度
                        Cell cell3 = row.getCell(2);
                        cell3.setCellType(CellType.STRING);
                        String fieldLength = cell3.getStringCellValue();
                        // 是否为主键
                        Cell cell4 = row.getCell(3);
                        // 是否自增
                        Cell cell5 = row.getCell(4);
                        // 字段注释
                        String fieldComment = row.getCell(5).getStringCellValue();

                        ddl.append("`").append(fieldName).append("` ").append(fieldType).append(!"0".equals(fieldLength) ? "(" + fieldLength + ")" : "").append(cell4 != null && "Y".equals(cell4.getStringCellValue()) ? " PRIMARY KEY " : "").append(cell5 != null && "Y".equals(cell5.getStringCellValue()) ? " AUTO_INCREMENT " : "").append(" COMMENT '").append(fieldComment).append("'").append(rows.hasNext() ? ",\r\n" : "\r\n");
                        if (cell4 != null && "Y".equals(cell5.getStringCellValue())) {
                            autoIncrement = true;
                        }

                        rowId++;
                    }
                    if (ddl.toString().endsWith(",\r\n")) {
                        ddl.deleteCharAt(ddl.length() - 3);
                        ddl.append("\r\n");
                    }
                    ddl.append(") ENGINE=InnoDB ").append(autoIncrement ? "AUTO_INCREMENT=1" : "").append(" DEFAULT CHARSET=utf8 ").append(!"".equals(tableChineseName) ? "COMMENT = '" + tableChineseName + "'" : "").append(";\r\n");
                    ddl.append("-- --------------------------------------------------------------------------------\r\n");
                    System.out.println(ddl);
                    writeMessageToFile(ddl.toString());
                }
                System.out.println("运行成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeMessageToFile(String message) throws IOException {
        File file = new File("ddl.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        try (FileWriter fileWriter = new FileWriter(file.getName(), true)){
            fileWriter.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
