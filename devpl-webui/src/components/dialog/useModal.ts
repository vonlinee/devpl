/**
 * https://blog.csdn.net/weixin_44869002/article/details/117631909
 * https://juejin.cn/post/7251065062131253309
 */
import { App, Component, ComputedOptions, createApp, h, MethodOptions, onBeforeUnmount, VNode, VNodeArrayChildren } from 'vue'

/**
 * 弹窗容器
 */
import Modal from './Modal.vue'
import { property } from 'xe-utils'

type RawSlots = {
	[name: string]: unknown
	$stable?: boolean
}

type RawChildren = string | number | boolean | VNode | VNodeArrayChildren | (() => any)

/**
 * 弹窗配置
 */
export interface IPopupOptions {
	rootComponent: Component
	rootProps?: Record<string, unknown> | null | undefined
	children?: RawChildren | RawSlots | undefined
}

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

/**
 * 创建一个挂载节点
 * @returns HTMLDivElement
 */
const createMountRoot = (): HTMLDivElement => {
	const el: HTMLDivElement = document.createElement('div')
	const randomStr = randomString()
	el.setAttribute('id', randomStr)
	el.setAttribute('name', randomStr)
	el.setAttribute('style', 'position: absolute; width: 100%; height: 100%;')
	return el
}

let app: App<Element> | undefined = undefined
let mounted: boolean = false

/**
 *
 * @param container 弹窗容器
 * @returns
 */
const usePopup = (container: Component<any, any, any, ComputedOptions, MethodOptions>) => {
	// 创建一个挂载节点，用于挂载弹窗
	const el: HTMLDivElement = createMountRoot()

	const createMyApp = () => createApp(h(container))

	/**
	 * 挂载弹窗
	 */
	const show = () => {
		if (!app) {
			// 只创建一个app实例
			app = createMyApp()
		}
		console.log(app)
    if (!mounted) {
      app.mount(el)
      document.body.appendChild(el)
    }
	}

	/**
	 * 销毁弹窗
	 */
	const destroy = () => {
		if (app) {
			app.unmount()
		}
		document.body.removeChild(el)
	}
	/**
	 * 关闭事件
	 */
	const close = () => {
		destroy()
	}

	/**
	 * 组件如果unmount也要执行销毁事件
	 */
	onBeforeUnmount(() => {
		destroy()
	})

	return {
		show,
		destroy,
		close
	}
}

/**
 * h(容器组件, props, 插槽)
 * @param options
 * @returns
 */
export function useModal(options: any, slots: any) {
  for (const key in Object.keys(slots)) {
    const element = slots[key];
    slots[key] = () => h(element)
  }
	let container = h(Modal, options, slots)
	return usePopup(container)
}
