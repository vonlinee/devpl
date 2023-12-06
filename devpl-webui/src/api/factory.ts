import http from "@/utils/http"

// 获取文件目录树形结构
export function apiGetFileTree(rootPath: string) {
  return http.get("/api/codegen/file-tree", {
    rootPath: rootPath,
  })
}

// 获取文件内容
export function apiGetFileContent(path: string) {
  return http.get("/api/codegen/file", {
    path: path,
  })
}
