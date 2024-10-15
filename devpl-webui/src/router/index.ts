import {
  createRouter,
  createWebHashHistory,
  Router,
  RouteRecordRaw,
} from "vue-router"

/**
 * 菜单路由
 * 存放了所有前端页面所有菜单
 * 目录级别的菜单不需要指定component，否则会导致子菜单跳转的路由均为该目录的component指定的组件
 * 
 * https://router.vuejs.org/zh/guide/
 */
export const menuRoutes: RouteRecordRaw[] = [
  {
    path: "/datasource",
    meta: {
      title: "数据源管理",
      icon: "datasource",
    },
    children: [
      // {
      //   path: "/datasource/driver",
      //   name: "DriverManager",
      //   meta: {
      //     title: "驱动管理",
      //     icon: "icon-appstore",
      //   },
      //   component: () => import("@/views/datasource/driver/index.vue")
      // },
      {
        path: "/datasource/connection-info",
        name: "ConnectionInfo",
        meta: {
          title: "连接信息",
          icon: "database-connection-filled",
        },
        component: () => import("@/views/datasource/index.vue"),
        children: [],
      },
    ]
  },
  {
    path: "/codegen",
    meta: {
      title: "代码生成器",
      icon: "code_generation",
    },
    children: [
      {
        path: "/codegen/file",
        name: "Generator",
        component: () => import("@/views/generator/index.vue"),
        meta: {
          title: "文件生成",
          icon: "file-create",
        },
      },
      {
        path: "/codegen/targetfile",
        name: "TargetFile",
        component: () => import("@/views/generator/targetfile/index.vue"),
        meta: {
          title: "文件类型",
          icon: "file",
        },
      },
      {
        path: "/codegen/project",
        name: "ProjectIndex",
        component: () => import("@/views/project/index.vue"),
        meta: {
          title: "项目管理",
          icon: "project",
        },
      },
    ],
  },
  {
    path: "/databaseManager",
    meta: {
      title: "数据库管理",
      icon: "database-empty"
    },
    children: [
      {
        path: "/databaseManager/model",
        name: "DatabaseModelManage", // name 字段唯一
        component: () => import("@/views/dbm/database/index.vue"),
        meta: {
          title: "模型管理",
          icon: "modelbim",
        },
      },
      {
        path: "/databaseManager/table",
        name: "DatabaseTableDesigner",
        component: () => import("@/views/dbm/table/index.vue"),
        meta: {
          title: "数据库表设计",
          icon: "table-design",
        },
      }
    ],
  },
  {
    path: "/model",
    name: "模型管理",
    meta: {
      title: "模型管理",
      icon: "domain-model",
    },
    children: [
      {
        path: "/model/datatype",
        name: "FieldType",
        component: () => import("@/views/datatype/index.vue"),
        meta: {
          title: "类型系统",
          icon: "data-type",
        },
      },
      {
        path: "/model/fields",
        name: "FieldInfo",
        meta: {
          title: "字段信息",
          icon: "fieldset",
        },
        component: () => import("@/views/fields/index.vue"),
      },
      {
        path: "/model/field/group",
        name: "FieldGroup",
        meta: {
          title: "字段分组",
          icon: "fieldset",
        },
        component: () => import("@/views/fields/group/index.vue"),
      },
      {
        path: "/model/detail",
        name: "DomainModel",
        component: () => import("@/views/model/index.vue"),
        meta: {
          title: "模型信息",
          icon: "class",
        },
      },
    ],
  },
  {
    path: "/devtools",
    meta: {
      title: "开发工具",
      icon: "devtools",
    },
    children: [
      {
        path: "/devtools/mybatis",
        name: "MyBatis",
        component: () => import("@/views/devtools/mybatis/index.vue"),
        meta: {
          title: "MyBatis",
          icon: "mybatis",
          keepAlive: true,
        },
      },
      // {
      //   path: "/devtools/collection",
      //   name: "其他",
      //   component: () => import("@/views/devtools/toolset/index.vue"),
      //   meta: {
      //     title: "其他",
      //     icon: "icon-edit-square",
      //     keepAlive: true,
      //   },
      // },
    ],
  },
  {
    path: "/codegen/template",
    meta: {
      title: "模板管理",
      icon: "template-manager",
    },
    children: [
      {
        path: "/codegen/template/list",
        meta: {
          title: "模板列表",
          icon: "template-list-fill",
        },
        component: () => import("@/views/template/index.vue"),
      }, {
        path: "/codegen/template/directives",
        meta: {
          title: "模板指令",
          icon: "template-directive",
        },
        component: () => import("@/views/template/directive/index.vue"),
      },
    ],
  },
  // {
  //   path: "/scripts",
  //   meta: {
  //     title: "脚本管理",
  //     icon: "icon-appstore",
  //   },
  //   component: () => import("@/views/scripts/index.vue"),
  //   children: [],
  // },
  /*
  {
    path: "/test",
    name: "测试",
    component: () => import("@/views/test/index.vue"),
    meta: {
      title: "测试",
      icon: "icon-edit-square",
    },
  },
  */
]

/**
 * 默认路由
 */
export const constantRoutes: RouteRecordRaw[] = [
  {
    path: "/redirect",
    component: () => import("../layout/index.vue"),
    children: [
      {
        path: "/redirect/:path(.*)",
        component: () => import("../layout/components/Router/Redirect.vue"),
      },
    ],
  },
  {
    path: "/",
    component: () => import("../layout/index.vue"),
    redirect: "/codegen/file",
    children: [...menuRoutes],
  },
  {
    path: "/404",
    component: () => import("../views/404.vue"),
  },
  {
    path: "/:pathMatch(.*)",
    redirect: "/404",
  },
]

export const router: Router = createRouter({
  history: createWebHashHistory(),
  routes: constantRoutes,
})
