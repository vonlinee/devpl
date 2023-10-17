import React from "react";
import * as monaco from "monaco-editor";
import MonacoEditor from "react-monaco-editor";

type Monaco = typeof monaco

/**
 * 编辑器属性
 */
export interface ReactMonacoEditorProps extends React.HTMLAttributes<HTMLElement> {
  lang?: string;
  value: string;
  theme?: string;
  width: "100%";
  height: "100%";
}

/**
 * 编辑器State
 */
interface ReactMonacoEditorState {
  text: string;
  language?: string;
}

/**
 * 封装 ReactMoancoEditor
 * https://github.com/react-monaco-editor/react-monaco-editor
 */
export class ReactMonacoEditor extends React.Component<
  ReactMonacoEditorProps,
  ReactMonacoEditorState
> {

  /**
   * monaco editor 实例
   */
  editor?: monaco.editor.IStandaloneCodeEditor = undefined;

  /**
   * https://microsoft.github.io/monaco-editor/docs.html#interfaces/editor.IStandaloneEditorConstructionOptions.html
   */
  options: monaco.editor.IStandaloneEditorConstructionOptions | undefined = undefined;

  constructor(props: ReactMonacoEditorProps) {
    super(props);

    this.state = {
      text: "",
      language: "json",
    };

    /**
     * Moanco Editor Options
     * react-moanco-editor不支持Ref
     * https://microsoft.github.io/monaco-editor/docs.html#interfaces/editor.IStandaloneEditorConstructionOptions.html
     */
    this.options = {
      language: props.lang ? props.lang : "java",
      value: "",
      selectOnLineNumbers: true,
    };
  }

  /**
   * 编辑器挂载完成回调
   * @param editor 
   * @param monaco 
   */
  editorDidMount(editor: monaco.editor.IStandaloneCodeEditor, monaco: Monaco) {
    editor.focus();
    this.editor = editor
  }

  /**
   * 编辑器挂载前回调
   * @param editor 
   * @param monaco 
   */
  editorWillMount(monaco: Monaco) {
    // monaco.languages.json.jsonDefaults.setDiagnosticsOptions({
    //   validate: true,
    //   schemas: [
    //     {
    //       uri: "http://myserver/foo-schema.json",
    //       fileMatch: ["*"],
    //       schema: {
    //         type: "object",
    //         properties: {
    //           p1: {
    //             enum: ["v1", "v2"],
    //           },
    //           p2: {
    //             $ref: "http://myserver/bar-schema.json",
    //           },
    //         },
    //       },
    //     },
    //   ],
    // });
  }

  editorWillUnmount(editor: monaco.editor.IStandaloneCodeEditor, monaco: Monaco) {
    editor.getModel()?.setValue("")
  }

  /**
   * 获取编辑器输入的文本
   * @returns 
   */
  getText() {
    return this.editor?.getModel()?.getValue()
  }

  /**
   * 设置编辑器输入的文本
   * @param val 
   */
  setText(val: string) {
    this.editor?.getModel()?.setValue(val)
  }

  getLanguage() {
    return this.editor?.getModel()?.getLanguageId();
  }

  render(): React.ReactNode {
    /**
     * TODO react-monaco-editor文档说可以使用ref，但是MonacoEditor是函数式组件，且未使用
     * forwardRef包裹，因此使用此方法保存 monaco editor 实例
     * @param editor 
     * @param monaco 
     */
    const editorDidMount = (editor: monaco.editor.IStandaloneCodeEditor, monaco: Monaco) => {
      // 绑定了this
      this.editorDidMount(editor, monaco)
    }

    const editorWillUnmount = (editor: monaco.editor.IStandaloneCodeEditor, monaco: Monaco) => {
      // 绑定了this
      this.editorWillUnmount(editor, monaco)
    }

    return (
      <MonacoEditor
        language={this.props.lang}
        theme={this.props.theme}
        options={this.options}
        editorWillMount={this.editorWillMount}
        editorWillUnmount={editorWillUnmount}
        value={this.props.value}
        editorDidMount={editorDidMount}
        height={this.props.height}
        width={this.props.width}></MonacoEditor>
    );
  }
}
