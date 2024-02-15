import http from "@/utils/http"

/**
 * 上传单个文件
 * @param folder 存放目录
 * @param file
 */
export const apiUploadSingleFile = (folder: string, file: File) => {
  return http.post("/api/file/upload/single", {
    folder: folder,
    filename: file.name,
    file: file,
    chunks: 1,
    chunk: 0,
  })
}

/**
 * 上传多个文件
 * @param param
 */
export const apiUploadMultiFiles = (param: FormData) => {
  return http.post("/api/file/upload/single", param)
}

/**
 * 获取服务器文件目录结构
 * @param parent 上级目录
 */
export const apiGetFileSystemTree = (parent?: string) => {
  return http.get("/api/file/fs/tree", {
    parent: parent
  })
}