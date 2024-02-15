import { toRaw } from "vue"

import "xe-utils"
import VXETable from "vxe-table"
import "vxe-table/lib/style.css"
// 配置vxe-table
VXETable.config({
  zIndex: 2000,
  select: {
    transfer: true,
  },
})

// 添加全局自定义指令
// https://vxetable.cn/v4/#/table/commands/api

/**
 * 打开弹窗
 * 在toolbarConfig.buttons 的 params 配置上添加 modalModelValue, 值为 Vue 的 Ref 类型
 */
VXETable.commands.add("openModal", {
  commandMethod({ button }): void {
    let params = toRaw(button?.params)
    if (params.modalModelValue) {
      params.modalModelValue.value = true
    }
  },
})

export default VXETable