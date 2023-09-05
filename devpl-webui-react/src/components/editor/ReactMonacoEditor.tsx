import React, { useDebugValue, useState } from "react";
import MonacoEditor from "react-monaco-editor";
import * as monaco from "monaco-editor";

/**
 * 编辑器属性
 */
export interface MonacoEditorProps {
  lang?: string;
  text: string;
  theme?: string;
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
const ReactMoancoEditor = (props: MonacoEditorProps) => {

  const [text, setText] = useState("hello world");
  const [language, setLanguage] = useState("plain");

  const onChangeCallback = (newValue: string, e: monaco.editor.IModelContentChangedEvent) => {
    setText(newValue)
  }

  /**
   * Moanco Editor Options
   * https://microsoft.github.io/monaco-editor/docs.html#interfaces/editor.IStandaloneEditorConstructionOptions.html
   */
  let options: monaco.editor.IStandaloneEditorConstructionOptions = {
    language: props.lang ? props.lang : "java",
    value: text
  };

  return (
    <>
      <MonacoEditor language={language} editorWillMount={editorWillMount} value={text} onChange={onChangeCallback}/>
    </>
  );
};

export default ReactMoancoEditor;
