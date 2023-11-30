import { Ref } from "vue";
import Sortable from "sortablejs";

const sortIndex = (tableData: any[]) => {
  const array = [];
  tableData.forEach((e, i) => {
    const obj = {
      index: i + 1
    };
    array.push(obj);
  });
};

/**
 * TODO 使用此方法不生效，但是在组件中写此方法的逻辑可以生效
 * @param tableRef
 * @param tableDataRef
 * @param rowClassName
 */
export function useElTableDragDrop<T>(
  tableRef: Ref,
  tableDataRef: Ref<T[]>,
  rowClassName: string) {
  const elTableBody = tableRef.value.$refs.tableBody.querySelector("tbody");
  console.log(elTableBody);
  if (elTableBody) {
    return Sortable.create(elTableBody, {
      disabled: false, // 是否开启拖拽
      ghostClass: "sortable-ghost", //拖拽样式
      animation: 150, // 拖拽延时，效果更好看
      handle: rowClassName,
      /**
       * 进行数据的处理，拖拽实际并不会改变绑定数据的顺序
       * @param e
       */
      onEnd: (e: any) => {
        // oldIndex为列表元素开始拖拽的索引，newIndex为列表元素结束拖拽的索引
        const { newIndex, oldIndex } = e;
        // 拖拽的行
        const curRow = tableDataRef.value.splice(oldIndex, 1)[0];
        // 将拖拽的行插入到拖拽结束的位置
        tableDataRef.value.splice(newIndex, 0, curRow);
        sortIndex(tableDataRef.value);
      }
    });
  }
  return null;
}
