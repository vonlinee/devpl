package io.devpl.backend.tools.translate;

import io.devpl.backend.tools.ddl.Field;
import io.devpl.backend.tools.ddl.MainSetting;
import io.devpl.backend.tools.ddl.MySettingProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranslationUtil {

    public static TranslationService translationInit(MySettingProperties properties) {
        if (StringUtils.equals(TranslationAppEnum.BAIDU.getValue(), properties.getTranslationAppComboBox())) {
            return new BaiduTranslationService(properties.getAppIdText(), properties.getSecretText());
        }
        if (StringUtils.equals(TranslationAppEnum.TENCENT.getValue(), properties.getTranslationAppComboBox())) {
            return new TencentTranslationService(properties.getSecretId(), properties.getSecretKey());
        }
        return null;
    }

    public static Map<String, String> enToZh(List<Field> fieldList, String tableName) {
        tableName = tableName.replace("_", " ");
        MySettingProperties properties = MainSetting.getInstance().myProperties;
        String translationApp = properties.getTranslationAppComboBox();

        Map<String, String> dataMap = new HashMap<>();
        TranslationService translation = translationInit(properties);
        if (null == translation) {
            return dataMap;
        }

        String englishText = "";
        // 百度翻译
        if (StringUtils.equals(TranslationAppEnum.BAIDU.getValue(), translationApp)) {
            List<String> commendList = new ArrayList<>();
            commendList.add(tableName);
            for (Field field : fieldList) {
                commendList.add(field.getTableColumn().replace("_", " "));
            }
            englishText = String.join("\n", commendList);
        }
        // 腾讯翻译
        if (StringUtils.equals(TranslationAppEnum.TENCENT.getValue(), translationApp)) {
            StringBuilder text = new StringBuilder("[" + tableName + "]");
            for (Field field : fieldList) {
                text.append("[").append(field.getTableColumn().replace("_", " ")).append("]");
            }
            englishText = text.toString();
        }
        List<TranslationVO> translationList = translation.toChinese(englishText);
        if (CollectionUtils.isEmpty(translationList)) {
            return dataMap;
        }

        for (TranslationVO translationVO : translationList) {
            dataMap.put(translationVO.getSrc(), translationVO.getDst());
        }
        return dataMap;
    }
}
