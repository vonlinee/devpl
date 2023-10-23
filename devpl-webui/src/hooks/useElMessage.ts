import { ElMessage } from "element-plus";
import { Ref } from "vue";

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














