package io.devpl.backend.tools.ddl;

import io.devpl.sdk.annotations.NotEmpty;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public enum SqlTypeAndJavaTypeEnum {

    BIGINT("BIGINT", Arrays.asList("long", "Long"), "(20)"),
    INT("INT", Arrays.asList("int", "Integer"), "(11)"),
    VARCHAR("VARCHAR", Arrays.asList("String", "char", "Character"), "(255)"),
    TINYINT("TINYINT", Arrays.asList("boolean", "Boolean"), "(1)"),
    DOUBLE("DOUBLE", Arrays.asList("double", "float", "Double", "Float"), ""),
    DATETIME("DATETIME", Collections.singletonList("Date"), ""),
    DECIMAL("DECIMAL", Collections.singletonList("BigDecimal"), "(18,2)"),
    DATE("DATE", Collections.singletonList("LocalDate"), ""),
    TIME("TIME", Collections.singletonList("LocalTime"), ""),
    TIMESTAMP("TIMESTAMP", Collections.singletonList("LocalDateTime"), "");

    private final String sqlType;

    private final List<String> javaType;

    private final String defaultLength;

    SqlTypeAndJavaTypeEnum(String sqlType, List<String> javaType, String defaultLength) {
        this.sqlType = sqlType;
        this.javaType = javaType;
        this.defaultLength = defaultLength;
    }

    public static SqlTypeAndJavaTypeEnum findByJavaType(@NotEmpty String javaType) {
        if (StringUtils.isBlank(javaType)) {
            throw new RuntimeException("异常实体");
        }
        for (SqlTypeAndJavaTypeEnum sqlTypeEnum : values()) {
            if (sqlTypeEnum.getJavaType().contains(javaType)) {
                return sqlTypeEnum;
            }
        }
        return VARCHAR;
    }
}
