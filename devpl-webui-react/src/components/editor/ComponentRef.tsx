import React, { forwardRef, Ref, ForwardRefRenderFunction, ForwardedRef } from 'react';  
  
type MyComponentProps<T extends HTMLElement> = {  
  ref: React.Ref<T>;  
  // 其他属性...  
};  
  
type ForwardedRefExoticComponent<P extends MyComponentProps<HTMLElement>> = ForwardRefRenderFunction<P, ForwardedRef<P>>;  
  
const MyComponent: ForwardedRefExoticComponent<MyComponentProps<HTMLElement>> = forwardRef((props, ref) => {  
  // ...函数组件的代码...  
});

export default MyComponent;