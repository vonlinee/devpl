package io.devpl.backend.interfaces.impl;

import java.util.ArrayList;
import java.util.List;

public class HtmlTableContentFieldParser extends MappingFieldParserAdapter {

    /**
     * @param content 直接复制浏览器页面上的表格内容，复制的结果是纯文本（暂时未发现有什么特定的规则）
     * @return 字段信息
     */
    @Override
    public List<String[]> parseRows(String content) {
        String[] lines = content.split("\n");

        // 第一行为标题
        List<String[]> rows = new ArrayList<>();

        String[] titleColumns = getTitleRowsOfTableContent(lines[0]);
        rows.add(titleColumns);

        List<String> mergedLines = new ArrayList<>(lines.length);

        // 合并行
        int curLineNum = 1;
        String curLine, nextLine;
        while (true) {
            curLine = lines[curLineNum];
            if (curLineNum + 1 >= lines.length) {
                mergedLines.add(lines[curLineNum]);
                break;
            }
            int index = curLineNum + 1;
            StringBuilder line = new StringBuilder(curLine);
            while (index < lines.length) {
                nextLine = lines[index];
                String[] columns = nextLine.split("\t");
                if (columns.length < titleColumns.length) {
                    line.append(nextLine);
                } else {
                    mergedLines.add(line.toString());
                    curLineNum = index;
                    break;
                }
                index++;
            }
        }

        for (int i = 0; i < mergedLines.size(); i++) {
            String line = mergedLines.get(i);
            String[] columnsOfRow = getTitleRowsOfTableContent(line);
            rows.add(columnsOfRow);
        }
        return rows;
    }

    /**
     * 获取标题列
     *
     * @param content 标题行，一行作为一个字符串
     * @return 标题列
     */
    private String[] getTitleRowsOfTableContent(String content) {
        content = content.replace("\t", " ");
        int start = 0, end = 0;
        List<String> result = new ArrayList<>();
        while (end < content.length()) {
            char c = content.charAt(start);
            if (c == ' ' || c == '\t') {
                start++;
                end = start + 1;
            } else {
                while (end < content.length()) {
                    c = content.charAt(end);
                    if (c == ' ' || c == '\t') {
                        break;
                    }
                    end++;
                }
                result.add(content.substring(start, end).replace("\r", ""));
                start = end;
            }
        }
        return result.toArray(new String[0]);
    }
}
