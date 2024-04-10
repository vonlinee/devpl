import { Cell, Column, Row, Table } from '@tanstack/react-table';
import React, { useEffect, useMemo, useState } from 'react';

import menuSvg from '@/assets/images/menu.svg'

type ResultSetTableCellProps<T> = {
  table: Table<T>;
  row: Row<T>;
  column: Column<T>;
  cell: Cell<T, any>;
  getValue: () => any;
  renderValue: () => any;
};

/**
 * 单元格
 * @param props
 * @returns
 */
function ResultSetTableCell<T = any>(props: ResultSetTableCellProps<T>) {
  const { row, column, cell, getValue } = props;

  const [editing, setEditing] = useState<boolean>(false);

  const [menuVisiable, setMenuVisiable] = useState<boolean>(false);
  const initialValue = props.cell.getValue();
  // 单元格的值
  const [value, setValue] = React.useState(initialValue);

  const editElementRef = React.useRef<HTMLInputElement>(null);

  // 更新编辑状态
  const onBlur = () => {
    if (editing) {
      setEditing(false);
    }
    props.table.options.meta?.updateData(row.index, column.id, value);
  };

  const toggleEdit = function () {
    if (editing) {
      setEditing(false);
    } else {
      setEditing(true);
    }
  };

  useEffect(() => {
    if (editing) {
      editElementRef.current?.focus();
    }
  })

  useEffect(() => {
    setValue(initialValue);
  }, [initialValue]);

  const handleCellFocused = () => {
    console.log("cell focused");
  }

  return (
    <>
      {editing ? (
        <div className='resultset-data-cell'>
        <input
          ref={editElementRef}
          value={value as string}
          onChange={(e) => setValue(e.target.value)}
          onBlur={onBlur}
        />
        </div>
      ) : (
        <div className='resultset-data-cell-noedit'
          onFocusCapture={() => {
            console.log("focused");
          }}
          onMouseEnter={() => {
            setMenuVisiable(true)
          }}
          onMouseLeave={() => {
            setMenuVisiable(false)
          }}
          onDoubleClick={() => toggleEdit()} onFocus={handleCellFocused}>
          <div>{value}</div>
          {menuVisiable ? <img
            onClick={(e) => {
              e.stopPropagation()
              console.log("点击菜单");
              
            }}
            className='resultset-data-cell-menu' src={menuSvg} width={16} height={16} style={{
              marginLeft: 'auto',
              cursor: 'pointer'
            }} /> : null}
        </div>
      )}
    </>)
}

export default ResultSetTableCell;
