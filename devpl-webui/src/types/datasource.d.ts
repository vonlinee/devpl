type TestConnVO = {
  /**
   * 是否失败
   */
  failed: boolean
  /**
   * 数据库类型
   */
  dbmsType: string
  /**
   * 是否使用ssl连接
   */
  useSsl: boolean
  /**
   * 连接失败时的错误信息
   */
  errorMsg: string
}


type DataSourceVO = {
  /**
   * 数据源ID
   */
  id: number
  /**
   * 数据源名称
   */
  name: string
}

/**
 * Request
 */
type ParamGetDbTableData = {
  /**
   * 连接配置
   */
  connInfo?: DbConnInfo
  /**
   * 数据源ID
   */
  dataSourceId?: number | null
  /**
   * 数据库名称
   */
  dbName?: null | string
  pageIndex?: number | null
  pageSize?: number | null
  /**
   * 表格名称
   */
  tableName?: null | string

  [property: string]: any
}

/**
 * 连接配置
 *
 * DbConnInfo
 */
type DbConnInfo = {
  /**
   * 连接名
   */
  connectionName?: null | string
  /**
   * URL
   */
  connectionUrl?: null | string
  /**
   * 创建时间
   */
  createTime?: null | string
  /**
   * 数据库名称
   */
  dbName?: null | string
  /**
   * 数据库类型
   */
  dbType?: null | string
  /**
   * 是否逻辑删除
   */
  deleted?: boolean | null
  /**
   * 驱动类名
   */
  driverClassName?: null | string
  /**
   * 驱动属性
   */
  driverProps?: null | string
  /**
   * 驱动类型
   */
  driverType?: string
  /**
   * IP地址
   */
  host?: null | string
  /**
   * id
   */
  id?: number | string | null
  /**
   * 密码
   */
  password?: null | string
  /**
   * 连接端口号
   */
  port?: number | null
  /**
   * 更新时间
   */
  updateTime?: null | string
  /**
   * 用户名
   */
  username?: null | string

  [property: string]: any
}

/**
 * 响应数据，可能是单个对象或者对象数组
 *
 * DBTableDataVO
 */
type DBTableDataVO = {
  /**
   * 表头
   */
  headers: ResultSetColumnMetadata[]
  /**
   * 表行数据列表，仅包含数据行
   */
  rows: string[][]
  /**
   * 表行数据列表格式，包含标题和数据行
   */
  rows1: ResultSetRowModel[]
}

/**
 * JDBC查询结果元数据
 *
 * ResultSetColumnMetadata
 */
type ResultSetColumnMetadata = {
  catalogName?: null | string
  columnClassName?: null | string
  columnDisplaySize?: number | null
  /**
   * 用于标题
   */
  columnLabel?: null | string
  /**
   * 列名称
   */
  columnName: string
  /**
   * 列数据类型
   */
  columnType: number
  columnTypeName?: null | string
  precision?: number | null
  scale?: number | null
  schemaName?: null | string
  /**
   * 表名
   */
  tableName?: null | string
}

type TreeNodeVO = {
  /**
   * 唯一ID
   */
  id?: number
  /**
   * 文本
   */
  label: string
  /**
   * 是否是叶子结点
   */
  leaf: boolean
  /**
   * 子节点
   */
  children?: TreeNodeVO[]
}
