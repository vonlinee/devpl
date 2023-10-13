import { getCompVisible } from "@/utils";
import { State } from "@/types"


export default function reducer(state : any, action: {
  type: string
  key: keyof State["componentsVisible"]
  val: boolean
}) {

  if (state == undefined || state == null) {
    const visible = getCompVisible() || { footer: true, topMenu: true };
    state = visible
  }

  const { type, key, val } = action;
  switch (type) {
    case "set": {
      return { ...state, [key]: val };
    }
    default: {
      return state;
    }
  }
}
