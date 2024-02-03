import http from "@/utils/http"
import { Keys } from "@/api/index"

export const apiGetModelById = (id: Number) => {
  return http.get("/api/model/" + id)
}

export const apiListBaseClass = (
  page: number = 1,
  limit: number = 10,
  params?: any
) => {
  return http.get("/api/model/list/page", {
    pageIndex: page,
    pageSize: limit,
    ...params,
  })
}

export const apiBatchRemoveModelById = (ids: Keys) => {
  return http.delete("/api/model/remove", ids)
}


export const apiRemoveModelField = (modelId: number, fieldIds: Keys) => {
  return http.delete("/api/model/field/remove", {
    modelId: modelId,
    fieldIds: fieldIds
  })
}

export const apiSaveOrUpdateModelById = (dataForm: any) => {
  if (dataForm.id) {
    return http.put("/api/model/update", dataForm)
  } else {
    return http.post("/api/model/save", dataForm)
  }
}
