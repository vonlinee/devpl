import { h } from "vue"
import { ElMessage } from "element-plus"

const createVNode = () => {
  h("div", {})
}

export const showException = (stackTrace: string) => {
  ElMessage({
    message: h("p", null, [
      h("div", { style: "color: teal", innerHtml: stackTrace }, stackTrace),
    ]),
    showClose: true,
    // 设置为0表示该消息不会被自动关闭
    duration: 0,
  })
}
