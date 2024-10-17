/**
 * 任意数据类型
 */
type TypeNameAny = "any"

/**
 * 单个数据类型
 */
type DataTypeItem = {
  id: number
  typeId: number
  typeKey: string
  localeTypeName: string,
  typeGroupId: string
}

/**
 * 类型分组
 */
type DataTypeGroup = {
  /**
   * 数据类型ID
   */
  id?: number
  /**
   * 类型分组ID
   */
  typeGroupId: string
  /**
   * 类型分组名称
   */
  typeGroupName: string
  /**
   * 备注信息
   */
  remark: string
  /**
   * 是否处于编辑状态
   */
  editing: boolean
  /**
   * 是否内置
   */
  internal: boolean
}

/**
 * 数据类型下拉选项
 */
type DataTypeSelectOption = {
  /**
   * 选项显示文本
   */
  label: string
  /**
   * 选项的key
   */
  key: number | string
  /**
   * 选项key对应的值
   */
  value: any
}

/**
 * 数据类型映射分组
 */
type DataTypeMappingGroup = {
  /**
   * 主键ID
   */
  id: number
  /**
   * 数据类型映射分组名称
   */
  groupName: string
  /**
   * 备注信息
   */
  remark: string
}

/**
 * 数据类型映射
 */
type DataTypeMapping = {
  /**
   * 主键ID
   */
  id?: number
  /**
   * 分组ID
   */
  groupId: number
  /**
   * 分组名称
   */
  groupName: string
  /**
   * 主类型
   */
  typeId: number | undefined
  /**
   * 主类型名称
   */
  typeName: string
  /**
   * 类型 Key
   */
  typeKey: string | undefined
  /**
   * 映射类型名称
   */
  anotherTypeId: number | undefined
  /**
   * 映射类型 Key
   */
  anotherTypeKey: string | undefined
}

/**
 * 数据类型映射, 单个组与单个组之间的映射关系
 */
type DataTypeMappingByTypeGroup = {
  /**
   * 分组ID
   */
  groupId: number | null
  /**
   * 分组名称
   */
  groupName: string
  /**
   * 映射类型名称
   */
  typeGroupId: number | undefined
  /**
   * 映射类型 Key
   */
  anotherTypeGroupId: string | undefined
  /**
   * 主数据类型
   */
  types: DataTypeItem[] | []
  /**
   * 映射数据类型
   */
  mappedDataTypes: DataTypeItem[][] | [][]
}