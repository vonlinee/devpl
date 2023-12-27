import { ElMessage } from "element-plus";

const DEFAULT_DURATION = 500

export namespace Message {

    /**
     * 展示info信息
     * @param content 
     * @param onClose 
     */
    export function info(content: string, onClose?: () => void) {
        ElMessage({
            message: content,
            duration: DEFAULT_DURATION,
            onClose: onClose
        })
    }
}



