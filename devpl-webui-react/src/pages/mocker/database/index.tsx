import { Allotment } from "allotment";
import "allotment/dist/style.css";
import DatabaseNavigationView from "./DatabaseNavigationView";
import ResultSetTable from "./ResultSetTable";

/**
 * 数据库导航
 * @returns 
 */
export default function DatabaseNavigation() {
    return (<>
        <Allotment>
            <Allotment.Pane minSize={200} preferredSize={200}>
                <DatabaseNavigationView></DatabaseNavigationView>
            </Allotment.Pane>
            <Allotment.Pane snap>
                <ResultSetTable></ResultSetTable>
            </Allotment.Pane>
        </Allotment>
    </>)
}
DatabaseNavigation.route = { [MENU_PATH]: "/mocker/database" }
