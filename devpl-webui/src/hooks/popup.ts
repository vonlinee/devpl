
import {
  App,
  Component,
  ComputedOptions,
  MethodOptions,
  VNode,
  VNodeArrayChildren,
  createApp,
  h,
  onBeforeUnmount,
} from 'vue'

/**
 *
 * @returns 随机字符串
 */
function randomString() {
	let chars = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'
	var result = ''
	for (var i = length; i > 0; --i) result += chars[Math.floor(Math.random() * chars.length)]
	return result
}

type RawSlots = {
  [name: string]: unknown
  $stable?: boolean
}

type RawChildren =
  | string
  | number
  | boolean
  | VNode
  | VNodeArrayChildren
  | (() => any)

/**
 * 弹窗配置
 */
export interface IPopupOptions {
  rootComponent: Component<any, any, any, ComputedOptions, MethodOptions>
  rootProps?: Record<string, unknown> | null | undefined
  children?: RawChildren | RawSlots | undefined
}

/**
 * 创建一个挂载节点
 * @returns HTMLDivElement
 */
const createMountRoot = () => {
  const el = document.createElement('div')
  const randomStr = randomString()
  el.setAttribute('id', randomStr)
  el.setAttribute('name', randomStr)
  el.setAttribute('style', 'position: absolute; width: 100%; height: 100%;')
  return el;
}
export const usePopup = (
  container: Component<any, any, any, ComputedOptions, MethodOptions>
) => {

  // 创建一个挂载节点，用于挂载弹窗
  const el = createMountRoot();

  const createMyApp = () => createApp(h(container))
  let app: App<Element>;

  /**
   * 挂载弹窗
   */
  const show = () => {
    app = createMyApp()
    app.mount(el)
    document.body.appendChild(el)
  }

  /**
   * 销毁弹窗
   */
  const destroy = () => {
    if (app) {
      app.unmount()
    }
    document.body.removeChild(el);
  }
  /**
   * 关闭事件
   */
  const close = () => {
    destroy();
  }

  /**
   * 组件如果unmount也要执行销毁事件
   */
  onBeforeUnmount(() => {
    destroy();
  })

  return {
    show,
    destroy,
    close,
  }
}

