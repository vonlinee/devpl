import Dirent, { readdirSync, readFileSync } from "fs"

let idPerfix: string = ""
const svgTitle: RegExp = /<svg([^>+].*?)>/
const clearHeightWidth: RegExp = /(width|height)="([^>+].*?)"/g

const hasViewBox: RegExp = /(viewBox="[^>+].*?")/g

const clearReturn: RegExp = /(\r)|(\n)/g

// @ts-ignore
function findSvgFile(dir: string) {
  const svgRes = []
  const dirents: Dirent.Dirent[] = readdirSync(dir, {
    withFileTypes: true,
  })
  for (const dirent of dirents) {
    if (dirent.isDirectory()) {
      svgRes.push(...findSvgFile(dir + dirent.name + "/"))
    } else {
      const svg: string = readFileSync(dir + dirent.name)
        .toString()
        .replace(clearReturn, "")
        .replace(svgTitle, ($1: string, $2): string => {
          // console.log(++i)
          // console.log(dirent.name)
          let width: number = 0
          let height: number = 0
          let content = $2.replace(
            clearHeightWidth,
            (s1: string, s2: string, s3: number): string => {
              if (s2 === "width") {
                width = s3
              } else if (s2 === "height") {
                height = s3
              }
              return ""
            }
          )
          if (!hasViewBox.test($2)) {
            content += `viewBox="0 0 ${width} ${height}"`
          }
          return `<symbol id="${idPerfix}-${dirent.name.replace(
            ".svg",
            ""
          )}" ${content}>`
        })
        .replace("</svg>", "</symbol>")
      svgRes.push(svg)
    }
  }
  return svgRes
}

export const svgBuilder = (path: string, perfix: string = "icon") => {
  if (path === "") {
    return
  }
  idPerfix = perfix
  const res = findSvgFile(path)
  // console.log(res.length)
  // const res = []
  return {
    name: "svg-transform",
    transformIndexHtml(html: string): string {
      return html.replace(
        "<body>",
        `
          <body>
            <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" style="position: absolute; width: 0; height: 0">
              ${res.join("")}
            </svg>
        `
      )
    },
  }
}
