package io.devpl.generator.tools.ddl.service;

import io.devpl.generator.tools.ddl.Constant;
import io.devpl.generator.tools.ddl.model.Field;
import io.devpl.generator.tools.ddl.setting.MainSetting;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class MainService {

    /**
     * 获取注释
     * @param field
     * @param translationMap
     * @return
     */
    private static String getCommend(Field field, Map<String, String> translationMap) {
        if (!StringUtils.equals(field.getName(), "id")) {
            if (MainSetting.getInstance().myProperties.getAutoTranslationRadio()) {
                return translationMap.getOrDefault(field.getTableColumn().replace("_", " "), "");
            }
            return translationMap.getOrDefault(field.getTableColumn().replace("_", " "), null);
        }
        return Constant.PRIMARY_KEY_COMMEND;
    }


    /**
     * 字段类型是否需要加入转换
     * @param field
     * @return
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
        return (StringUtils.equals(Constant.STRING_PACKAGE, canonicalText)
            || StringUtils.equals(Constant.DATE_PACKAGE, canonicalText)
            || StringUtils.equals(Constant.BIG_DECIMAL_PACKAGE, canonicalText)
            || StringUtils.equals(Constant.LOCAL_DATE, canonicalText)
            || StringUtils.equals(Constant.LOCAL_TIME, canonicalText)
            || StringUtils.equals(Constant.LOCAL_DATE_TIME, canonicalText));
    }
}
