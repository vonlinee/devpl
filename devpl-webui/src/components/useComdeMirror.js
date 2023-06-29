/**
 * 按需引入需要的模块
 * @param mode
 */
export function importLanguage(mode) {
  let result
  if ('sql' === mode) {
    result = import('@codemirror/lang-sql')
  } else if ('xml' === mode) {
    result = import('@codemirror/lang-xml')
  } else if ('cpp' === mode) {
    result = import('@codemirror/lang-cpp')
  } else if ('java' === mode) {
    result = import('@codemirror/lang-java')
  } else if ('javascript' === mode) {
    result = import('@codemirror/lang-javascript')
  } else if ('python' === mode) {
    result = import('@codemirror/lang-python')
  } else {
    result = import('@codemirror/lang-javascript')
  }
  return result
}

/**
 * 获取语言模式
 * @param mode 简写形式
 * @return {null}
 */
export function getLanguageMode(mode) {
  let languageMode
  switch (mode) {
    case 'sql':
      // https://codemirror.net/5/mode/sql/index.html
      languageMode = 'text/x-sql'
      break
    case 'java':
      languageMode = 'text/x-java'
      break
    case 'javascript':
      languageMode = 'text/javascript'
      break
    case 'xml':
      languageMode = 'application/xml'
      break
    case 'html':
      languageMode = 'text/html'
      break
    default:
      languageMode = 'text/plain'
      break
  }
  return languageMode
}