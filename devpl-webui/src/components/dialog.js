import {createApp} from 'vue'
import Dialog from "./dialog.vue";

/**
 * 实现参考：https://juejin.cn/post/7136099187057721380
 * @param title
 * @param content
 * @param onCancel
 * @param onConfirm
 * @returns {App<Element>}
 */
export default ({title, content, onCancel, onConfirm}) => {
  const mountNode = document.createElement('div')
  const instance = createApp(Dialog, {
    title, modelValue: true, contentBody: content, onCancel: () => {
      instance.unmount();
      document.body.removeChild(mountNode);
      onCancel && onCancel()
    }, onConfirm: async () => {
      // if (onConfirm) {
      //   alert(onConfirm)
      //   await onConfirm()
      // }
      instance.unmount();
      document.body.removeChild(mountNode);
    }
  })

  document.body.appendChild(mountNode)
  instance.mount(mountNode)
  // 显示dialog loading
  instance.showLoading = () => {
    instance._instance.exposed.showLoading()
  };
  // 关闭dialog loading
  instance.hideLoading = () => {
    instance._instance.exposed.hideLoading()
  }
  return instance
}
