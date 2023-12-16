import {
  createRouter,
  createWebHashHistory,
  Router,
  RouteRecordRaw
} from "vue-router";

/**
 * 菜单路由
 * https://router.vuejs.org/zh/guide/
 */
export const menuRoutes: RouteRecordRaw[] = [
  {
    path: "/datasource",
    name: "DataSource",
    component: () => import("../views/datasource/index.vue"),
    meta: {
      title: "数据源管理",
      icon: "icon-database-fill"
    }
  },
  {
    path: "/codegen",
    meta: {
      title: "代码生成",
      icon: "icon-appstore"
    },
    children: [
      {
        path: "/codegen/template",
        meta: {
          title: "模板管理",
          icon: "icon-appstore"
        },
        component: () => import("@/views/template/index.vue"),
        children: []
      },
      {
        path: "/codegen/file",
        name: "Generator",
        component: () => import("@/views/generator/index.vue"),
        meta: {
          title: "文件生成",
          icon: "icon-fire"
        }
      },
      {
        path: "/codegen/project",
        name: "ProjectIndex",
        component: () => import("@/views/project/index.vue"),
        meta: {
          title: "项目管理",
          icon: "icon-edit-square"
        }
      }
    ]
  },
  {
    path: "/scripts",
    meta: {
      title: "脚本管理",
      icon: "icon-appstore"
    },
    component: () => import("@/views/scripts/index.vue"),
    children: []
  },
  {
    path: "/model",
    name: "领域模型",
    meta: {
      title: "领域模型",
      icon: "icon-appstore"
    },
    children: [
      {
        path: "/codegen/datatype",
        name: "FieldType",
        component: () => import("@/views/datatype/index.vue"),
        meta: {
          title: "类型系统",
          icon: "icon-menu"
        }
      },
      {
        path: "/fields",
        meta: {
          title: "字段信息",
          icon: "icon-appstore"
        },
        component: () => import("@/views/fields/index.vue"),
        children: []
      },
      {
        path: "/codegen/model",
        name: "DomainModel",
        component: () => import("@/views/model/index.vue"),
        meta: {
          title: "模型管理",
          icon: "icon-cluster"
        }
      }
    ]
  },
  {
    path: "/devtools",
    meta: {
      title: "开发工具",
      icon: "icon-appstore"
    },
    children: [
      {
        path: "/devtools/database",
        name: "数据库",
        component: () => import("@/views/devtools/database/index.vue"),
        meta: {
          title: "数据库",
          icon: "icon-fire"
        }
      },
      {
        path: "/devtools/mybatis",
        name: "MyBatis",
        component: () => import("@/views/devtools/mybatis/index.vue"),
        meta: {
          title: "MyBatis",
          icon: "icon-edit-square",
          keepAlive: true
        }
      },
      {
        path: "/devtools/mocker",
        name: "数据模拟",
        component: () => import("@/views/devtools/mocker/index.vue"),
        meta: {
          title: "数据模拟",
          icon: "icon-edit-square",
          keepAlive: true
        }
      },
      {
        path: "/devtools/designer",
        name: "设计工具",
        component: () => import("@/views/devtools/designer/index.vue"),
        meta: {
          title: "设计工具",
          icon: "icon-edit-square",
          keepAlive: true
        }
      },
      {
        path: "/devtools/collection",
        name: "工具箱",
        component: () => import("@/views/devtools/toolset/index.vue"),
        meta: {
          title: "工具箱",
          icon: "icon-edit-square",
          keepAlive: true
        }
      }
    ]
  },
  {
    path: "/test",
    name: "测试",
    component: () => import("@/views/test/index.vue"),
    meta: {
      title: "测试",
      icon: "icon-edit-square"
    }
  }
];

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
        component: () => import("../layout/components/Router/Redirect.vue")
      }
    ]
  },
  {
    path: "/",
    component: () => import("../layout/index.vue"),
    redirect: "/codegen/file",
    children: [...menuRoutes]
  },
  {
    path: "/404",
    component: () => import("../views/404.vue")
  },
  {
    path: "/:pathMatch(.*)",
    redirect: "/404"
  }
];

export const router: Router = createRouter({
  history: createWebHashHistory(),
  routes: constantRoutes
});
