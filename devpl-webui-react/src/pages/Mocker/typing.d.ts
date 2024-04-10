

type ContextMenuParam = {
  dataSourceId: number;
  databaseName?: string;
  tableName?: string;
}


/**
 * 树节点类型
 */
type DataNode = {
  title: string | React.ReactNode;
  key: string;
  isLeaf?: boolean;
  children?: DataNode[];
  /**
   * 数据库名称，表名称
   */
  nodeName: string;
}