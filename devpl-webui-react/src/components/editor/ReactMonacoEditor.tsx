import React, {
  Component,
  forwardRef,
  useImperativeHandle,
  useState,
} from "react";
import * as monaco from "monaco-editor";
import MonacoEditor from "react-monaco-editor";

/**
 * 编辑器属性
 */
export interface MonacoEditorProps extends React.HTMLAttributes<HTMLElement> {
  lang?: string;
  value: string;
  theme?: string;
  width: "100%";
  height: "100%";
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

interface ReactMonacoEditorState {
  text: string;
  language?: string;
}

/**
 * 封装 ReactMoancoEditor
 * https://github.com/react-monaco-editor/react-monaco-editor
 */
export class ReactMonacoEditor extends React.Component<
  MonacoEditorProps,
  ReactMonacoEditorState
> {
  // 编辑器实例
  private editor?: monaco.editor.IStandaloneCodeEditor;

  constructor(props: MonacoEditorProps) {
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

  /**
   * 实时更新输入的值
   * @param newValue 输入的值
   * @param e
   */
  onChangeCallback(
    newValue: string,
    e: monaco.editor.IModelContentChangedEvent
  ): void {
    this.state = {
      text: newValue,
    };
  }

  getText() {
    const model = this.refs.monaco.editor.getModel();
    const value = model.getValue();
    return value;
  }

  setText(val: string) {
    this.state = {
      text: val,
    };
  }

  getLanguage() {
    return this.state.language;
  }

  render(): React.ReactNode {
    return (
      <>
        <MonacoEditor
          language={this.state.language}
          editorWillMount={editorWillMount}
          value={this.state.text}
          onChange={this.onChangeCallback}
          height={400}
        />
      </>
    );
  }
}
