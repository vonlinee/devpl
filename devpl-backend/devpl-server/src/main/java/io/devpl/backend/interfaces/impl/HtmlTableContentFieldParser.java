package io.devpl.backend.interfaces.impl;

import io.devpl.backend.interfaces.FieldParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HtmlTableContentFieldParser implements FieldParser {

    /**
     * @param content 直接复制浏览器页面上的表格内容，复制的结果是纯文本（暂时未发现有什么特定的规则）
     * @return 字段信息
     */
    @Override
    public List<Map<String, Object>> parse(String content) {

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

        for (int i = 1; i < rows.size(); i++) {

        }
        return null;
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
                result.add(content.substring(start, end));
                start = end;
            }
        }
        return result.toArray(new String[0]);
    }

    public static void main(String[] args) {
        String content = """
            参数名	必选	类型	说明
            appid	✔	string	发起请求的子系统ID，即分配的SysID的值
            access_token	✔	string	接口请求通行令牌，即对appid参数的值取MD5值并反序得到；
            C#环境可使用以下方法生成
            SchoolID	✔	string	学校id
            ParentID	✘	string	部门id，指定后返回该部门的下级部门列表
            不传值或传递空字符串，则返回第一层级的部门
            PageIndex	✘	int	页码，从1起，传递0则表示不启用分页
            默认为1
            PageSize	✘	int	页大小，默认为10
            """;

        HtmlTableContentFieldParser parser = new HtmlTableContentFieldParser();

        List<Map<String, Object>> fields = parser.parse(content);

        System.out.println(fields);
    }
}
