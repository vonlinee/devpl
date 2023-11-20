/**
 * 数据类型项
 */
type DataTypeItem = {
	name: string; // 数据类型名称，界面显示的值
	value: string | number; // 类型值
}

/**
 * 参数项
 */
type ParamItem = {
  /**
   * ID
   */
	id: number,
  /**
   * 父节点ID
   */
	parentId?: number | null,
  /**
   * 参数名称，可包含多级名称，以.进行分隔
   */
	name: string,
  /**
   * 参数值
   */
	value?: string | null,
  /**
   * 参数项的数据类型
   */
	dataType?: DataTypeItem | string | null,
  /**
   * 是否是叶子结点
   */
  leaf: boolean
}


