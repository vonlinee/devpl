
function _s<T>(object: T | null, defaultValue: T = {} as T): T {

  console.log("hello");
  
  return object == null
    ? defaultValue
    : object as T;
}

// Global declaration
declare var s: typeof _s;

// Global scope augmentation
var window = window || null;

// node环境下 const _global = window as any;
const _global = window as any;
_global.s = _s;