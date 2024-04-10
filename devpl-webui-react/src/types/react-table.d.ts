/* eslint-disable */
import '@tanstack/react-table'

declare module '@tanstack/table-core' {
  interface ColumnMeta<TData extends RowData, TValue> {
    updateData: (rowIndex, columnId, value) => void
  }
}