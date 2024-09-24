# 版本要求

JDK: 17.0.5+
Maven: 3.6.0+

1. element-plus: https://element-plus.gitee.io/zh-CN/component/button.html
2. vxe-table: https://vxetable.cn/#/table/start/install

# 问题记录

```text
2023-09-07 09:44:52.994  INFO 18832 --- [nio-8088-exec-8] o.apache.tomcat.util.http.parser.Cookie  :
A cookie header was received [Hm_lvt_e3522da961ab5db57e945e810f2e3e33=1693878457,1693902301,1693970922,1693993389;]
that contained an invalid cookie. That cookie will be ignored.
Note: further occurrences of this error will be logged at DEBUG level.
```

springboot3 直接使用@Resource注解进行依赖注入失效

# 参考资料

1. 数据库建表规范
   https://github.com/alibaba/p3c/blob/master/p3c-gitbook/MySQL%E6%95%B0%E6%8D%AE%E5%BA%93/%E5%BB%BA%E8%A1%A8%E8%A7%84%E7%BA%A6.md

# TODO

1.代码生成器自定义模板，尽量做到灵活

2.mock数据工具

生成对应的代码，数据库表mock等等

3.

实体转sql

sql转实体类等等

Xcheck

https://xcheck.tencent.com/index

模拟数据生成器

1. 模型内部数据项之间的关联关系
2. 模型实例与实例之间的关联关系

数据模拟参考文章：
https://www.jianshu.com/p/533adbc1d37f

TODO

简化代码
去掉@RequestBody注解，默认都是JSON形式数据

# 日志转SQL

```plain
==>  Preparing: SELECT id,project_name,project_code,project_package,project_path,modify_project_name,modify_project_code,modify_project_package,exclusions,modify_suffix,version,backend_path,frontend_path,status,build_tool,update_time,create_time FROM project_info WHERE id=?
==> Parameters: 89(Long)
<==    Columns: id, project_name, project_code, project_package, project_path, modify_project_name, modify_project_code, modify_project_package, exclusions, modify_suffix, version, backend_path, frontend_path, status, build_tool, update_time, create_time
<==        Row: 89, devpl-backend, null, null, D:\Develop\Code\devpl-backend\devpl-backend, null, null, null, null, null, null, null, null, null, null, null, null
<==      Total: 1
```

# 数据库迁移工具 TODO

1.表结构转换

https://blog.csdn.net/TIME_1981/article/details/128468035

2.数据转换

解析javadoc
https://github.com/tangcent/easy-api











