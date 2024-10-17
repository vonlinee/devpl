import { ElMessage } from "element-plus"

const DEFAULT_DURATION = 600

export namespace Message {
  /**
   * 展示info信息
   * @param content 消息文本
   * @param onClose 消息提示弹窗关闭的回调
   */
  export function info(content: string, onClose?: () => void) {
    ElMessage({
      type: "info",
      message: content,
      duration: DEFAULT_DURATION,
      onClose: onClose,
    })
  }

    /**
   * 展示warn信息
   * @param content 消息文本
   * @param onClose 消息提示弹窗关闭的回调
   */
    export function warn(content: string, onClose?: () => void) {
      ElMessage({
        type: "warn",
        message: content,
        duration: DEFAULT_DURATION,
        onClose: onClose,
      })
    }

  /**
   * 展示error信息
   * @param content 消息文本
   * @param onClose 消息提示弹窗关闭的回调
   */
  export function error(content: string, onClose?: () => void) {
    ElMessage({
      type: "error",
      message: content,
      duration: DEFAULT_DURATION,
      onClose: onClose,
    })
  }
}
