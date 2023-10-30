import { createApp } from "vue"
import ElementPlus from "element-plus"
import App from "./App.vue"
import { router } from "./router"
import "virtual:svg-icons-register"
import "xe-utils"
import VXETable from "vxe-table"
import SvgIcon from "@/components/svg-icon"
import "vxe-table/lib/style.css"

import { createPinia, Pinia } from "pinia"

import "./command.ts"

// 使用svg目录下的svg文件
// import '@/icons/iconfont/iconfont'
import "element-plus/dist/index.css"
import "@/styles/index.scss"

import "./useMonaco"
import { isWindows } from "./utils/tool"

VXETable.setup({
  zIndex: 2000,
  select: {
    transfer: true,
  },
})

// @ts-ignore
const app: App<Element> = createApp(App)

// 创建 Pinia 实例
const pinia: Pinia = createPinia()

// 挂载到 Vue 根实例
app.use(pinia)
app.use(router)
app.use(SvgIcon)
app.use(ElementPlus)
app.use(VXETable)
app.mount("#app")

window.addEventListener("keydown", function (e) {
  // 只禁用ctrl+s保存功能
  if (isWindows() ? e.ctrlKey : e.metaKey) {
    if (e.key === "s" && !e.altKey) {
      e.preventDefault()
    }
  }
})
