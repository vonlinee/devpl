package io.devpl.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.devpl.backend.domain.MsParamNode;
import io.devpl.backend.domain.param.GetSqlParam;
import io.devpl.backend.domain.param.MappedStatementListParam;
import io.devpl.backend.entity.MappedStatementItem;
import io.devpl.backend.tools.mybatis.ParseResult;

import java.util.List;

/**
 * MyBatis Service
 */
public interface MyBatisService {

    /**
     * 获取MyBatis参数节点
     *
     * @param content   MyBatis Mapper Statement
     * @param inferType 推断参数的类型
     * @return 参数列表
     */
    List<MsParamNode> getMapperStatementParams(String content, boolean inferType);

    ParseResult parseMapperStatement(String mapperStatement, boolean inferType, boolean enableCache);

    /**
     * 获取MappedStatement的sql
     *
     * @param param mapper标签内容
     * @return sql，可能有占位符
     */
    String getSqlOfMappedStatement(GetSqlParam param);

    String getContent(String projectId, String msId);

    List<String> buildIndex(String projectRootDir);

    /**
     * 单个项目解析Mapper信息进行索引
     *
     * @param projectRootDir 项目根路径
     */
    void buildMapperXmlIndexForProject(String projectRootDir, boolean reset);

    List<String> listIndexedProjectRootPaths();

    IPage<MappedStatementItem> pageIndexedMappedStatements(MappedStatementListParam param);
}
