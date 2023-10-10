import React, {forwardRef, useState,} from "react";
import MonacoEditor from "react-monaco-editor";
import * as monaco from "monaco-editor";

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
const ReactMoancoEditor = (props: MonacoEditorProps, ref: any) => {
  const [text, setText] = useState("hello world");
  const [language, setLanguage] = useState("plain");

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
      <MonacoEditor
        language={language}
        editorWillMount={editorWillMount}
        value={text}
        onChange={onChangeCallback}
        height={400}
      />
    </>
  );
};

export default forwardRef(ReactMoancoEditor);
