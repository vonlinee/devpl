/**
 * Request
 */
export interface ParamGetDbTableData {
  /**
   * 连接配置
   */
  connInfo?: DbConnInfo;
  /**
   * 数据源ID
   */
  dataSourceId?: number | null;
  /**
   * 数据库名称
   */
  dbName?: null | string;
  pageIndex?: number | null;
  pageSize?: number | null;
  /**
   * 表格名称
   */
  tableName?: null | string;

  [property: string]: any;
}

/**
 * 连接配置
 *
 * DbConnInfo
 */
export interface DbConnInfo {
  /**
   * 连接名
   */
  connName?: null | string;
  /**
   * URL
   */
  connUrl?: null | string;
  /**
   * 创建时间
   */
  createTime?: null | string;
  /**
   * 数据库名称
   */
  dbName?: null | string;
  /**
   * 数据库类型
   */
  dbType?: null | string;
  /**
   * 是否逻辑删除
   */
  deleted?: boolean | null;
  /**
   * 驱动类名
   */
  driverClassName?: null | string;
  /**
   * 驱动属性
   */
  driverProps?: null | string;
  /**
   * 驱动类型
   */
  driverType?: string;
  /**
   * IP地址
   */
  host?: null | string;
  /**
   * id
   */
  id?: number | null;
  /**
   * 密码
   */
  password?: null | string;
  /**
   * 连接端口号
   */
  port?: number | null;
  /**
   * 更新时间
   */
  updateTime?: null | string;
  /**
   * 用户名
   */
  username?: null | string;

  [property: string]: any;
}

/**
 * 响应数据，可能是单个对象或者对象数组
 *
 * DBTableDataVO
 */
export interface DBTableDataVO {
  /**
   * 表头
   */
  headers?: ResultSetColumnMetadata[] | null;
  /**
   * 表行数据列表
   */
  rows?: Array<string[]> | null;

  [property: string]: any;
}

/**
 * JDBC查询结果元数据
 *
 * ResultSetColumnMetadata
 */
export interface ResultSetColumnMetadata {
  catalogName?: null | string;
  columnClassName?: null | string;
  columnDisplaySize?: number | null;
  columnLabel?: null | string;
  /**
   * 列名称
   */
  columnName: string;
  /**
   * 列数据类型
   */
  columnType: number;
  columnTypeName?: null | string;
  precision?: number | null;
  scale?: number | null;
  schemaName?: null | string;
  tableName?: null | string;

  [property: string]: any;
}
