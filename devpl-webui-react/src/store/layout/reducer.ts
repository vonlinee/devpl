import * as actionTypes from "./actionTypes";
import { getLayoutMode } from "@/utils";
import { LayoutAction, LayoutMode } from "@/types"

// 放在这儿会报错
// index.ts:165 Uncaught ReferenceError: Cannot access 'LAYOUT_MODE' before initialization
// at getLayoutMode (index.ts:165:23)
// at reducer.ts:5:30
// const layout: LayoutMode[] = getLayoutMode() || [actionTypes.TWO_COLUMN];

export default function reducer(state : any, action: LayoutAction) {

  const layout: LayoutMode[] = getLayoutMode() || [actionTypes.TWO_COLUMN];

  if (state == null || state == undefined) {
    state = layout
  }
  
  const { type, mode } = action;
  switch (type) {
    case "push": {
      if (!mode) {
        return state
      }
      let lastMode = state[state.length - 1]
      if (lastMode === mode) {
        return state
      }
      const sliceNum = state.length > 1 ? 1 : 0
      state = state.slice(sliceNum).concat(mode)
      return state
    }
    case "pop": {
      if (state.length > 1) {
        state = state.slice(0, 1)
      } else {
        state = layout
      }
      return state
    }
    default: {
      return state
    }
  }
}
