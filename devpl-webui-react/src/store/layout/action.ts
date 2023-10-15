import { LayoutMode, LayoutModeType } from "@/types";

import { AnyAction } from "redux";

/**
 * 改变布局模式
 * @param type action
 * @param mode 布局模式
 * @returns 
 */
export const changeLayoutMode = (
  type: LayoutModeType,
  mode?: LayoutMode
): AnyAction => ({
  type,
  mode,
});
