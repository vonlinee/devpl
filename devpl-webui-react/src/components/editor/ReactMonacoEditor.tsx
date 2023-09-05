import React from "react";
import MonacoEditor from "react-monaco-editor";
import * as monaco from 'monaco-editor'

/**
 * 编辑器属性
 */
export interface MonacoEditorProps {
  lang: string
}

/**
 * Moanco Editor Options
 * https://microsoft.github.io/monaco-editor/docs.html#interfaces/editor.IStandaloneEditorConstructionOptions.html
 */
let options: monaco.editor.IStandaloneEditorConstructionOptions = {

}


const ReactMoancoEditor = () => {


  return (
    <>
    <MonacoEditor language={"js"} editorWillMount={this.editorWillMount} />
  </>
  )
}

/**
 * 封装 ReactMoancoEditor
 * https://github.com/react-monaco-editor/react-monaco-editor
 */
class ReactMonacoEditor extends React.Component {

  constructor(props: MonacoEditorProps) {
    super(props)
  }

  editorWillMount() {
      monaco.languages.json.jsonDefaults.setDiagnosticsOptions({
          validate: true,
          schemas: [{
              uri: "http://myserver/foo-schema.json",
              fileMatch: ['*'],
              schema: {
                  type: "object",
                  properties: {
                      p1: {
                          enum: [ "v1", "v2"]
                      },
                      p2: {
                          $ref: "http://myserver/bar-schema.json"
                      }
                  }
              }
          }]
      });
  }
  render() {
      return (
        <>
          <MonacoEditor language={"js"} editorWillMount={this.editorWillMount} />
          {console.log(this.props)}
        </>
      );
  }
}

export default ReactMonacoEditor

