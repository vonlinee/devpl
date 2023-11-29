// 可用的语言选项
const languageOptions: string[] = [
  "bat",
  "cpp",
  "css",
  "dockerfile",
  "go",
  "graphql",
  "html",
  "ini",
  "java",
  "javascript",
  "json",
  "julia",
  "kotlin",
  "less",
  "markdown",
  "mysql",
  "objective-c",
  "pascal",
  "pascaligo",
  "perl",
  "php",
  "powershell",
  "python",
  "r",
  "redis",
  "rust",
  "scala",
  "scheme",
  "scss",
  "shell",
  "sophia",
  "sql",
  "swift",
  "tcl",
  "typescript",
  "xml",
  "yaml",
]

/**
 * 支持的语言以及别名
 */
const languageMap: Map<String, String> = new Map()

languageMap.set("bat", "bat")
languageMap.set("cpp", "cpp")
languageMap.set("css", "css")
languageMap.set("dockerfile", "dockerfile")
languageMap.set("go", "go")
languageMap.set("graphql", "graphql")
languageMap.set("html", "html")
languageMap.set("ini", "ini")
languageMap.set("java", "java")
languageMap.set("javascript", "javascript")
languageMap.set("js", "javascript")
languageMap.set("json", "json")
languageMap.set("julia", "julia")
languageMap.set("kotlin", "kotlin")
languageMap.set("less", "less")
languageMap.set("markdown", "markdown")
languageMap.set("mysql", "mysql")
languageMap.set("objective-c", "objective-c")
languageMap.set("pascal", "pascal")
languageMap.set("pascaligo", "pascaligo")
languageMap.set("perl", "perl")
languageMap.set("php", "php")
languageMap.set("powershell", "powershell")
languageMap.set("python", "python")
languageMap.set("r", "r")
languageMap.set("redis", "redis")
languageMap.set("rust", "rust")
languageMap.set("swift", "swift")
languageMap.set("sql", "sql")
languageMap.set("typescript", "typescript")
languageMap.set("ts", "typescript")
languageMap.set("xml", "xml")
languageMap.set("yaml", "yaml")
languageMap.set("yml", "yaml")
languageMap.set("vue", "html")
languageMap.set("jsx", "javascript")

/**
 * 编辑器组件是否支持某种语言
 * @param lang
 */
export function isLanguageSupported(lang: string): boolean {
  return languageMap.get(lang) !== null
}

/**
 * 获取编辑器可识别的语言模式
 * @param lang
 */
export function getLanguage(lang: string): string {
  return <string>languageMap.get(lang)
}
