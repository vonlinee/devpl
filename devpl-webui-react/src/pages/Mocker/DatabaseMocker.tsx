import { Col, Row, Table } from 'antd';

type DatabaseMockerProps = {
  param: ContextMenuParam;
};

interface ColumnGenItem {
  key: string;
  fieldName: string;
  generatorId: string;
}

export default function DatabaseMocker(props: DatabaseMockerProps) {
  const { param } = props;

  const columns = [
    {
      title: 'Field',
      dataIndex: 'fieldName',
      render: (text) => <a>{text}</a>,
    },
    {
      title: 'Generator',
      dataIndex: 'generatorId',
    },
  ];

  return (
    <>
      <div>数据源: {param.dataSourceId}</div>
      <div>数据库名: {param.databaseName}</div>
      <div>表名: {param.tableName}</div>
      <Row>
        <Col>
          <Table size="small" bordered columns={columns}/>
        </Col>
      </Row>
    </>
  );
}
