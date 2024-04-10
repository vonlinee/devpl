import {Allotment} from "allotment";
import "allotment/dist/style.css";
import DatabaseNavigationView from "./DatabaseNavigationView";
import ResultSetTableContainer from "./ResultSetTableContainer";

type DatabaseNavigationProps = {
  dataSourceId: number
}

/**
 * 数据库导航
 * @returns
 */
const DatabaseNavigation: React.FC<DatabaseNavigationProps> = ({dataSourceId}) => {
  return (<div style={{
    width: '100%',
    height: '800px'
  }}>
    <Allotment>
      <Allotment.Pane minSize={300} preferredSize={300}>
        <DatabaseNavigationView dataSourceId={dataSourceId}/>
      </Allotment.Pane>
      <Allotment.Pane>
        <ResultSetTableContainer/>
      </Allotment.Pane>
    </Allotment>
  </div>)
}

export default DatabaseNavigation
