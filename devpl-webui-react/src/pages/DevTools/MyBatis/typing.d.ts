/**
 * 参数节点
 */
type ParamNode = {
  /**
   * 节点唯一ID
   * @see ParamNode.parent
   */
  id?: React.ReactNode;
  /**
   * antd 树形节点唯一的 key
   */
  key: React.ReactNode;
  /**
   * 字段key
   */
  fieldKey?: React.ReactNode;
  /**
   * 字段名称
   */
  fieldName: string;
  /**
   * 字段值
   */
  value: string;
  /**
   * 数据类型
   */
  dataType: string;
  /**
   * 是否是叶子节点
   */
  leaf?: boolean;
  /**
   * 父节点ID
   */
  parent?: ParamNode;
  /**
   * 子节点列表
   */
  children?: ParamNode[];
};

/**
 * 类型推断规则
 */
type MyBatisToolOptions = {
  // 开启自动类型推断，不一定准确
  enableTypeInference: boolean;
}