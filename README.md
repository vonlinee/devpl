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

文件类型与模板
该类型的文件生成使用什么模板

模板 + 数据 => 文件

1. 生成任务

一切都围绕模板来实现

提供一些方便导入模板数据的界面功能

JDK 17

```text
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'sqlSessionFactory' defined in class path resource [com/baomidou/mybatisplus/autoconfigure/MybatisPlusAutoConfiguration.class]: Failed to instantiate [org.apache.ibatis.session.SqlSessionFactory]: Factory method 'sqlSessionFactory' threw exception with message: Receiver class org.apache.xerces.jaxp.DocumentBuilderFactoryImpl does not define or inherit an implementation of the resolved method 'abstract void setFeature(java.lang.String, boolean)' of abstract class javax.xml.parsers.DocumentBuilderFactory.
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:657)
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:645)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1325)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1162)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:562)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:522)
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:326)
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:324)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:200)
	at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:254)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1405)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1325)
	at org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(ConstructorResolver.java:885)
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:789)
	... 55 common frames omitted
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.apache.ibatis.session.SqlSessionFactory]: Factory method 'sqlSessionFactory' threw exception with message: Receiver class org.apache.xerces.jaxp.DocumentBuilderFactoryImpl does not define or inherit an implementation of the resolved method 'abstract void setFeature(java.lang.String, boolean)' of abstract class javax.xml.parsers.DocumentBuilderFactory.
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:171)
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:653)
	... 69 common frames omitted
Caused by: java.lang.AbstractMethodError: Receiver class org.apache.xerces.jaxp.DocumentBuilderFactoryImpl does not define or inherit an implementation of the resolved method 'abstract void setFeature(java.lang.String, boolean)' of abstract class javax.xml.parsers.DocumentBuilderFactory.
	at org.apache.ibatis.parsing.XPathParser.createDocument(XPathParser.java:234)
	at org.apache.ibatis.parsing.XPathParser.<init>(XPathParser.java:127)
	at org.apache.ibatis.builder.xml.XMLMapperBuilder.<init>(XMLMapperBuilder.java:81)
	at com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean.buildSqlSessionFactory(MybatisSqlSessionFactoryBean.java:572)
	at com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean.afterPropertiesSet(MybatisSqlSessionFactoryBean.java:444)
	at com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean.getObject(MybatisSqlSessionFactoryBean.java:608)
	at com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration.sqlSessionFactory(MybatisPlusAutoConfiguration.java:224)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:139)
	... 70 common frames omitted
```

解决办法：https://stackoverflow.com/questions/62285448/java-lang-abstractmethoderror-org-apache-xerces-jaxp-documentbuilderfactoryimpl

```xml
<!-- https://mvnrepository.com/artifact/xerces/xercesImpl -->
<dependency>
    <groupId>xerces</groupId>
    <artifactId>xercesImpl</artifactId>
    <version>2.12.0</version>
</dependency>
```

