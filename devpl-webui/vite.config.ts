import {resolve} from 'path'
import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import {createSvgIconsPlugin} from 'vite-plugin-svg-icons'

// 参考：https://cn.vitejs.dev/config/
export default defineConfig({
    base: './',
    resolve: {
        // 起个别名，在引用资源时，可以用‘@/资源路径’直接访问
        alias: {
            '@': resolve(__dirname, './src'),
            'vue-i18n': 'vue-i18n/dist/vue-i18n.cjs.js'
        }
    },
    plugins: [
        vue(),
        createSvgIconsPlugin({
            iconDirs: [resolve(__dirname, 'src/icons/svg')],
            symbolId: 'icon-[dir]-[name]'
        })
    ],
    server: {
        host: '0.0.0.0',
        port: 9527, // 端口号
        open: true, // 是否自动打开浏览器
        https: false, // 是否开启 https
        // https://cn.vitejs.dev/config/server-options.html#server-proxy
        // proxy: {
        //     '/api1': {
        //         // 后台地址
        //         target: 'http://127.0.0.1:8990/',
        //         changeOrigin: true,
        //         rewrite: path => path.replace(/^\/api1/, '')
        //     },
        //     '/api2': {
        //         // 后台地址
        //         target: 'http://127.0.0.1:8956/',
        //         changeOrigin: true,
        //         rewrite: path => path.replace(/^\/api2/, '')
        //     }
        // }
    },
    // 强制预构建插件包
    optimizeDeps: {
        include: [`monaco-editor/esm/vs/language/json/json.worker`,
            `monaco-editor/esm/vs/language/css/css.worker`,
            `monaco-editor/esm/vs/language/html/html.worker`,
            `monaco-editor/esm/vs/language/typescript/ts.worker`,
            `monaco-editor/esm/vs/editor/editor.worker`
        ]
    }
})
