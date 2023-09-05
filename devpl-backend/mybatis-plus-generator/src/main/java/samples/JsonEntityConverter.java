package samples;

import com.baomidou.mybatisplus.generator.jdbc.CommonJavaType;
import com.baomidou.mybatisplus.generator.type.JavaType;
import com.baomidou.mybatisplus.generator.type.JsonDataType;
import com.baomidou.mybatisplus.generator.type.StandardJsonDataType;
import com.baomidou.mybatisplus.generator.utils.StringUtils;
import de.marhail.json5.*;
import de.marhail.json5.comment.Comment;

import java.util.HashMap;
import java.util.Map;

/**
 * JSON 实体类 转换 , 支持JSON5
 * <a href="https://json5.org/">JSON5</a>
 */
public class JsonEntityConverter {

    static final Map<JsonDataType, JavaType> typeMapping = new HashMap<>();

    static {
        typeMapping.put(StandardJsonDataType.NUMBER, CommonJavaType.INTEGER);
        typeMapping.put(StandardJsonDataType.BOOLEAN, CommonJavaType.BOOLEAN);
    }

    static final String input = "D:/Temp/1.json";

    public static void main(String[] args) {

        Json5 parser = new Json5();

        final String json = """
            {
              "Code": 200,
              "Msg": "成功",
              "Data": [
                {
                  "LateNum": 423,    // 迟到次数
                  "absentNum": 12,    // 缺勤次数
                  "userName": "张三" ,   // 姓名
                  "age": 23 // 年龄
                }
              ]
            }
            """;

        Json5Element root = parser.parse(json);

        Json5Object obj = root.getAsJson5Object();

        Json5Array data = obj.getAsJson5Array("Data");
        if (!data.isEmpty()) {
            Json5Element jsonElement = data.get(0);

            if (jsonElement.isJson5Object()) {
                Json5Object json5Object = (Json5Object) jsonElement;

                for (Map.Entry<String, Json5Element> entry : json5Object.entrySet()) {
                    Comment comment = entry.getValue().getComment();
                    System.out.println(entry.getKey() + " " + getJavaType(entry.getValue()) + " " + StringUtils.trimWrapCharacters(comment.getCommentContent()));
                }
            }
        }
    }

    /**
     * 获取字段的Java类型
     * @param element JSON字段
     * @return Java类型
     */
    public static CommonJavaType getJavaType(Json5Element element) {
        JsonDataType jsonType = getJsonType(element);
        if (jsonType == StandardJsonDataType.ARRAY) {
            return CommonJavaType.LIST;
        } else if (jsonType == StandardJsonDataType.OBJECT) {
            return CommonJavaType.OBJECT;
        } else if (jsonType == StandardJsonDataType.NUMBER) {
            return CommonJavaType.INTEGER;
        } else if (jsonType == StandardJsonDataType.STRING) {
            return CommonJavaType.STRING;
        }
        return CommonJavaType.BASE_BYTE;
    }

    /**
     * 获取JSON类型枚举
     * @param element
     * @return
     */
    public static JsonDataType getJsonType(Json5Element element) {
        Class<?> type = element.getClass();
        if (type == Json5Array.class) {
            return StandardJsonDataType.ARRAY;
        } else if (type == Json5Object.class) {
            return StandardJsonDataType.OBJECT;
        } else if (type == Json5Boolean.class) {
            return StandardJsonDataType.BOOLEAN;
        } else if (type == Json5Number.class) {
            return StandardJsonDataType.NUMBER;
        } else if (type == Json5String.class) {
            return StandardJsonDataType.STRING;
        } else {
            return StandardJsonDataType.OBJECT;
        }
    }
}
