import http from "@/utils/http"

export const useFieldTypeListApi = () => {
  return http.get("/gen/fieldtype/list")
}
