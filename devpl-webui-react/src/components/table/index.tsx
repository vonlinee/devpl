import EditableTable from './EditableTable';

const Test = () => {
  const columns = [
    {
      title: 'Age',
      dataIndex: 'age',
      editable: true,
    },
    {
      title: 'Name',
      dataIndex: 'name',
      editable: true,
    },
  ];

  const data = [
    {
      age: 22,
      name: 'zs'
    },
    {
      age: 22,
      name: 'ls'
    },
  ];

  return (
    <>
      <EditableTable columns={columns} data={data}></EditableTable>
    </>
  );
};

export default Test;
