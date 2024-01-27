<script setup>
import { arrow, useFloating } from '@floating-ui/vue';
import { onMounted, ref } from 'vue';

const reference = ref(null);
const floating = ref(null);
const floatingArrow = ref(null);


onMounted(() => {
  
})

const { floatingStyles, middlewareData } = useFloating(
  reference,
  floating,
  {
    middleware: [arrow({ element: floatingArrow })],
  },
);

floatingStyles.backgroundColor = 'red'

</script>
 
<template>
  <span ref="reference">1111111111</span>
  <div ref="floating" :style="floatingStyles">
    Floating
    <div ref="floatingArrow" :style="{
      position: 'absolute',
      left:
        middlewareData.arrow?.x != null
          ? `${middlewareData.arrow.x}px`
          : '',
      top:
        middlewareData.arrow?.y != null
          ? `${middlewareData.arrow.y}px`
          : '',
    }"></div>
  </div>
</template>