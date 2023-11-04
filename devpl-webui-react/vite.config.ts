import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import ReactRouterGenerator from "vite-plugin-react-router-generator";
import { resolve } from "path";
import { theme } from "antd";
import lessToJs from "less-vars-to-js";
import { readFileSync } from "fs";

const { defaultAlgorithm, defaultSeed } = theme;
const varLessPath = "./theme/var.less";
const mapToken = defaultAlgorithm(defaultSeed);
const varLessStr = readFileSync(varLessPath, "utf-8");
const customVarLessJson = lessToJs(varLessStr, {
  resolveVariables: true,
  stripPrefix: true,
});

/**
 * https://vitejs.dev/config/
 */
export default defineConfig({
  // 开发或生产环境服务的公共基础路径
  base: "/devpl/",
  build: {
    outDir: "build",
    sourcemap: true,
  },
  // 用于加载 .env 文件的目录。可以是一个绝对路径，也可以是相对于项目根的路径
  envDir: "./",
  // 定义全局常量替换方式。其中每项在开发环境下会被定义在全局，而在构建时被静态替换
  define: {
    MENU_PATH: `"path"`,
    MENU_SHOW: `"isShowOnMenu"`,
    MENU_KEEPALIVE: `"keepAlive"`,
    MENU_KEY: `"key"`,
    MENU_ICON: `"icon"`,
    MENU_TITLE: `"title"`,
    MENU_CHILDREN: `"children"`,
    MENU_PARENTKEY: `"parentKey"`,
    MENU_ALLPATH: `"allPath"`,
    MENU_PARENTPATH: `"parentPath"`,
    MENU_LAYOUT: `'layout'`,
    __IS_THEME__: `${process.env.REACT_APP_COLOR === "1"}`,
    CUSTOMVARLESSDATA: `${JSON.stringify(customVarLessJson)}`,
  },
  plugins: [
    // 文档参考: https://gitee.com/kong_yiji_and_lavmi/vite-plugin-react-router-generator
    ReactRouterGenerator({
      // 生成路由列表信息的文件路径。
      outputFile: resolve(".", "./src/router/auto.tsx"),
      // 导出的组件是否为懒加载
      isLazy: true,
      // 导出路由文件的 key
      comKey: "components",
      // 需要匹配的文件后缀名
      // exts: [".tsx"]
    }),
    react(),
  ],
  resolve: {
    alias: {
      "@": resolve(".", "./src"),
      "~": resolve(".", "./node_modules"),
    },
    extensions: [".mjs", ".js", ".ts", ".jsx", ".tsx", ".json", ".less"],
  },
  css: {
    preprocessorOptions: {
      less: {
        modifyVars: { ...mapToken, ...customVarLessJson },
      },
    },
  },
  server: {
    port: 8421,
    // 启动时打开浏览器
    open: true,
    host: '0.0.0.0',
    // 设为 true 时若端口已被占用则会直接退出，而不是尝试下一个可用端口
    strictPort: true,
    https: false, // 是否开启 https
    // proxy: {
    //   "^/api": {
    //     target: "localhost:8088/devpl",
    //     changeOrigin: true,
    //     rewrite: (path) => {
    //       return path.replace("/api", "/api/");
    //     },
    //   },
    // },
  },
  // 以 envPrefix 开头的环境变量会通过 import.meta.env 暴露在你的客户端源码中。
  // 即只有envPrefix指定的环境变量可以在代码中使用
  envPrefix: "REACT_APP_",
  // 强制预构建插件包
  optimizeDeps: {
    esbuildOptions: {
      plugins: [],
    },
    include: [
      `monaco-editor/esm/vs/language/json/json.worker`,
      `monaco-editor/esm/vs/language/css/css.worker`,
      `monaco-editor/esm/vs/language/html/html.worker`,
      `monaco-editor/esm/vs/language/typescript/ts.worker`,
      `monaco-editor/esm/vs/editor/editor.worker`,
    ],
  },
});
