package io.devpl.backend.tools.ddl;

import java.util.Map;

public class MainService {

    /**
     * 获取注释
     *
     * @param field          Field
     * @param translationMap translationMap
     * @return 注释信息
     */
    private static String getCommend(Field field, Map<String, String> translationMap) {
        if (!"id".equals(field.getName())) {
            if (MainSetting.getInstance().myProperties.getAutoTranslationRadio()) {
                return translationMap.getOrDefault(field.getTableColumn().replace("_", " "), "");
            }
            return translationMap.getOrDefault(field.getTableColumn().replace("_", " "), null);
        }
        return Constant.PRIMARY_KEY_COMMEND;
    }


    /**
     * 字段类型是否需要加入转换
     *
     * @param field Field字段信息
     * @return 是否需要进行转换
     */
    private boolean isNeedAddConvert(Field field) {
        if (null == field) {
            return false;
        }
        // 忽略 serialVersionUID
        if (Constant.SERIAL_VERSION_UID.equals(field.getName())) {
            return false;
        }
        // 是否基础数据类型
        // 额外可支持的类型
        String canonicalText = "";
        if (isAdditional(canonicalText)) {
            return true;
        }
        // 是否开启显示所有非常用字段
        return MainSetting.getInstance().myProperties.getShowNoInMapFieldRadio();
    }

    private boolean isAdditional(String canonicalText) {
        return (Constant.STRING_PACKAGE.equals(canonicalText)
            || Constant.DATE_PACKAGE.equals(canonicalText)
            || Constant.BIG_DECIMAL_PACKAGE.equals(canonicalText)
            || Constant.LOCAL_DATE.equals(canonicalText)
            || Constant.LOCAL_TIME.equals(canonicalText)
            || Constant.LOCAL_DATE_TIME.equals(canonicalText));
    }
}
