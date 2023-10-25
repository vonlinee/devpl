package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.generator.util.TypeUtils;
import io.devpl.generator.domain.ParamNode;
import io.devpl.generator.domain.param.GetSqlParam;
import io.devpl.generator.enums.MapperStatementParamValueType;
import io.devpl.generator.mybatis.MyBatisUtils;
import io.devpl.generator.mybatis.ParseResult;
import io.devpl.generator.mybatis.SqlFormat;
import io.devpl.generator.mybatis.tree.TreeNode;
import io.devpl.generator.service.MyBatisService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MyBatisServiceImpl implements MyBatisService {

    @Resource
    SqlSessionFactory sqlSessionFactory;

    @Resource
    DataSource dataSource;

    Configuration configuration;

    @Override
    public ParseResult paraseMapperStatement(String mapperStatement) {
        return MyBatisUtils.parseXml(mapperStatement);
    }

    @Override
    public String getExecutableSql(MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) {
        Configuration configuration = sqlSessionFactory.getConfiguration();

        ParameterHandler parameterHandler = configuration.newParameterHandler(mappedStatement, parameterObject, boundSql);

        try {
            // 获取数据源
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSql());
            parameterHandler.setParameters(preparedStatement);

            String sql = preparedStatement.toString();

            int index = sql.indexOf(":");
            if (index >= 0) {
                sql = sql.substring(index + 1);
            }
            sql = sql.replace("\n", "").replace("\t", "");
            return SqlFormat.mysql(sql);
        } catch (Exception exception) {
            log.error("获取真实sql出错");
        }
        return "解析失败";
    }

    @Override
    public String getPreCompliedSql(GetSqlParam param) {
        List<TreeNode<ParamNode>> treeNodes = buildParamNodeTree(param.getMsParams());
        Map<String, Object> map = new HashMap<>();
        for (TreeNode<ParamNode> treeNode : treeNodes) {
            fillParamMap(treeNode, map);
        }
        // TODO 缓存解析结果
        ParseResult result = MyBatisUtils.parseXml(param.getMapperStatement());
        MappedStatement ms = result.getMappedStatement();
        BoundSql boundSql = ms.getBoundSql(map);
        String resultSql;
        if (param.getReal() == 0) {
            // 预编译sql
            resultSql = SqlFormat.mysql(boundSql.getSql());
        } else {
            resultSql = this.getExecutableSql(ms, boundSql, map);
        }
        return resultSql;
    }


    /**
     * 将树形结构的参数节点放到嵌套Map中
     *
     * @param node     参数节点
     * @param paramMap 嵌套Map
     */
    public void fillParamMap(TreeNode<ParamNode> node, Map<String, Object> paramMap) {
        if (node.hasChildren()) {
            Map<String, Object> childMap = new HashMap<>();
            for (TreeNode<ParamNode> child : node.getChildren()) {
                fillParamMap(child, childMap);
            }
            paramMap.put(node.getData().getName(), childMap);
        } else {
            ParamNode paramNode = node.getData();
            paramMap.put(paramNode.getName(), getParamValueByType(paramNode));
        }
    }


    /**
     * 递归将树形结构转换为列表
     *
     * @param parentNode 父节点
     * @param rows       存储转换结果
     * @param parentId   父节点ID
     */
    @Override
    public void recursive(TreeNode<String> parentNode, List<ParamNode> rows, int parentId) {
        ParamNode parentRow = new ParamNode();
        parentRow.setId(rows.size());
        if (parentId != -1) {
            parentRow.setParentId(parentId);
        }
        parentRow.setName(parentNode.getData());
        rows.add(parentRow);
        if (parentNode.hasChildren()) {
            for (TreeNode<String> node : parentNode.getChildren()) {
                recursive(node, rows, parentRow.getId());
            }
        } else {
            parentRow.setLeaf(true);
        }
    }

    /**
     * 将树形节点转换层单层map
     *
     * @param params 参数列表
     */
    private List<TreeNode<ParamNode>> buildParamNodeTree(List<ParamNode> params) {
        Map<Integer, TreeNode<ParamNode>> parentNodeMap = new HashMap<>();
        for (ParamNode curNode : params) {
            // 父节点为null则默认为-1
            if (curNode.isLeaf()) {
                Integer parentId = curNode.getParentId();
                if (parentId == null) {
                    parentNodeMap.put(curNode.getId(), new TreeNode<>(curNode));
                } else {
                    if (parentNodeMap.containsKey(parentId)) {
                        parentNodeMap.get(parentId).addChild(curNode);
                    } else {
                        parentNodeMap.get(parentId).addChild(new ParamNode());
                    }
                }
            } else {
                // 父节点
                final Integer nodeId = curNode.getId();
                if (parentNodeMap.containsKey(nodeId)) {
                    TreeNode<ParamNode> treeNode = parentNodeMap.get(nodeId);
                    treeNode.getChildren().add(new TreeNode<>(curNode));
                } else {
                    parentNodeMap.put(nodeId, new TreeNode<>(curNode));
                }
            }
        }
        return new ArrayList<>(parentNodeMap.values());
    }

    /**
     * 界面上输入的值都是字符串
     * 参数都是使用#{}进行指定，在给sql填充参数时字符串会使用引号包裹
     * 而数字不需要使用引号包裹，因此需要推断数据类型
     *
     * @param node 参数表中的一行数据
     * @return 参数值，将字符串推断为某个数据类型，比如字符串类型的数字，将会转化为数字类型
     */
    public static Object getParamValueByType(ParamNode node) {
        Object val = node.getValue();
        if (!(val instanceof String)) {
            return val;
        }
        final String literalValue = String.valueOf(val);
        MapperStatementParamValueType paramValueType = node.getValueType();
        if (paramValueType == null) {
            paramValueType = MapperStatementParamValueType.valueOfTypeName(node.getType());
        }
        if (paramValueType == null) {
            // 根据字符串推断类型，结果只能是简单的类型，不会很复杂
            if (TypeUtils.isInteger(literalValue)) {
                paramValueType = MapperStatementParamValueType.NUMBER;
            } else if (TypeUtils.isDouble(literalValue)) {
                paramValueType = MapperStatementParamValueType.NUMBER;
            } else {
                // 非数字类型的其他类型都可以当做字符串处理
                // 推断失败
                paramValueType = MapperStatementParamValueType.STRING;
            }
        }
        // 根据指定的类型进行赋值
        return parseLiteralValue(literalValue, paramValueType);
    }

    /**
     * 根据值类型将字符串解析为对应类型的值
     *
     * @param literalValue   字符串值
     * @param paramValueType 值类型
     * @return 对应类型 {@code paramValueType} 的值
     */
    private static Object parseLiteralValue(String literalValue, MapperStatementParamValueType paramValueType) {
        Object val;
        switch (paramValueType) {
            case NUMBER -> val = Long.parseLong(literalValue);
            case COLLECTION -> val = LocalDateTime.parse(literalValue);
            default -> val = literalValue;
        }
        return val;
    }
}
