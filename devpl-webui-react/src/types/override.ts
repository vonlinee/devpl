// https://juejin.cn/post/7081460215085793310
// 解决被 react.forwardRef 包裹的组件无法传递泛型参数
// declare module "react" {
//   function forwardRef<T, P = {}>(
//     render: (props: P, ref: React.Ref<T>) => React.ReactElement | null
//   ): (props: P & React.RefAttributes<T>) => React.ReactElement | null;
// }
