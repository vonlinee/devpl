import http from "@/utils/http"

export const apiListMockFields = (
  dsId: number,
  dbName: string,
  tableName: string
) => {
  return http.get<MockField[]>("/api/tools/mock/columns", {
    dataSourceId: dsId,
    databaseName: dbName,
    tableName: tableName,
  })
}

export const apiListMockGenerators = () => {
  return http.get<GeneratorItem[]>("/api/tools/mock/generators")
}
