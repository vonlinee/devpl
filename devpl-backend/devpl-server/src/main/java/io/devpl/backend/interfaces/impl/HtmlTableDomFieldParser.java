package io.devpl.backend.interfaces.impl;

import io.devpl.backend.interfaces.FieldParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * F12打开选中table元素，右键复制 -> 复制元素或者复制outerHTML
 */
public class HtmlTableDomFieldParser implements FieldParser {

    @Override
    public List<Map<String, Object>> parse(String content) {

        Document document = Jsoup.parse(content);

        Elements tableElements = document.getElementsByTag("table");

        // 包括标题行
        List<String[]> rows = new ArrayList<>();

        for (int i = 0; i < tableElements.size(); i++) {
            Element tableElement = tableElements.get(i);
            // 标题行
            Elements theadElement = tableElement.getElementsByTag("th");

            List<String> headerRow = new ArrayList<>();
            for (int colNum = 0; colNum < theadElement.size(); colNum++) {
                headerRow.add(theadElement.get(colNum).html());
            }
            rows.add(headerRow.toArray(new String[0]));

            Elements tbodyElement = tableElement.getElementsByTag("tbody");

            Elements trElements = tbodyElement.get(0).getElementsByTag("tr");
            for (int rowNum = 0; rowNum < trElements.size(); rowNum++) {
                Elements tdElements = trElements.get(rowNum).getElementsByTag("td");

                List<String> row = new ArrayList<>();

                for (int colNum = 0; colNum < tdElements.size(); colNum++) {
                    Element tdElement = tdElements.get(colNum);

                    row.add(tdElement.html());
                }

                rows.add(row.toArray(new String[0]));
            }
        }

        return null;
    }

    public static void main(String[] args) {
        String html = """
            <table>
            <thead>
            <tr style="background-color: rgb(64, 158, 255); color: rgb(255, 255, 255);">
            <th style="text-align: left; width: 188px;">参数名</th>
            <th style="text-align: left; width: 188px;">必选</th>
            <th style="text-align: left; width: 188px;">类型</th>
            <th style="width: 188px;">说明</th>
            </tr>
            </thead>
            <tbody>
            <tr style="background-color: rgb(255, 255, 255);">
            <td style="text-align:left;">appid</td>
            <td style="text-align:left;">✔</td>
            <td style="text-align:left;">string</td>
            <td>发起请求的子系统ID，即分配的SysID的值</td>
            </tr>
            <tr style="background-color: rgb(255, 255, 255);">
            <td style="text-align:left;">access_token</td>
            <td style="text-align:left;">✔</td>
            <td style="text-align:left;">string</td>
            <td>接口请求通行令牌，即对appid参数的值取MD5值并反序得到；<br>C#环境可使用以下方法生成</td>
            </tr>
            <tr style="background-color: rgb(255, 255, 255);">
            <td style="text-align:left;">SchoolID</td>
            <td style="text-align:left;">✔</td>
            <td style="text-align:left;">string</td>
            <td>学校id</td>
            </tr>
            <tr style="background-color: rgb(255, 255, 255);">
            <td style="text-align:left;">ParentID</td>
            <td style="text-align:left;">✘</td>
            <td style="text-align:left;">string</td>
            <td>部门id，指定后返回该部门的下级部门列表<br>不传值或传递空字符串，则返回第一层级的部门</td>
            </tr>
            <tr style="background-color: rgb(255, 255, 255);">
            <td style="text-align:left;">PageIndex</td>
            <td style="text-align:left;">✘</td>
            <td style="text-align:left;">int</td>
            <td>页码，从1起，传递0则表示不启用分页<br>默认为1</td>
            </tr>
            <tr style="background-color: rgb(255, 255, 255);">
            <td style="text-align:left;">PageSize</td>
            <td style="text-align:left;">✘</td>
            <td style="text-align:left;">int</td>
            <td>页大小，默认为10</td>
            </tr>
            </tbody>
            </table>
            """;

        HtmlTableDomFieldParser parser = new HtmlTableDomFieldParser();

        List<Map<String, Object>> list = parser.parse(html);


    }
}
