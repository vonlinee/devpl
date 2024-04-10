import * as monaco from 'monaco-editor/esm/vs/editor/editor.api.js';
import { Editor } from '@monaco-editor/react';
import React from 'react';

export declare type Monaco = typeof monaco;

export type EditorInstance = monaco.editor.IStandaloneCodeEditor;

type MonaocEditorProps = {
  id: string
  width: number | string;
  height: number | string;
  lang: string;
};

export type MonacoEditorRef = {
  getText: () => string;
  setText: (text: string) => void;
};

/**
 * Monaco Editor
 * https://microsoft.github.io/monaco-editor/docs.html#modules/editor.html
 *
 * https://github.com/suren-atoyan/monaco-react
 */
const MonacoEditor = React.forwardRef<MonacoEditorRef, MonaocEditorProps>((props, ref) => {
  const editorRef = React.useMemo(() => React.useRef<EditorInstance>(), [1]);

  // 使用此组件会报错 TypeError: Cannot read property 'length' of null 指向第31行 暂不知道原因
  const onEditorMount = React.useCallback((editor: EditorInstance, _monaco: Monaco) => {
    debugger
    editorRef.current = editor;
  }, []);

  React.useImperativeHandle(
    ref,
    () => ({
      getText: () => editorRef.current?.getModel()?.getValue() || '',
      setText: (text: string) => editorRef.current?.getModel()?.setValue(text),
    }),
    [props.id],
  );
  return <Editor width={props.width} height={props.height} defaultLanguage={props.lang} onMount={onEditorMount} />;
});

export default MonacoEditor