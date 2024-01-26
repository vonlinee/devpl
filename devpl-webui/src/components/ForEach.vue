<!-- 
  列表组件
 -->
<script lang="ts">
import { h, defineComponent, PropType, VNode } from "vue"
import { VNodeArrayChildren, RendererNode, RendererElement } from "vue-demi"

export default defineComponent({
  props: {
    /**
     * 行高
     */
    rowHeight: Number,
    /**
     * 渲染子节点的名称
     */
    name: String,
    /**
     * 数据列表
     */
    items: Array as PropType<any[]>,
  },
  setup(props, { slots }) {
    const { items, name, rowHeight = 20 } = props

    const nodes: ChildrenVNodes = []

    if (items && name) {
      for (let i = 0; i < items?.length; i++) {
        if (slots.default) {
          const defaultSlot = slots?.default({
            item: items[i],
          })
          nodes.push(
            h(
              name,
              {
                style: {
                  height: rowHeight,
                },
              },
              defaultSlot
            )
          )
        }
      }
    }

    return () => h("div", {}, nodes)
  },
})
</script>
