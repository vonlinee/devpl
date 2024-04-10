import React, {useEffect, useState} from 'react';
import {Modal, Tree, TreeDataNode} from 'antd';
import {Menu, Item, Submenu, useContextMenu, TriggerEvent} from 'react-contexify';
import 'react-contexify/dist/ReactContexify.css';
import database from '@/assets/images/db_db_object.svg';
import table from '@/assets/images/table.svg';
import {apiGetDatabaseNamesById, apiListTableNamesOfDataSource} from '@/services/datasource';
import {EventDataNode} from 'antd/es/tree';
import DatabaseMocker from './DatabaseMocker';
import './index.less'

let dbCount = 0;
let tableCount = 0;

// It's just a simple demo. You can use tree map to optimize update perf.
const updateTreeData = (list: DataNode[], key: React.Key, children: DataNode[]): DataNode[] =>
  list.map((node) => {
    if (node.key === key) {
      return {
        ...node,
        children,
      };
    }
    if (node.children) {
      return {
        ...node,
        children: updateTreeData(node.children, key, children),
      };
    }
    return node;
  });

type DatabaseNavigationViewProps = {
  dataSourceId: number;
};

/**
 * 数据库导航树结构视图
 * @param param0
 * @returns
 */
const DatabaseNavigationView: React.FC<DatabaseNavigationViewProps> = ({dataSourceId}) => {
  const [treeData, setTreeData] = useState<DataNode[]>();

  const menuId = 'menu-id';
  const {show} = useContextMenu({
    id: menuId,
  });

  /**
   * 展示content菜单
   * @param e
   * @param param
   */
  function showContextMenu(e: TriggerEvent, param: ContextMenuParam) {
    show({
      event: e,
      props: param,
    });
  }

  /**
   * 生成数据库节点
   * @param dbName
   * @returns
   */
  const generateDatabaseNode = (dbName: string): DataNode => {
    return {
      title: (
        <div>
          <img
            src={database}
            width={12}
            height={12}
            style={{
              marginRight: 3,
            }}
          ></img>
          <span
            onContextMenu={(event) =>
              showContextMenu(event, {
                dataSourceId: 0,
                databaseName: '',
                tableName: '',
              })
            }
          >
            {dbName}
          </span>
        </div>
      ),
      key: dbName + dbCount++,
      isLeaf: false,
      nodeName: dbName,
    };
  };

  const onTreeNodeSelected = (
    selectedKeys: React.Key[],
    info: {
      selected: boolean;
      selectedNodes: TreeDataNode[];
      node: EventDataNode<TreeDataNode>;
      event: string;
      nativeEvent: Event;
    },
  ) => {
    console.log(info.node);
  };

  const generateTableNode = (dataSourceId: number, dbName: string, tableName: string): DataNode => {
    return {
      title: (
        <div>
          <img
            src={table}
            width={12}
            height={12}
            style={{
              marginRight: 3,
            }}
          ></img>
          <span
            onContextMenu={(event) =>
              showContextMenu(event, {
                dataSourceId: dataSourceId,
                databaseName: dbName,
                tableName: tableName,
              })
            }
          >
            {tableName}
          </span>
        </div>
      ),
      key: tableName + tableCount++,
      isLeaf: true,
      nodeName: tableName,
    };
  };

  const [currentDatabaseName, setCurrentDatabaseName] = useState<React.Key>();

  /**
   * 优先于loadData方法执行
   * @param selectedKeys
   * @param info
   */
  const onExpand = (
    selectedKeys: React.Key[],
    info: {
      expanded: boolean;
      node: EventDataNode<TreeDataNode>;
      nativeEvent: Event;
    },
  ) => {
    setCurrentDatabaseName(info.node.key);
  };

  useEffect(() => {
    apiGetDatabaseNamesById(dataSourceId).then((res) => {
      setTreeData(
        res.data.map((dbName) => {
          return generateDatabaseNode(dbName);
        }),
      );
    });
  }, [dataSourceId]);

  /**
   * 异步加载数据
   * @returns
   * @param node
   */
  const onLoadData = (node: EventDataNode<DataNode>) => {
    return new Promise<DataNode[]>((resolve, rejected) => {
      if (node.children) {
        return;
      }
      // 获取数据库下的所有表
      apiListTableNamesOfDataSource(dataSourceId, node.nodeName).then((res) => {
        resolve(
          res.data.map((tableName) => generateTableNode(dataSourceId, node.nodeName, tableName)),
        );
      });
    }).then((res) => {
      setTreeData((origin) => {
        return updateTreeData(origin || [], node.key, res);
      });
    });
  };

  const [modalOpen, setModalOpen] = useState(false);

  const [dbMockerParam, setDbMockerParam] = useState<ContextMenuParam>({} as ContextMenuParam);

  function handleMenuItemClick(param: { event: any; props?: any; triggerEvent: any; data?: any }) {
    setModalOpen(true);
    setDbMockerParam(param.props);
  }

  return (
    <>
      <Menu id={menuId}>
        <Submenu label="工具">
          <Item onClick={handleMenuItemClick}>Mock</Item>
          <Item onClick={handleMenuItemClick}>导出</Item>
        </Submenu>
      </Menu>
      <Modal open={modalOpen} onCancel={() => setModalOpen(false)}>
        <DatabaseMocker param={dbMockerParam}/>
      </Modal>

      <div className='dbtree-container'>
        <Tree<DataNode>
          loadData={onLoadData}
          treeData={treeData}
          onExpand={onExpand}
          onSelect={onTreeNodeSelected}
        />
      </div>
    </>
  );
};

export default DatabaseNavigationView;
