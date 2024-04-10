import '@tanstack/react-table'

declare module '@tanstack/table-core' {
  interface ColumnMeta<TData, TValue> {
    updateData: (rowIndex, columnId, value) => void
  }
}