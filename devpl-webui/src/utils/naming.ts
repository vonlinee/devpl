export interface NamingStrategy {
  label: string
  key: string
  transform: (input: string) => string
}

/**
 * 所有命名策略
 */
export const namingStyles: NamingStrategy[] = [
  {
    label: "保持原样",
    key: "none",
    transform: (input: string) => {
      return input
    },
  },
  {
    label: "小驼峰",
    key: "camelCase",
    transform: (input: string) => {
      return input.replace(/_(\w)/g, function (all, letter) {
        return letter.toUpperCase()
      })
    },
  },
  {
    label: "大驼峰",
    key: "PascalCase",
    transform: (input: string) => {
      return input.replace(/\_(\w)/g, function (all, letter) {
        return letter.toUpperCase()
      })
    },
  },
  {
    label: "下划线(小写)",
    key: "snake_case",
    transform: (input: string) => {
      return input.replace(/([A-Z])/g, "_$1").toLowerCase()
    },
  },
  {
    label: "下划线(大写)",
    key: "SNAKE_CASE",
    transform: (input: string) => {
      return input.replace(/([A-Z])/g, "_$1").toUpperCase()
    },
  },
]
