import React, { forwardRef, useImperativeHandle } from "react";
import * as monaco from "monaco-editor";
import MonacoEditor, { MonacoEditorProps } from "react-monaco-editor";

/**
 * 编辑器属性
 */
export interface EditorProps extends React.HTMLAttributes<HTMLElement> {
  lang?: string;
  value: string;
  theme?: string;
  width: "100%";
  height: "100%";
}

interface ReactMonacoEditorState {
  text: string;
  language?: string;
}

/**
 * 封装 ReactMoancoEditor
 * https://github.com/react-monaco-editor/react-monaco-editor
 * 
 * 文档提到可以使用ref来获取monaco editor实例，但是MonacoEditor是函数式组件
 * 
 */
export class ReactMonacoEditor extends React.Component<
  EditorProps,
  ReactMonacoEditorState
> {
  editor?: monaco.editor.IStandaloneCodeEditor = undefined;

  constructor(props: EditorProps) {
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
    let options: monaco.editor.IStandaloneEditorConstructionOptions = {
      language: props.lang ? props.lang : "java",
      value: "",
      selectOnLineNumbers: true,
    };
  }

  editorDidMount(editor: monaco.editor.IStandaloneCodeEditor, monaco: any) {
    editor.focus();
    this.editor = editor
  }

  editorWillMount() {
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
  }

  getText() {
    return this.editor?.getModel()?.getValue()
  }

  setText(val: string) {
    this.editor?.getModel()?.setValue(val)
  }

  getLanguage() {
    return this.state.language;
  }

  render(): React.ReactNode {

    const editorDidMount = (editor: monaco.editor.IStandaloneCodeEditor, monaco: any) => {
      // 绑定了this
      this.editorDidMount(editor, monaco)
    }

    return (
      <>
        <MonacoEditor
          language={this.state.language}
          editorWillMount={this.editorWillMount}
          value={this.state.text}
          editorDidMount={editorDidMount}
          height={400}
        />
      </>
    );
  }
}
