import DataTable, {
  DataTableOptions,
  RowDataModel,
} from "@/components/datatable";
import { useDataTable } from "@/components/datatable/datatable";
import { ReactMonacoEditor } from "@/components/editor/ReactMonacoEditor";
import { Button } from "antd";
import { ColumnsType } from "antd/es/table";
import React from "react";

/**
 * 测试
 */
const Test = () => {
  let text = "";

  let ref = React.createRef<ReactMonacoEditor>();

  const getText = () => {
    console.log(ref.current?.getText());
  };

  const setText = () => {
    ref.current?.setText("hello world");
  };

  interface DataType {
    key: React.Key;
    name: string;
    age: number;
    address: string;
  }

  const data: RowDataModel[] = [];
  for (let i = 0; i < 100; i++) {
    data.push({
      key: i,
      name: `Edward ${i}`,
      age: 32,
      address: `London Park no. ${i}`,
    });
  }

  const options: DataTableOptions = useDataTable({
    columns: [
      {
        title: "Full Name",
        width: 100,
        dataIndex: "name",
        key: "name",
        fixed: "left",
      },
      {
        title: "Age",
        width: 100,
        dataIndex: "age",
        key: "age",
        fixed: "left",
      },
      {
        title: "Column 1",
        dataIndex: "address",
        key: "1",
        width: 150,
      },
      {
        title: "Column 2",
        dataIndex: "address",
        key: "2",
        width: 150,
      },
      {
        title: "Column 3",
        dataIndex: "address",
        key: "3",
        width: 150,
      },
      {
        title: "Column 4",
        dataIndex: "address",
        key: "4",
        width: 150,
      },
      {
        title: "Column 5",
        dataIndex: "address",
        key: "5",
        width: 150,
      },
    ],
    pageable: true,
    data: data,
  });

  return (
    <>
      <DataTable options={options}></DataTable>

      <Button onClick={getText}>getText</Button>
      <Button onClick={setText}>setText</Button>
      <ReactMonacoEditor
        ref={ref}
        value={text}
        width={"100%"}
        height={"100%"}
      ></ReactMonacoEditor>
    </>
  );
};

Test.route = {
  [MENU_TITLE]: "test页面",
  [MENU_KEY]: "test",
  [MENU_PATH]: "/power/test",
};

export default Test;
