package io.devpl.backend.service.impl;

import io.devpl.backend.dao.DataTypeMappingMapper;
import io.devpl.backend.domain.param.FieldParseParam;
import io.devpl.backend.domain.param.Model2DDLParam;
import io.devpl.backend.domain.param.TableCreatorParam;
import io.devpl.backend.domain.vo.ColumnInfoVO;
import io.devpl.backend.service.DevToolsService;
import io.devpl.backend.service.FieldInfoService;
import io.devpl.backend.tools.ddl.DdlUtils;
import io.devpl.backend.tools.ddl.Field;
import io.devpl.common.utils.Utils;
import io.devpl.sdk.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DevToolsServiceImpl implements DevToolsService {

    @Resource
    FieldInfoService fieldInfoService;
    @Resource
    DataTypeMappingMapper dataTypeMappingMapper;

    @Override
    public String model2DDL(Model2DDLParam param) {
        FieldParseParam fieldParseParam = new FieldParseParam();
        fieldParseParam.setContent(param.getContent());
        fieldParseParam.setType("java");

        return "";
    }

    /**
     * TODO 支持多种数据库类型，目前只支持MySQL
     * TODO 后续通过模板生成
     *
     * @param param 创建表参数
     * @return DDL参数
     */
    @Override
    public String getCreateTableDDL(TableCreatorParam param) {
        List<ColumnInfoVO> columns = param.getColumns();
        List<Field> fields = new ArrayList<>();
        for (ColumnInfoVO column : columns) {
            Field field = new Field();
            column.setColumnName(Utils.removeInvisibleCharacters(column.getColumnName()));
            column.setRemarks(Utils.removeInvisibleCharacters(column.getRemarks()));
            if (param.getWrapIdentifier()) {
                field.setName("`" + column.getColumnName() + "`");
            } else {
                field.setName(column.getColumnName());
            }
            field.setComment(column.getRemarks());
            field.setPrimaryKey(column.getPrimaryKey());
            field.setType(column.getDataType());
            fields.add(field);
        }
        String tableName = StringUtils.whenBlank(param.getTableName(), "table");

        if (param.getWrapIdentifier()) {
            tableName = "`" + tableName + "`";
        }

        String result = "";
        if (param.getDropTable()) {
            result += "DROP TABLE IF EXISTS " + tableName + ";\n";
        }
        result += DdlUtils.buildDdlScript(tableName, fields);
        return result;
    }
}
