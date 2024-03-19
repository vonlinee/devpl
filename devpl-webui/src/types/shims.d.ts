declare module "*.svg"
declare module "*.png"
declare module "*.jpg"
declare module "*.gif"
declare module "*.scss"
declare module "*.ts"
declare module "*.js"

declare module "*.vue" {
  import type { DefineComponent } from "vue"
  const component: DefineComponent<{}, {}, any>
  export default component
}

declare module "v-contextmenu"

/**
 * 声明'vue3-json-viewer'模块
 */
declare module 'vue3-json-viewer';