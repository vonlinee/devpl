import { ElMessage } from "element-plus"

const DEFAULT_DURATION = 500

export namespace Message {
  /**
   * 展示info信息
   * @param content
   * @param onClose
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
   * 展示info信息
   * @param content
   * @param onClose
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
