import {createRouter, createWebHashHistory, RouteRecordRaw} from 'vue-router'

// 菜单路由
export const menuRoutes: RouteRecordRaw[] = [
    {
        path: '/p/gen',
        meta: {
            title: '代码生成器',
            icon: 'icon-appstore'
        },
        children: [
            {
                path: '/gen/generator',
                name: 'Generator',
                component: () => import('../views/generator/index.vue'),
                meta: {
                    title: '代码生成',
                    icon: 'icon-fire'
                }
            },
            {
                path: '/gen/datasource',
                name: 'DataSource',
                component: () => import('../views/datasource/index.vue'),
                meta: {
                    title: '数据源管理',
                    icon: 'icon-database-fill'
                }
            },
            {
                path: '/gen/field-type',
                name: 'FieldType',
                component: () => import('../views/field-type/index.vue'),
                meta: {
                    title: '字段类型映射',
                    icon: 'icon-menu'
                }
            },
            {
                path: '/gen/base-class',
                name: 'BaseClass',
                component: () => import('../views/base-class/index.vue'),
                meta: {
                    title: '基类管理',
                    icon: 'icon-cluster'
                }
            },
            {
                path: '/gen/project',
                name: 'ProjectIndex',
                component: () => import('../views/project/index.vue'),
                meta: {
                    title: '项目名变更',
                    icon: 'icon-edit-square'
                }
            },
            {
                path: '/tools/mybatis',
                name: 'MyBatis 工具',
                component: () => import('@/views/mybatis/index.vue'),
                meta: {
                    title: 'MyBatis 工具',
                    icon: 'icon-edit-square'
                }
            },
            {
                path: '/test',
                name: '测试',
                component: () => import('@/views/test/index.vue'),
                meta: {
                    title: '测试',
                    icon: 'icon-edit-square'
                }
            }, {
                path: '/gen/files',
                name: '目标文件管理',
                component: () => import('@/views/generator/genfile/index.vue'),
                meta: {
                    title: '目标文件管理',
                    icon: 'icon-menu'
                }
            },
            {
                path: '/filegen',
                name: '文件生成',
                component: () => import('@/views/generator/gen/index.vue'),
                meta: {
                    title: '文件生成',
                    icon: 'icon-menu'
                }
            },
        ]
    },
    {
        path: '/template',
        meta: {
            title: '模板管理',
            icon: 'icon-appstore'
        },
        component: () => import('@/views/template/index.vue'),
        children: []
    }
]

export const constantRoutes: RouteRecordRaw[] = [
    {
        path: '/redirect',
        component: () => import('../layout/index.vue'),
        children: [
            {
                path: '/redirect/:path(.*)',
                component: () => import('../layout/components/Router/Redirect.vue')
            }
        ]
    },
    {
        path: '/',
        component: () => import('../layout/index.vue'),
        redirect: '/gen/generator',
        children: [...menuRoutes]
    },
    {
        path: '/404',
        component: () => import('../views/404.vue')
    },
    {
        path: '/:pathMatch(.*)',
        redirect: '/404'
    }
]

export const router = createRouter({
    history: createWebHashHistory(),
    routes: constantRoutes
})
