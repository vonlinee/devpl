import http from "@/utils/http"

// 获取文件目录树形结构
export function apiGetFileTree(root_path: string) {
  return http.get("/api/codegen/file-tree", {
    rootPath: root_path,
  })
}

// 获取文件内容
export function apiGetFileContent(path: string) {
  return http.get("/api/codegen/file", {
    path: path,
  })
}
