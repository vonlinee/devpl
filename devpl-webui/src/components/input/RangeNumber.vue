<template>
  <div
    class="number-range"
    :class="{ 'is-disabled': disabled, 'is-focus': isFocus }"
  >
    <el-input-number
      v-model="minValue_"
      :disabled="disabled"
      placeholder="最小值"
      v-bind="$attrs"
      :controls="false"
      @blur="handleBlur"
      @focus="handleFocus"
      @change="handleChangeMinValue"
      v-on="['update:minValue']"
    />
    <div class="to">
      <span>{{ to }}</span>
    </div>
    <el-input-number
      v-model="maxValue_"
      :disabled="disabled"
      placeholder="最大值"
      v-bind="$attrs"
      :controls="false"
      @blur="handleBlur"
      @focus="handleFocus"
      @change="handleChangeMaxValue"
      v-on="['update:maxValue']"
    />
    <div id="append">
      <slot name="append">
        <!-- 后缀插槽 -->
      </slot>
    </div>
  </div>
</template>
<script lang="ts" setup name="RangeNumber">
import { computed, ref } from "vue"

const props = defineProps({
  minValue: {
    type: Number,
    default: null, // 调用时使用v-model:min-value="" 绑定多个v-model
  },
  maxValue: {
    type: Number,
    default: null, // 调用时使用v-model:max-value="" 绑定多个v-model
  },
  // 是否禁用
  disabled: {
    type: Boolean,
    default: false,
  },
  to: {
    type: String,
    default: "至",
  },
  // 精度参数 -保留小数位数
  precision: {
    type: Number,
    default: 0,
    validator(val: number) {
      return val >= 0 && val === parseInt(String(val), 10)
    },
  },
  // 限制取值范围
  valueRange: {
    type: Array,
    default: () => [],
    validator(val: []) {
      if (val && val.length > 0) {
        // @ts-ignore
        if (val.length !== 2) {
          throw new Error("请传入长度为2的Number数组")
        }
        // @ts-ignore
        if (typeof val[0] !== "number" || typeof val[1] !== "number") {
          throw new Error("取值范围只接受Number类型,请确认")
        }
        // @ts-ignore
        if (val[1] < val[0]) {
          throw new Error("valueRange格式须为[最小值,最大值],请确认")
        }
      }
      return true
    },
  },
})

const emit = defineEmits(["update:minValue", "update:maxValue", "change"])

const minValue_ = computed({
  get() {
    return props.minValue
  },
  set(value) {
    emit("update:minValue", value)
  },
})

const maxValue_ = computed({
  get() {
    return props.maxValue
  },
  set(value) {
    emit("update:maxValue", value)
  },
})

const handleChangeMinValue = (value: number) => {
  // 非数字空返回null
  if (isNaN(value)) {
    emit("update:minValue", null)
    return
  }
  // 初始化数字精度
  const newMinValue = parsePrecision(value, props.precision)
  // min > max 交换min max
  if (
    typeof newMinValue === "number" &&
    parseFloat(String(newMinValue)) > parseFloat(String(props.maxValue))
  ) {
    // 取值范围判定
    const { min, max } = decideValueRange(props.maxValue, newMinValue)
    emit("update:minValue", min)
    emit("update:maxValue", max)
    emit("change", { min, max })
  } else {
    // 取值范围判定
    const { min, max } = decideValueRange(newMinValue, props.maxValue)
    emit("update:minValue", min)
    emit("change", { min, max })
  }
}

const handleChangeMaxValue = (value: number) => {
  // 非数字空返回null
  if (isNaN(value)) {
    emit("update:maxValue", null)
    return
  }
  // 初始化数字精度
  const newMaxValue = parsePrecision(value, props.precision)
  // max < min 交换min max
  if (
    typeof newMaxValue === "number" &&
    parseFloat(String(newMaxValue)) < parseFloat(String(props.minValue))
  ) {
    // 取值范围判定
    const { min, max } = decideValueRange(newMaxValue, props.minValue)
    emit("update:maxValue", max)
    emit("update:minValue", min)
    emit("change", { min, max })
  } else {
    // 取值范围判定
    const { min, max } = decideValueRange(props.minValue, newMaxValue)
    emit("update:maxValue", max)
    emit("change", { min, max })
  }
}

// 取值范围判定
const decideValueRange = (min: number, max: number) => {
  if (props.valueRange && props.valueRange.length > 0) {
    // @ts-ignore
    min =
      min < props.valueRange[0]
        ? props.valueRange[0]
        : min > props.valueRange[1]
        ? props.valueRange[1]
        : min
    // @ts-ignore
    max = max > props.valueRange[1] ? props.valueRange[1] : max
  }
  return { min, max }
}

// input焦点事件
const isFocus = ref()

const handleFocus = () => {
  isFocus.value = true
}

const handleBlur = () => {
  isFocus.value = false
}

// 处理数字精度
const parsePrecision = (number: number, precision = 0) => {
  return parseFloat(
    String(
      Math.round(number * Math.pow(10, precision)) / Math.pow(10, precision)
    )
  )
}
</script>
<style lang="scss" scoped>
.number-range {
  background-color: var(--el-bg-color) !important;
  box-shadow: 0 0 0 1px var(--el-input-border-color, var(--el-border-color))
    inset;
  border-radius: var(--el-input-border-radius, var(--el-border-radius-base));
  padding: 0 2px;
  display: flex;
  flex-direction: row;
  width: 100%;
  height: auto;
  justify-content: center;
  align-items: center;
  color: var(--el-input-text-color, var(--el-text-color-regular));
  transition: var(--el-transition-box-shadow);
  transform: translate3d(0, 0, 0);

  .to {
    margin-top: 1px;
  }
}

.is-focus {
  transition: all 0.3s;
  box-shadow: 0 0 0 1px var(--el-color-primary) inset !important;
}

.is-disabled {
  background-color: var(--el-input-bg-color, var(--el-fill-color-blank));
  color: var(--el-input-text-color, var(--el-text-color-regular));
  cursor: not-allowed;
  box-shadow: none;
}

#append {
  &:has(div),
  &:has(span),
  &:has(p) {
    color: var(--el-color-info);
    padding: 0 10px;
  }
}

:deep(.el-input) {
  border: none;
}

:deep(.el-input__wrapper) {
  margin: 0;
  padding: 0 15px;
  background-color: transparent;
  border: none !important;
  box-shadow: none !important;

  &.is-focus {
    border: none !important;
    box-shadow: none !important;
  }
}
</style>
