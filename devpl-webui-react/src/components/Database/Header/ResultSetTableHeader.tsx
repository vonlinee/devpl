import { Column, Header, Table } from '@tanstack/react-table';
import React, { useState, useEffect } from 'react';

type ResultSetTableHeaderProps<T> = {
  table: Table<T>;
  header: Header<T, any>;
  column: Column<T>;
};

/**
 * 表头组件
 * @param
 * @returns
 */
function ResultSetTableHeader<T>(props: ResultSetTableHeaderProps<T>) {

  return (
    <>
      <div>{props.column.id}</div>
    </>
  );
}

export default ResultSetTableHeader;
