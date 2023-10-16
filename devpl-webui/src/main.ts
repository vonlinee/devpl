import { createApp } from "vue";
import ElementPlus from "element-plus";
import App from "./App.vue";
import { router } from "./router";
import "virtual:svg-icons-register";
import "xe-utils";
import VXETable from "vxe-table";
import SvgIcon from "@/components/svg-icon";
import "vxe-table/lib/style.css";

import "./command.ts";

// 使用svg目录下的svg文件
// import '@/icons/iconfont/iconfont'
import "element-plus/dist/index.css";
import "@/styles/index.scss";

import "./useMonaco";

VXETable.setup({
  zIndex: 3000,
  select: {
    transfer: true
  }
});

// @ts-ignore
const app: App<Element> = createApp(App);

app.use(router);
app.use(SvgIcon);
app.use(ElementPlus);
app.use(VXETable);
app.mount("#app");
