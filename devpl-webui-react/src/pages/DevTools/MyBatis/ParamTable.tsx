import React, {useContext, useEffect, useRef, useState} from 'react';
import type {InputRef} from 'antd';
import {Button, Form, Input, Select, Table, Tooltip} from 'antd';
import type {FormInstance} from 'antd/es/form';
import {AnyObject} from 'antd/es/_util/type';
import {MinusOutlined, PlusOutlined} from '@ant-design/icons';
import ButtonGroup from 'antd/es/button/button-group';
import {apiListMsDataTypes} from '@/services/tools/mybatis';

const EditableContext = React.createContext<FormInstance<any> | null>(null);

/**
 * 修改某个key的节点
 * @param list key所在节点的父列表
 * @param key 通过 === 进行比较
 * @param consumer 找到key对应的节点时的回调
 * @returns
 */
function find(
  list: ParamNode[],
  key: React.ReactNode,
  consumer: (item: ParamNode, index: number, list: ParamNode[]) => void,
) {
  for (let i = 0; i < list.length; ++i) {
    if (list[i].key === key) {
      consumer(list[i], i, list);
      return;
    } else {
      if (list[i].children) {
        find(list[i].children || [], key, consumer);
      }
    }
  }
}

interface EditableRowProps {
  index: number;
}

const EditableRow: React.FC<EditableRowProps> = ({index, ...props}) => {
  const [form] = Form.useForm();
  return (
    <Form form={form} component={false}>
      <EditableContext.Provider value={form}>
        <tr {...props} />
      </EditableContext.Provider>
    </Form>
  );
};

interface EditableCellProps {
  title: React.ReactNode;
  editable: boolean;
  children: React.ReactNode;
  dataIndex: keyof ParamNode;
  editConfig: CellEditConfig;
  record: ParamNode;
  handleSave: (record: ParamNode) => void;
}

/**
 * 可编辑单元格
 * @param param0
 * @returns
 */
const EditableCell: React.FC<EditableCellProps> = ({
                                                     title,
                                                     editable,
                                                     children,
                                                     dataIndex,
                                                     record,
                                                     editConfig,
                                                     handleSave,
                                                     ...restProps
                                                   }) => {

  // 编辑状态
  const [editing, setEditing] = useState(false);
  const inputRef = useRef<InputRef>(null);
  const selectRef = useRef<any>(null);
  const form = useContext(EditableContext)!;

  const [value, setValue] = useState<string>()

  const [selectOpen, setSelectOpen] = useState(false);

  useEffect(() => {
    if (editing) {
      if (editConfig === undefined || editConfig?.type === 'input') {
        inputRef.current!.focus();
      } else if (editConfig?.type === 'select') {
        setSelectOpen(true);
      }
    } else {
      form.setFieldsValue(record)
    }
  }, [editing]);

  /**
   * 切换编辑状态
   */
  const toggleEdit = () => {
    setEditing(!editing);
    const obj = {[dataIndex]: record[dataIndex]}
    form.setFieldsValue(obj);
  };

  const save = async () => {
    try {
      if (!editing) {
        return;
      }
      const values = await form.validateFields();
      toggleEdit();
      handleSave({...record, ...values});
      if (editConfig?.type === 'select') {
        setSelectOpen(false);
      }
    } catch (errInfo) {
      console.log('Save failed:', errInfo);
    }
  };

  let childNode: React.ReactNode = children;


  /**
   * 编辑状态时渲染表格
   * @param editConfig 编辑配置
   */
  function renderEditingCell(editConfig: CellEditConfig) {
    if (editConfig === undefined || editConfig?.type === 'input') {
      return (
        <Form.Item style={{margin: 0}} name={dataIndex}>
          <Input ref={inputRef} onPressEnter={save} onBlur={save}
                 onChange={(event: any) => setValue(event.target.value)}/>
        </Form.Item>
      );
    } else if (editConfig?.type === 'select') {
      return (
        <Form.Item style={{margin: 0}} name={dataIndex}>
          <Select
            ref={selectRef}
            onBlur={save}
            options={editConfig.options}
            autoFocus={true}
            open={selectOpen}
            onSelect={save}
          />
        </Form.Item>
      );
    }
  }

  if (editable) {
    childNode = editing ? (
      renderEditingCell(editConfig)
    ) : (
      <div className="editable-cell-value-wrap" style={{margin: 0, width: '100%', height: '100%'}} onClick={toggleEdit}>
        {children}
      </div>
    );
  }
  return <td {...restProps}>{childNode}</td>;
};

type EditableTableProps = Parameters<typeof Table>[0];

type ColumnTypes = Exclude<EditableTableProps['columns'], undefined>;

/**
 * 单元格编辑配置
 */
type CellEditConfig = {
  /**
   * 单元格类型
   */
  type: 'input' | 'select';
  /**
   * type = select 时的选项
   */
  options?: SelectOptionType[];
};

interface ParamTableProps {
  /**
   * 初始高度
   */
  initialHeight: number;
  /**
   * 初始数据
   */
  initialDataSource: ParamNode[];
}

/**
 * 参数表组件
 * @returns
 */
const ParamTable: React.FC<ParamTableProps> = ({initialDataSource, initialHeight}) => {
  const [msDataTypes, setMsDataTypes] = useState<DataTypeVO[]>([]);

  useEffect(() => {
    apiListMsDataTypes().then((res) => {
      setMsDataTypes(res.data);
    });
  }, []);

  const [dataSource, setDataSource] = useState<ParamNode[]>(initialDataSource);

  useEffect(() => {
    setDataSource(initialDataSource)
  }, [initialDataSource])

  const defaultColumns: (ColumnTypes[number] & {
    editable?: boolean;
    dataIndex: string;
    editConfig?: CellEditConfig;
  })[] = [
    {
      title: '参数名',
      dataIndex: 'fieldKey',
      width: 200,
      editable: true,
    },
    {
      title: '值',
      width: 200,
      dataIndex: 'value',
      editable: true,
      editConfig: {
        type: 'input'
      },
    },
    {
      align: 'left',
      title: '数据类型',
      width: 200,
      editable: true,
      editConfig: {
        type: 'select',
        options: msDataTypes,
      },
      dataIndex: 'dataType',
    },
    {
      width: 70,
      align: 'center',
      title: '操作',
      dataIndex: 'operation',
      fixed: 'right',
      render: (value: any, record: AnyObject, index: any) => {
        return (
          <>
            <ButtonGroup>
              <Tooltip title={record.leaf ? '添加相邻节点' : '添加子节点'}>
                <Button
                  shape="circle"
                  icon={<PlusOutlined/>}
                  size="small"
                  onClick={() => handAdd(value, record, index)}
                ></Button>
              </Tooltip>
              <Button
                shape="circle"
                size="small"
                icon={<MinusOutlined/>}
                onClick={() => handleRemoveNode(value, record, index)}
              ></Button>
            </ButtonGroup>
          </>
        );
      },
    },
  ];

  /**
   * 添加行
   * @param value
   * @param record
   * @param index
   */
  function handAdd(value: any, record: AnyObject, index: number) {
    const newData = [...dataSource];
    find(newData, record.key, (item, index, list) => {
      if (record.leaf) {
        // 添加相邻节点
        const key = item.key as string;
        list.splice(index + 1, 0, {
          key: item.parent?.key + '' + list.length + 1,
          fieldName: 'sl',
          value: '12122',
          leaf: true,
          dataType: '1',
        });
      } else {
        // 添加子节点
        record.children?.push({
          key: record.key + '' + list.length + 1,
          fieldName: 'sl',
          value: '12122',
          editable: true,
          dataType: '1',
        });
      }
    });
    setDataSource(newData);
  }

  function handleRemoveNode(value: any, record: AnyObject, index: number) {
    const newData = [...dataSource];
    find(newData, record.key, (item, index, list) => {
      list.splice(index, 1);
    });
    setDataSource(newData);
  }

  const handleSave = (row: ParamNode) => {
    const newData = [...dataSource];

    find(newData, row.key, (item, index, list) => {
      list.splice(index, 1, {
        ...item,
        ...row,
      });
    });
    setDataSource(newData);
  };

  // 覆盖默认的 table 元素
  const components = {
    body: {
      row: EditableRow,
      cell: EditableCell,
    },
  };

  const columns = defaultColumns.map((col) => {
    if (!col.editable) {
      return col;
    }
    return {
      ...col,
      onCell: (record: ParamNode, rowIndex: number) => {
        return {
          record,
          editConfig: col.editConfig,
          editable: col.editable,
          dataIndex: col.dataIndex,
          title: col.title,
          handleSave,
          style: {
            padding: 0,
            margin: 0,
            paddingLeft: 20
          }
        };
      },
    };
  });

  return (
    <Table
      expandable={{
        defaultExpandAllRows: true,
      }}
      size="small"
      components={components}
      rowClassName={() => 'editable-row'}
      bordered
      pagination={false}
      dataSource={dataSource}
      scroll={{y: 'calc(100vh - 200px)'}}
      columns={columns as ColumnTypes}
    />
  );
};

export default ParamTable;
