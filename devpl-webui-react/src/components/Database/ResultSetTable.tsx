import React from 'react';

import './index.less';


import { useReactTable, getCoreRowModel, ColumnDef, flexRender } from '@tanstack/react-table';
import ResultSetTableHeader from './Header/ResultSetTableHeader';
import ResultSetTableCell from './Cells/ResultSetTableCell';

// 行数据
type ResultSetTableRow = Record<string, any>;

const defaultData: ResultSetTableRow[] = [
  {
    firstName: 'tanner',
    lastName: 'linsley',
    age: 24,
    visits: 100,
    status: 'In Relationship',
    progress: 50,
  },
  {
    firstName: 'tandy',
    lastName: 'miller',
    age: 40,
    visits: 40,
    status: 'Single',
    progress: 80,
  },
  {
    firstName: 'joe',
    lastName: 'dirte',
    age: 45,
    visits: 20,
    status: 'Complicated',
    progress: 10,
  },
];

const defaultColumns: ColumnDef<ResultSetTableRow>[] = [
  {
    accessorKey: 'age',
    header: ResultSetTableHeader,
    cell: ResultSetTableCell,
    footer: (props) => props.column.id,
  },
  {
    accessorKey: 'visits',
    header: ResultSetTableHeader,
    cell: ResultSetTableCell,
    footer: (props) => props.column.id,
  },
  {
    accessorKey: 'status',
    header: ResultSetTableHeader,
    cell: ResultSetTableCell,
    footer: (props) => props.column.id,
  },
  {
    accessorKey: 'progress',
    header: ResultSetTableHeader,
    cell: ResultSetTableCell,
    footer: (props) => props.column.id,
  },
];

/**
 * 展示数据库一张表的数据
 * @returns
 */
function ResultSetTable() {
  const [data, setData] = React.useState(() => [...defaultData]);
  const [columns] = React.useState<typeof defaultColumns>(() => [...defaultColumns]);

  const table = useReactTable({
    data,
    columns,
    columnResizeMode: 'onChange',
    getCoreRowModel: getCoreRowModel(),
    // Provide our updateData function to our table meta
    meta: {
      updateData: (rowIndex: number, columnId: string, value: any) => {
        // Skip page index reset until after next rerender
        setData((old) =>
          old.map((row, index) => {
            if (index === rowIndex) {
              return {
                ...old[rowIndex]!,
                [columnId]: value,
              };
            }
            return row;
          }),
        );
      },
    },
    // debugTable: true,
    // debugHeaders: true,
    // debugColumns: true,
  });

  return (
    <div className="database-table-container">
      <div className="overflow-x-auto">
        <table
          {...{
            style: {
              tableLayout: 'fixed',
              width: table.getCenterTotalSize(),
            },
          }}
        >
          <thead>
            {table.getHeaderGroups().map((headerGroup) => (
              <tr key={headerGroup.id}>
                {headerGroup.headers.map((header) => (
                  <th
                    {...{
                      key: header.id,
                      colSpan: header.colSpan,
                      style: {
                        width: header.getSize(),
                      },
                    }}
                  >
                    {header.isPlaceholder
                      ? null
                      : flexRender(header.column.columnDef.header, header.getContext())}
                    <div
                      {...{
                        onMouseDown: header.getResizeHandler(),
                        onTouchStart: header.getResizeHandler(),
                        className: `resizer ${header.column.getIsResizing() ? 'resizing' : ''}`,
                      }}
                    />
                  </th>
                ))}
              </tr>
            ))}
          </thead>
          <tbody>
            {table.getRowModel().rows.map((row) => (
              <tr key={row.id}>
                {row.getVisibleCells().map((cell) => (
                  <td
                    className="resultset-data-cell"
                    key={cell.id}
                    style={{
                      overflow: 'hidden',
                      textOverflow: 'ellipsis',
                      whiteSpace: 'nowrap',
                      width: cell.column.getSize(),
                    }}
                  >
                    {flexRender(cell.column.columnDef.cell, cell.getContext())}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div className="h-4" />
    </div>
  );
}

export default ResultSetTable;
