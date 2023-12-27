package io.devpl.backend.domain.param;

import io.devpl.backend.domain.ParamNode;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetSqlParam {

    /**
     * Mapper Statement
     */
    private String mapperStatement;

    /**
     * 参数
     */
    private List<ParamNode> msParams;

    /**
     * 实际sql还是预编译的sql
     */
    private Integer real;
}
