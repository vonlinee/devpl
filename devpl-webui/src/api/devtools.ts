import http from "@/utils/http";

export const apiModel2Ddl = (code: string) => {
  return http.post("/api/devtools/ddl", {
    content: code
  });
};
