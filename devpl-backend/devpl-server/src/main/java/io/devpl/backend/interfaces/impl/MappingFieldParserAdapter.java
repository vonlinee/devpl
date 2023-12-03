package io.devpl.backend.interfaces.impl;

import io.devpl.backend.interfaces.FieldParser;
import io.devpl.sdk.util.CollectionUtils;
import io.devpl.sdk.util.NumberUtils;

import java.util.*;

public abstract class MappingFieldParserAdapter implements FieldParser {

    /**
     * 按索引顺序依次为：名称，数据类型，描述信息
     */
    private String[] columns;

    public void setColumnMapping(String[] columnMapping) {
        this.columns = columnMapping;
    }

    @Override
    public List<Map<String, Object>> parse(String content) {
        List<String[]> rows = parseRows(content);

        if (CollectionUtils.isEmpty(rows)) {
            return Collections.emptyList();
        }

        return convertRowsAsFields(rows);
    }

    public abstract List<String[]> parseRows(String content);

    public List<Map<String, Object>> convertRowsAsFields(List<String[]> rows) {
        String[] titleRow = rows.get(0);
        Map<String, Integer> titleNameIndexMap = new HashMap<>();
        for (int i = 0; i < titleRow.length; i++) {
            titleNameIndexMap.put(titleRow[i].replace("\r", ""), i);
        }

        Map<String, Integer> columnIndexMap = new HashMap<>();
        if (columns != null) {

            if (NumberUtils.isNaturalNumber(columns[0])) {
                columnIndexMap.put(FIELD_NAME, Integer.parseInt(columns[0]) - 1);
            } else {
                columnIndexMap.put(FIELD_NAME, titleNameIndexMap.get(columns[0]));
            }

            if (NumberUtils.isNaturalNumber(columns[1])) {
                columnIndexMap.put(FIELD_TYPE, Integer.parseInt(columns[1]) - 1);
            } else {
                columnIndexMap.put(FIELD_TYPE, titleNameIndexMap.get(columns[1]));
            }

            if (NumberUtils.isNaturalNumber(columns[2])) {
                columnIndexMap.put(FIELD_DESCRIPTION, Integer.parseInt(columns[2]) - 1);
            } else {
                columnIndexMap.put(FIELD_DESCRIPTION, titleNameIndexMap.get(columns[2]));
            }
        }

        List<Map<String, Object>> fields = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put(FIELD_NAME, rows.get(i)[columnIndexMap.get(FIELD_NAME)]);
            map.put(FIELD_TYPE, rows.get(i)[columnIndexMap.get(FIELD_TYPE)]);
            map.put(FIELD_DESCRIPTION, rows.get(i)[columnIndexMap.get(FIELD_DESCRIPTION)]);
            fields.add(map);
        }
        return fields;
    }
}
