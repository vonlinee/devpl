package net.maku.maku.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.maku.framework.common.query.Query;


/**
* 字段信息表查询
*
* @author 111 222
* @since 1.0.0 2023-07-29
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "字段信息表查询")
public class FieldSpecQuery extends Query {
}
