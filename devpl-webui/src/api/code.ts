import http from "@/utils/http"

export const apiCompile = (code: string) => {
  return http.post("/api/compiler/compile", {
    code: code,
  })
}

export const apiGetCompileSampleCode = () => {
  return http.get("/api/compiler/smaple")
}

export const apiParseCode = (code: string) => {
  return http.post("/api/compiler/parse", {
    code: code,
  })
}
