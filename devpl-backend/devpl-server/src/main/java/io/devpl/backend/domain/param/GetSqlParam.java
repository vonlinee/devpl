package io.devpl.backend.domain.param;

import io.devpl.backend.domain.MsParamNode;
import lombok.Getter;
import lombok.Setter;
import org.apache.ddlutils.platform.BuiltinDatabaseType;

import java.util.List;

@Getter
@Setter
public class GetSqlParam {

    /**
     * sql的数据库类型
     */
    private String dialect = BuiltinDatabaseType.MYSQL.getName();

    /**
     * Mapper Statement
     * <<select></select>
     * <<insert></insert>
     * <update></update>
     * <delete></delete>
     */
    private String mapperStatement;

    /**
     * 参数
     */
    private List<MsParamNode> msParams;

    /**
     * 实际sql还是预编译的sql
     */
    private Integer real;

    /**
     * 是否需要格式化
     * none: 不格式化
     * druid: 使用druid进行格式化
     */
    private String formatter = "druid";

    public boolean needFormatSql() {
        return !"none".equals(formatter);
    }
}
