import React, { Component, forwardRef, useImperativeHandle, useState, } from "react";
import * as monaco from "monaco-editor";
import MonacoEditor from "react-monaco-editor";
import { extend } from "dayjs";

/**
 * 暴露的属性和方法
 */
export interface MonacoEditorRef {
  getText: () => string
  setText: (val: string) => void
}

interface ChildProps<T> {
  msg: string,
  ref: React.RefObject<T>
}

class Child extends React.Component<ChildProps<Child>> {

  sayHello() {
    console.log(this)
  }

  render() {
    return (
      <div>
        <h1>{this.props.msg}</h1>
        <input type="text"></input>
      </div>
    )
  }
}

class Parent extends React.Component {

  inputRef: React.RefObject<Child>;

  constructor(props: any) {
    super(props)

    this.inputRef = React.createRef()
  }

  show() {
    this.inputRef.current?.sayHello()
  }

  componentDidMount () {
    console.log(this.inputRef.current)
  }

  render() {
    return (<>
      <div>
        <button onClick={() => this.show()}>Show</button>
        <Child msg="hello" ref={this.inputRef}></Child>
      </div>
    </>)
  }
}






/**
 * 编辑器属性
 */
export interface MonacoEditorProps extends React.HTMLAttributes<HTMLElement> {
  lang?: string;
  value: string;
  theme?: string;
  width: '100%'
  height: '100%'
}

const editorWillMount = () => {
  monaco.languages.json.jsonDefaults.setDiagnosticsOptions({
    validate: true,
    schemas: [
      {
        uri: "http://myserver/foo-schema.json",
        fileMatch: ["*"],
        schema: {
          type: "object",
          properties: {
            p1: {
              enum: ["v1", "v2"],
            },
            p2: {
              $ref: "http://myserver/bar-schema.json",
            },
          },
        },
      },
    ],
  });
};

/**
 * 封装 ReactMoancoEditor
 * https://github.com/react-monaco-editor/react-monaco-editor
 */
export const ReactMonacoEditor = forwardRef<MonacoEditorRef, MonacoEditorProps>((props, ref) => {
  const [text, setText] = useState("hello world");
  const [language, setLanguage] = useState("plain");

  useImperativeHandle(ref, () => ({
    getText: () => text,
    setText: (val) => setText(val)
  }), [])

  /**
   * 实时更新输入的值
   * @param newValue 输入的值
   * @param e 
   */
  const onChangeCallback = (
    newValue: string,
    e: monaco.editor.IModelContentChangedEvent
  ): void => {
    setText(newValue);
  };

  /**
   * Moanco Editor Options
   * react-moanco-editor不支持Ref
   * https://microsoft.github.io/monaco-editor/docs.html#interfaces/editor.IStandaloneEditorConstructionOptions.html
   */
  let options: monaco.editor.IStandaloneEditorConstructionOptions = {
    language: props.lang ? props.lang : "java",
    value: text,
    selectOnLineNumbers: true
  };

  return (
    <>
      <Parent></Parent>
      <MonacoEditor
        language={language}
        editorWillMount={editorWillMount}
        value={text}
        onChange={onChangeCallback}
        height={400}
      />
    </>
  );
})
