react-redux doc：https://react-redux.js.org/introduction/getting-started

useSelector

https://react-redux.js.org/api/hooks#useselector



Reducers

createStore  is deprecated.



useMemo

https://react.dev/reference/react/useMemo

缓存结果，重新渲染时



ReactRouterGenerator
文档参考: https://gitee.com/kong_yiji_and_lavmi/vite-plugin-react-router-generator



forwardRef



```tsx
let ref = React.createRef()

<MyComponent ref={ref}/>
```

报错：react-dom.development.js:86 Warning: Function components cannot be given refs. Attempts to access this ref will fail. Did you mean to use React.forwardRef()?

https://stackoverflow.com/questions/66664209/how-can-i-use-forwardref-in-react



useImperativeHandle



useRef()





###### 一、自定义组件使用ref并且透传子组件ref

自定义组件中使用ref需要用到react的2个hooks：
 1.forwardRef
 2.useImperativeHandle

- useImperativeHandle 可以让你在使用 ref 时自定义暴露给父组件的实例值
- useImperativeHandle 应当与 forwardRef 一起使用

自定义组件：





自定义函数式组件使用Ref

















