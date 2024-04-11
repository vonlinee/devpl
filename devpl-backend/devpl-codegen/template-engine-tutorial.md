

# FreeMarker



https://freemarker.apache.org/

中文文档：http://www.kerneler.com/freemarker2.3.23/





# Velocity





https://velocity.apache.org/

## VTL
https://velocity.apache.org/engine/devel/vtl-reference.html

## 指令

### if 条件判断

判断非null

```velocity
#if( $value !=  null)
 something code
#end
```

(2) 判断非null或者true (空字符串不起作用)

```velocity
#if($value)
something code
#end
```

(3) 判断非null或者非空字符串
TODO 是否可以用String#length()进行判断

```velocity
#if( "$!value" != "")
something code
#end
```

(4) 数组中元素，非空判断后才能判断

```velocity
#foreach($item in $ListData)
#if($!{item} && $!{item.value} && $!{item.value} >0)
somthing code
#end
#end
```





### 嵌套指令

https://stackoverflow.com/questions/31258825/how-can-i-nest-velocity-custom-directives

对于嵌套指令，下面的语法是错误的

```velocity
#msg('a_resource','TODAY', #date(1234567890900,'date')))!
```

解析该模板会报错：

Caused by: org.apache.velocity.exception.ParseErrorException: Encountered "(" at StringTemplate[line 1, column 33]
Was expecting one of:
    "," ...
    "##" ...
    <WHITESPACE> ...
    <NEWLINE> ...

正确写法是使用双引号包裹：

```velocity
#msg('a_resource','TODAY', "#date(1234567890900,'date')"))!
```

Velocity将用双引号计算任何内容（如变量、方法和此处所需的指令）





# Groovy Template Engine







# Moustache

https://github.com/spullara/mustache.java









# Pebble

https://pebbletemplates.io/









# HTTL

https://httl.github.io/zh/









# template-benchmark

https://github.com/mbosecke/template-benchmark

























