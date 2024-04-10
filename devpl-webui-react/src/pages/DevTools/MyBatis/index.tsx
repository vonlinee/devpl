import { Editor } from '@monaco-editor/react';
import ParamTable from './ParamTable';
import { Button, Flex, Switch } from 'antd';
import { Allotment } from 'allotment';
import { useEffect, useRef, useState } from 'react';
import { apiGetSampleInput, apiGetSql, getMapperStatementParams } from '@/services/tools/mybatis';

import * as monaco from 'monaco-editor/esm/vs/editor/editor.api.js';
import { editor } from 'monaco-editor';

type EditorInstance = monaco.editor.IStandaloneCodeEditor;

declare type Monaco = typeof monaco;

/**
 * MyBatis工具
 * @returns
 */
const MyBatisTool = () => {

  // 参数节点
  const [paramNodes, setParamNodes] = useState<ParamNode[]>([]);
  // 分割线大小
  const [dividerSize, setDividerSize] = useState(300);
  // 表格最大高度
  const height = 800;

  const [inputText, setInputText] = useState<string>('');
  const [resultText, setResultText] = useState<string>('');

  const inputEditorRef = useRef<EditorInstance>();
  const outputEditorRef = useRef<EditorInstance>();

  function handleEditor1DidMount(editor: EditorInstance, _monaco: Monaco) {
    inputEditorRef.current = editor;
  }

  function handleEditor2DidMount(editor: EditorInstance, _monaco: Monaco) {
    outputEditorRef.current = editor;
  }

  const [getSqlOptions, setGetSqlOptions] = useState<MyBatisToolOptions>({
    enableTypeInference: true,
  });

  const setText = (
    editorRef: React.MutableRefObject<editor.IStandaloneCodeEditor | undefined>,
    val: string,
  ) => {
    if (editorRef.current) {
      editorRef.current.getModel()?.setValue(val);
    }
  };

  // 获取sql
  function getSqlOfMapperStatement(real: boolean) {
    if (inputText.length <= 0) {
      return;
    }
    apiGetSql(inputText, [], real).then((res) => {
      setText(outputEditorRef, res.data);
    });
  }

  const fillSampleText = () => {
    apiGetSampleInput().then((res) => {
      setInputText(res.data);
      setText(inputEditorRef, res.data);
    });
  };

  const fillParamTable = () => {
    getMapperStatementParams(inputText, getSqlOptions).then((res) => {
      setParamNodes(res.data);
    });
  };

  useEffect(() => {
    fillSampleText();
  }, []);

  return (
    <>
      <Flex
        style={{
          width: '100%',
          height: height,
        }}
      >
        <Allotment
          onChange={(sizes : any[]) => {
            setDividerSize(sizes[0]);
          }}
        >
          <Allotment.Pane minSize={300} preferredSize={dividerSize}>
            <Flex vertical gap={5} style={{ width: dividerSize, height: height }}>
              <Allotment vertical>
                <Allotment.Pane preferredSize={300}>
                  <Editor
                    width={dividerSize}
                    defaultLanguage="xml"
                    defaultValue={inputText}
                    onChange={(val : any) => setInputText(val || '')}
                    onMount={handleEditor1DidMount}
                  />
                </Allotment.Pane>
                <Allotment.Pane>
                  <Editor
                    width={dividerSize}
                    defaultLanguage="sql"
                    defaultValue={resultText}
                    onMount={handleEditor2DidMount}
                  />
                </Allotment.Pane>
              </Allotment>
            </Flex>
          </Allotment.Pane>
          <Allotment.Pane>
            <ParamTable initialdataSource={paramNodes} initialHeight={height}></ParamTable>
            <Button onClick={() => fillParamTable()}>解析参数</Button>
            <Button onClick={() => getSqlOfMapperStatement(false)}>预编译sql</Button>
            <Button onClick={() => getSqlOfMapperStatement(true)}>可执行sql</Button>
            <Button onClick={() => fillSampleText()}>填充示例</Button>

            <Flex
              style={{
                margin: 10,
              }}
            >
              <Switch
                checkedChildren="开启类型推断"
                unCheckedChildren="关闭类型推断"
                defaultChecked
                onChange={(val : any) => {
                  getSqlOptions.enableTypeInference = val;
                  setGetSqlOptions({
                    ...getSqlOptions,
                    enableTypeInference: val,
                  });
                }}
              ></Switch>
            </Flex>
          </Allotment.Pane>
        </Allotment>
      </Flex>
    </>
  );
};

export default MyBatisTool;
