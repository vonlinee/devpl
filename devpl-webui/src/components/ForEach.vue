<!-- 
  列表组件
 -->
<script lang="ts">
import { h, defineComponent, PropType } from "vue"

export default defineComponent({
  props: {
    /**
     * 行高
     */
    rowHeight: Number,
    /**
     * 渲染子节点的名称，如果不传，则需要在插槽内指定每项的父节点
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

    if (items) {
      for (let i = 0; i < items?.length; i++) {
        if (slots.default) {
          const defaultSlot = slots?.default({
            item: items[i]
          })

          if (name) {
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
          } else {
            // 无法在子组件给插槽节点添加样式
            nodes.push(defaultSlot)
          }
        }
      }
    }
    return () => h("div", {}, nodes)
  },
})
</script>
