import http from "@/utils/http"

/**
 * 获取单个项目信息
 * @param id
 */
export const apiGetProjectInfo = (id: number) => {
  return http.get("/api/project/" + id)
}

/**
 * 获取可选择的项目列表
 */
export const apiListSelectableProjects = () => {
  return http.get<ProjectSelectVO[]>("/api/project/selectable")
}

/**
 * 更新项目信息
 * @param dataForm
 */
export const apiUpdateProjectInfo = (dataForm: any) => {
  if (dataForm.id) {
    return http.put("/api/project", dataForm)
  } else {
    return http.post("/api/project", dataForm)
  }
}

/**
 * 项目源码下载
 * @param id
 */
export const apiDownloadProject = (id: string) => {
  location.href = import.meta.env.VITE_API_URL + "/api/project/download/" + id
}

/**
 * 项目源码下载
 * @param id
 */
export const apiAnalyseProject = (path: string) => {
  return http.get("/api/project/analyse", {
    path: path
  })
}
