
## 本地启动
通过git下载源码

后端启动
1. 创建数据库 执行db/mysql.sql文件，初始化数据
2. 修改application.yml，更新MySQL账号和密码、数据库名称
运行DevplApplication.java，启动后端项目

前端启动
1. npm install
2. npm run dev

## 问题汇总

codemirror6没有某些语言的支持，因此仍使用codemirror5版本
npm install vue-codemirror --save
npm i @codemirror/lang-javascript
npm i @codemirror/lang-cpp
npm i @codemirror/lang-java
npm i @codemirror/lang-xml
npm i @codemirror/lang-text
npm i @codemirror/lang-velocity
npm i @codemirror/lang-python或者npm install @codemirror/lang-python
npm i @codemirror/theme-one-dark
运行出现
Missing "./addon/hint/show-hint.css" export in "codemirror" package

```js
import "codemirror/addon/hint/show-hint.css";
```
vue-codemirror@4.x

