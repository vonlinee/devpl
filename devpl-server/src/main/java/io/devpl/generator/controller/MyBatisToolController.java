package io.devpl.generator.controller;

import io.devpl.generator.common.utils.Result;
import io.devpl.generator.domain.DataTypeVO;
import io.devpl.generator.domain.ParamNode;
import io.devpl.generator.domain.param.GetSqlParam;
import io.devpl.generator.enums.MapperStatementParamValueType;
import io.devpl.generator.service.MyBatisService;
import io.devpl.generator.utils.TypeUtils;
import io.devpl.sdk.collection.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.devpl.generator.mybatis.MyBatisUtils;
import io.devpl.generator.mybatis.ParseResult;
import io.devpl.generator.mybatis.SqlFormat;
import io.devpl.generator.mybatis.tree.TreeNode;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/tools/mybatis")
@AllArgsConstructor
public class MyBatisToolController {

    private final MyBatisService myBatisService;

    /**
     * 界面上输入的值都是字符串
     * 参数都是使用#{}进行指定，在给sql填充参数时字符串会使用引号包裹
     * 而数字不需要使用引号包裹，因此需要推断数据类型
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

    /**
     * 获取Mapper Statemtn的参数列表
     * JSON传参
     */
    @PostMapping("/ms/params")
    public Result<List<ParamNode>> getMapperStatementParams(@RequestBody Map<String, Object> param) throws Exception {
        String content = (String) param.get("ms");
        Assert.hasText(content, "文本为空");
        ParseResult result = MyBatisUtils.parseXml(content);
        // 根节点不使用
        TreeNode<String> root = result.getRoot();
        final List<ParamNode> rows = new LinkedList<>();
        if (root.hasChildren()) {
            for (TreeNode<String> node : root.getChildren()) {
                recursive(node, rows, -1);
            }
        }
        return Result.ok(rows);
    }

    /**
     * 获取Mapper Statemtn的参数列表
     */
    @GetMapping("/ms/param/datatypes")
    public Result<List<DataTypeVO>> getDataTypes() throws Exception {
        List<DataTypeVO> list = Lists.arrayOf();
        MapperStatementParamValueType[] values = MapperStatementParamValueType.values();
        for (MapperStatementParamValueType type : values) {
            DataTypeVO dataTypeVO = new DataTypeVO();
            dataTypeVO.setName(type.name());
            dataTypeVO.setValue(type.name());
            dataTypeVO.setLabel(type.name());
            list.add(dataTypeVO);
        }
        return Result.ok(list);
    }

    /**
     * 获取Mapper Statemtn结合输入参数获取sql
     */
    @PostMapping("/ms/sql")
    public Result<?> getPreCompliedSql(@RequestBody GetSqlParam param) throws Exception {
        List<TreeNode<ParamNode>> treeNodes = buildParamNodeTree(param.getMsParams());
        Map<String, Object> map = new HashMap<>();
        for (TreeNode<ParamNode> treeNode : treeNodes) {
            fillParamMap(treeNode, map);
        }
        // TODO 缓存解析结果
        ParseResult result = MyBatisUtils.parseXml(param.getMapperStatement());

        MappedStatement ms = result.getMappedStatement();
        BoundSql boundSql = ms.getBoundSql(map);

        if (param.getReal() == 0) {
            // 预编译sql
            return Result.ok(SqlFormat.mysql(boundSql.getSql()));
        }
        return Result.ok(myBatisService.getExecutableSql(ms, boundSql, map));
    }

    /**
     * 将树形结构的参数节点放到嵌套Map中
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
     * @param parentNode 父节点
     * @param rows       存储转换结果
     * @param parentId   父节点ID
     * @see MyBatisToolController#getMapperStatementParams(Map)
     */
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
     * @param params 参数列表
     */
    private List<TreeNode<ParamNode>> buildParamNodeTree(List<ParamNode> params) {
        Map<Integer, TreeNode<ParamNode>> parentNodeMap = new HashMap<>();
        for (int i = 0; i < params.size(); i++) {
            ParamNode curNode = params.get(i);
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
}
