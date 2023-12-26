import { ElMessage } from "element-plus";
import { Ref } from "vue";

/**
 * 消息提示
 * @param visiable 控制弹窗的打开和关闭
 * @returns 
 */
export const useMessage = (visiable : Ref<boolean>) => {
    return {
        info: (msg: string) => {
            ElMessage({
                message: msg,
                duration: 300,
                zIndex: 4000,
                onClose: () => {
                    visiable.value = false;
                }
            })
        }
    }
}














