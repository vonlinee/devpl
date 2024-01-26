<!-- 
  列表组件
 -->
<script lang="ts">
import { h, defineComponent, PropType, VNode } from 'vue';

export default defineComponent({
  slots: {
    default: () => any
  },
  props: {
    name: String,
    items: Array as PropType<any[]>
  },
  setup(props, ctx) {

    const { items, name } = props

    const nodes: VNode[] = []

    if (items && name) {
      for (let i = 0; i < items?.length; i++) {



        nodes.push(h(name, {}, ctx.slots.default()))
      }
    }

    return () => h("div", nodes)
  },
})

</script>